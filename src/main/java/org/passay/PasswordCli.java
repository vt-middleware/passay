/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import org.passay.dictionary.FileWordList;
import org.passay.dictionary.TernaryTreeDictionary;

/**
 * Provides a simple command line interface to password validation.
 *
 * @author  Middleware Services
 */

public final class PasswordCli
{


  /** Default constructor. */
  private PasswordCli() {}


  /**
   * Provides command line access to password rules.
   *
   * @param  args  from command line
   *
   * @throws  Exception  if an error occurs
   */
  public static void main(final String[] args)
    throws Exception
  {
    final List<Rule> rules = new ArrayList<>();
    String username = null;
    String password = null;
    try {
      if (args.length < 2) {
        throw new ArrayIndexOutOfBoundsException();
      }
      for (int i = 0; i < args.length; i++) {
        if ("-l".equals(args[i])) {
          final int min = Integer.parseInt(args[++i]);
          final int max = Integer.parseInt(args[++i]);
          final LengthRule rule = new LengthRule(min, max);
          rules.add(rule);
        } else if ("-c".equals(args[i])) {
          final CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule();
          rule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, Integer.parseInt(args[++i])));
          rule.getRules().add(new CharacterRule(EnglishCharacterData.Alphabetical, Integer.parseInt(args[++i])));
          rule.getRules().add(new CharacterRule(EnglishCharacterData.Special, Integer.parseInt(args[++i])));
          rule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, Integer.parseInt(args[++i])));
          rule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, Integer.parseInt(args[++i])));
          rule.setNumberOfCharacteristics(Integer.parseInt(args[++i]));
          rules.add(rule);
        } else if ("-d".equals(args[i])) {
          final TernaryTreeDictionary dict = new TernaryTreeDictionary(
            new FileWordList(new RandomAccessFile(args[++i], "r"), false));
          final DictionarySubstringRule rule = new DictionarySubstringRule(dict);
          rule.setMatchBackwards(true);
          rules.add(rule);
        } else if ("-u".equals(args[i])) {
          rules.add(new UsernameRule(true, true));
          username = args[++i];
        } else if ("-s".equals(args[i])) {
          rules.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty));
          rules.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical));
          rules.add(new IllegalSequenceRule(EnglishSequenceData.Numerical));
          rules.add(new RepeatCharacterRegexRule());
        } else if ("-h".equals(args[i])) {
          throw new ArrayIndexOutOfBoundsException();
        } else {
          password = args[i];
        }
      }

      if (password == null) {
        throw new ArrayIndexOutOfBoundsException();
      } else {
        final RuleResult result;
        final PasswordData pd = new PasswordData(password);
        final PasswordValidator validator = new PasswordValidator(rules);
        if (username != null) {
          pd.setUsername(username);
        }
        result = validator.validate(pd);
        if (result.isValid()) {
          System.out.println("Valid password");
        } else {
          validator.getMessages(result).forEach(System.out::println);
        }
      }

    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Usage: java " + PasswordCli.class.getName() + " <options> <password>");
      System.out.println();
      System.out.println("Options:");
      System.out.println("    -l <min> <max>");
      System.out.println("       set the min/max password length");
      System.out.println("    -c <digits> <alphabetical> <non-alphanumeric> <uppercase> <lowercase> <num>");
      System.out.println("       set the characters which must be present in the password");
      System.out.println("       the value for each category must be >= 0");
      System.out.println("       <num> is the number of these rules to enforce");
      System.out.println("    -d <file> <num>");
      System.out.println("       test password against a dictionary file");
      System.out.println("       <file> is a dictionary files");
      System.out.println("    -u <username>");
      System.out.println("       test for a given username");
      System.out.println("    -s test for sequences");
      System.out.println("    -h print this help message");
      System.exit(1);
    }
  }
}

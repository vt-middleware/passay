/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServer;
import com.sun.management.HotSpotDiagnosticMXBean;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.passay.generate.PasswordGenerator;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.AllowedRegexRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterOccurrencesRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.DictionarySubstringRule;
import org.passay.rule.DigestHistoryRule;
import org.passay.rule.DigestSourceRule;
import org.passay.rule.IllegalCharacterRule;
import org.passay.rule.IllegalRegexRule;
import org.passay.rule.IllegalSequenceRule;
import org.passay.rule.LengthComplexityRule;
import org.passay.rule.LengthRule;
import org.passay.rule.NumberRangeRule;
import org.passay.rule.RepeatCharacterRegexRule;
import org.passay.rule.RepeatCharactersRule;
import org.passay.rule.Rule;
import org.passay.rule.UsernameRule;
import org.passay.rule.WhitespaceRule;
import org.passay.support.HistoricalReference;
import org.passay.support.Reference;
import org.passay.support.SourceReference;

/**
 * Performs a password validation and generation, then dumps the JVM heap.
 * Execute this class by invoking:
 * <pre>
   java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC -cp $PATH_TO_DEPENDENCIES org.passay.HeapDump PATH_TO_FILE
 * </pre>
 *
 * @author  Middleware Services
 */
public final class HeapDump
{


  /** Default constructor. */
  private HeapDump() {}


  /**
   * Main method.
   *
   * @param  args command line arguments
   *
   * @throws  Exception if an error occurs
   */
  public static void main(final String[] args)
    throws Exception
  {
    validatePasswords();
    generatePassword();
    dumpHeap(args[0], false);
  }


  /**
   * Performs a password validation.
   *
   * @throws  Exception  on error
   */
  public static void validatePasswords()
    throws Exception
  {
    final ArrayWordList awl = WordLists.createFromReader(
      new FileReader[] {new FileReader("src/test/resources/web2-gt3")},
      false,
      new ArraysSort());
    final Dictionary dict = new WordListDictionary(awl);

    final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule(
      3,
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1));

    final WhitespaceRule whitespaceRule = new WhitespaceRule();

    final LengthRule lengthRule = new LengthRule(8, 16);

    final DictionarySubstringRule dictRule = new DictionarySubstringRule(dict, true);

    final IllegalSequenceRule qwertySeqRule = new IllegalSequenceRule(EnglishSequenceData.USQwerty);

    final IllegalSequenceRule alphaSeqRule = new IllegalSequenceRule(EnglishSequenceData.Alphabetical);

    final IllegalSequenceRule numSeqRule = new IllegalSequenceRule(EnglishSequenceData.Numerical);

    final RepeatCharacterRegexRule dupSeqRule = new RepeatCharacterRegexRule();

    final UsernameRule userIDRule = new UsernameRule(true, true);

    final EncodingHashBean sha1Bean = new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"));
    final List<Reference> references = new ArrayList<>();
    final DigestHistoryRule historyRule = new DigestHistoryRule(sha1Bean);
    references.add(new HistoricalReference("history", "safx/LW8+SsSy/o3PmCNy4VEm5s="));
    references.add(new HistoricalReference("history", "zurb9DyQ5nooY1la8h86Bh0n1iw="));
    references.add(new HistoricalReference("history", "bhqabXwE3S8E6xNJfX/d76MFOCs="));

    final DigestSourceRule sourceRule = new DigestSourceRule(sha1Bean);
    references.add(new SourceReference("source", "CJGTDMQRP+rmHApkcijC80aDV0o="));

    final AllowedCharacterRule allowedCharacterRule = new AllowedCharacterRule(
      new UnicodeString(
        EnglishCharacterData.LowerCase.getCharacters() +
          EnglishCharacterData.UpperCase.getCharacters() +
          EnglishCharacterData.Digit.getCharacters()));
    final IllegalCharacterRule illegalCharacterRule = new IllegalCharacterRule(
      new UnicodeString('\u0000'));
    final CharacterOccurrencesRule occurrencesRule = new CharacterOccurrencesRule(3);
    final LengthComplexityRule lengthComplexityRule = new LengthComplexityRule(
      new LengthComplexityRule.Entry(
        "[0,12)",
        new LengthRule(8, 64),
        new CharacterCharacteristicsRule(
          2,
          new CharacterRule(EnglishCharacterData.Digit, 1),
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1))));
    final NumberRangeRule numberRangeRule = new NumberRangeRule(100, 1000);
    final RepeatCharactersRule repeatCharactersRule = new RepeatCharactersRule();
    final AllowedRegexRule allowedRegexRule = new AllowedRegexRule(".*");
    final IllegalRegexRule illegalRegexRule = new IllegalRegexRule("[\uffff]+");

    final List<Rule> rules = new ArrayList<>();
    rules.add(charRule);
    rules.add(whitespaceRule);
    rules.add(lengthRule);
    rules.add(dictRule);
    rules.add(qwertySeqRule);
    rules.add(alphaSeqRule);
    rules.add(numSeqRule);
    rules.add(dupSeqRule);
    rules.add(userIDRule);
    rules.add(historyRule);
    rules.add(sourceRule);
    rules.add(allowedCharacterRule);
    rules.add(illegalCharacterRule);
    rules.add(occurrencesRule);
    rules.add(lengthComplexityRule);
    rules.add(numberRangeRule);
    rules.add(repeatCharactersRule);
    rules.add(allowedRegexRule);
    rules.add(illegalRegexRule);
    final DefaultPasswordValidator validator = new DefaultPasswordValidator(rules);
    final char[] pass = new char[] {'a', 'B', 'c', 'D', '3', 'F', 'g', 'H', '1', 'J', 'k'};
    final UnicodeString password = new UnicodeString(pass);
    final PasswordData passwordData = new PasswordData(new UnicodeString("heapdump"), password, references);
    try {
      final ValidationResult result = validator.validate(passwordData);
      System.out.println(result);
      printPassword("Validated password", password);
    } finally {
      PassayUtils.clear(pass);
      passwordData.clear();
    }
  }


  /**
   * Performs a password generation.
   */
  public static void generatePassword()
  {
    final PasswordGenerator generator = new PasswordGenerator(
      22,
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 20));
    final UnicodeString password = generator.generate();
    try {
      printPassword("Generated password", password);
    } finally {
      password.clear();
    }
  }


  /**
   * Dumps the JVM heap to the supplied output file.
   *
   * @param  outputFile  to write hprof file to
   * @param  live  whether to dump only live objects
   *
   * @throws  Exception  on error
   */
  public static void dumpHeap(final String outputFile, final boolean live)
    throws Exception
  {
    final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    final HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
      server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
    mxBean.dumpHeap(outputFile, live);
  }


  /**
   * Prints a message to {@link System#out} containing the supplied password.
   *
   * @param  msg  to prefix password
   * @param  password  to print as a character array
   */
  public static void printPassword(final String msg, final UnicodeString password)
  {
    final CharBuffer buffer = CharBuffer.wrap(password.toCharArray());
    try {
      System.out.print(msg + ": '");
      for (char c : buffer.array()) {
        System.out.print(c);
      }
      System.out.println("'");
    } finally {
      PassayUtils.clear(buffer);
    }
  }
}

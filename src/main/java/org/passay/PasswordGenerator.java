/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.Buffer;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.IllegalCharacterRule;
import org.passay.rule.Rule;

/**
 * Creates passwords that meet password character rule criteria.
 *
 * @author  Sean C. Sullivan
 * @author  Middleware Services
 */
public class PasswordGenerator
{
  /** Maximum supported length of password generation. */
  private static final int MAX_PASSWORD_LENGTH = 1024;

  /** Retry password generation at most this many times. */
  private static final int RETRY_LIMIT = 2;

  /** Source of random data. */
  private final Random random;

  /** Length of passwords to generate. */
  private final int length;

  /** Rules to determine password character appenders. */
  private final List<Rule> passwordRules = new ArrayList<>();

  /** Character appenders derived from the rules. */
  private final List<CharacterAppender> characterAppenders = new ArrayList<>();

  /** Valid characters used to satisfy the requested password length. */
  private final UnicodeString fillCharacters;

  /** Tracks the total number of retries. */
  private int retryCount;


  /**
   * Creates a new password generator.
   *
   * @param  length  of the password to generate
   * @param  rules  to govern the content of the password
   */
  public PasswordGenerator(final int length, final Rule... rules)
  {
    this(new SecureRandom(), length, Arrays.asList(rules));
  }


  /**
   * Creates a new password generator.
   *
   * @param  length  of the password to generate
   * @param  rules  to govern the content of the password
   */
  public PasswordGenerator(final int length, final List<? extends Rule> rules)
  {
    this(new SecureRandom(), length, rules);
  }


  /**
   * Creates a new password generator.
   *
   * @param  random  for ordering of password characters
   * @param  length  of the password to generate
   * @param  rules  to govern the content of the password
   */
  public PasswordGenerator(final Random random, final int length, final List<? extends Rule> rules)
  {
    if (length <= 0) {
      throw new IllegalArgumentException("Length must be greater than 0");
    }
    if (length > MAX_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Length must be less than " + MAX_PASSWORD_LENGTH);
    }
    PassayUtils.assertNotNullArgOr(
      rules,
      v -> Stream.of(rules).anyMatch(Objects::isNull),
      "Rules cannot be null or contain null");
    this.random = PassayUtils.assertNotNullArg(random, "Random cannot be null");
    this.length = length;

    // characters defined in all AllowedCharacterRules
    final UnicodeString allowedChars = getAllowedCharacters(rules);
    // characters defined in all IllegalCharacterRules
    final UnicodeString illegalChars = getIllegalCharacters(rules);
    // valid characters used to fill the generated password
    this.fillCharacters = getFillCharacters(rules, allowedChars, illegalChars);
    if (this.fillCharacters.isEmpty()) {
      throw new IllegalArgumentException("Rules did not produce any combination of valid characters");
    }

    this.characterAppenders.addAll(getCharacterAppenders(rules, allowedChars, illegalChars, this.random));
    if (this.characterAppenders.isEmpty() ||
        this.characterAppenders.stream().allMatch(c -> c.getCharacters().isEmpty()))
    {
      throw new IllegalArgumentException("Rules did not produce any combination of valid characters");
    }
    this.passwordRules.addAll(rules);
  }


  /**
   * Tracks the number retries. A password generator with a high percentage of retries may be indicative of a password
   * ruleset that needs adjustment.
   *
   * @return Total number of retry attempts.
   */
  public int getRetryCount()
  {
    return retryCount;
  }


  /**
   * Generates a new password of the configured length which meets the requirements of the configured rules.
   *
   * @return  generated password
   */
  public UnicodeString generate()
  {
    final CharBuffer buffer = CharBuffer.allocate(length);
    UnicodeString generated;
    int count = 0;
    do {
      for (CharacterAppender appender : characterAppenders) {
        // for each rule append characters that satisfy the rule
        appender.append(buffer, length);
      }
      // fill any additional characters needed to satisfy the length of the password
      PassayUtils.appendRandomCharacters(fillCharacters, buffer, length - buffer.position(), random);
      // cast to Buffer prevents NoSuchMethodError when compiled on JDK9+ and run on JDK8
      ((Buffer) buffer).flip();
      randomize(buffer);
      generated = new UnicodeString(buffer);
      if (count > 0) {
        retryCount++;
      }
      final DefaultPasswordValidator validator = new DefaultPasswordValidator(passwordRules);
      if (validator.validate(new PasswordData(generated)).isValid()) {
        break;
      } else {
        generated.clear();
      }
    } while (++count <= RETRY_LIMIT);
    if (count > RETRY_LIMIT) {
      generated.clear();
      throw new IllegalStateException("Exceeded maximum number of password generation retries");
    }
    try {
      return generated;
    } finally {
      PassayUtils.clear(buffer);
    }
  }


  /**
   * Returns the fill characters used to populate a generated password.
   *
   * @param  rules  to derive characters from
   * @param  allowedChars  characters allowed
   * @param  illegalChars  characters not allowed
   *
   * @return  fill characters
   */
  protected UnicodeString getFillCharacters(
    final List<? extends Rule> rules, final  UnicodeString allowedChars, final UnicodeString illegalChars)
  {
    final UnicodeString fillChars;
    if (!allowedChars.isEmpty()) {
      if (!illegalChars.isEmpty()) {
        fillChars = allowedChars.difference(illegalChars, true);
      } else {
        fillChars = allowedChars;
      }
    } else {
      // characters defined in all CharacterRules
      final UnicodeString chars = getCharacters(rules);
      if (!chars.isEmpty()) {
        if (!illegalChars.isEmpty()) {
          fillChars = chars.difference(illegalChars, true);
        } else {
          fillChars = chars;
        }
      } else {
        fillChars = new UnicodeString();
      }
    }
    return fillChars;
  }


  /**
   * Returns the list of character appenders used for password generation.
   *
   * @param  rules  to derive password characters
   * @param  allowedChars  characters allowed
   * @param  illegalChars  characters not allowed
   * @param  rand  to randomize character selection
   *
   * @return  character appenders
   */
  protected List<CharacterAppender> getCharacterAppenders(
    final List<? extends Rule> rules,
    final UnicodeString allowedChars,
    final UnicodeString illegalChars,
    final Random rand)
  {
    final List<CharacterAppender> appenders = new ArrayList<>();
    for (Rule rule : rules) {
      if (rule instanceof CharacterRule) {
        appenders.add(new CharacterRuleAppender((CharacterRule) rule, allowedChars, illegalChars, rand));
      } else if (rule instanceof CharacterCharacteristicsRule) {
        appenders.add(
          new CharacterCharacteristicsAppender((CharacterCharacteristicsRule) rule, allowedChars, illegalChars, rand));
      }
    }
    return appenders;
  }


  /**
   * Return a unicode string that contains the unique characters from all {@link CharacterRule} or
   * {@link CharacterCharacteristicsRule} contained in the supplied list.
   *
   * @param  rules  to extract unique characters from
   *
   * @return  unicode string containing unique characters or empty string
   */
  private UnicodeString getCharacters(final List<? extends Rule> rules)
  {
    final List<CharacterRule> characterRules = new ArrayList<>();
    for (Rule rule : rules) {
      if (rule instanceof CharacterRule) {
        characterRules.add((CharacterRule) rule);
      } else if (rule instanceof CharacterCharacteristicsRule) {
        characterRules.addAll(((CharacterCharacteristicsRule) rule).getRules());
      }
    }
    UnicodeString chars = null;
    if (!characterRules.isEmpty()) {
      chars = new UnicodeString(characterRules.get(0).getValidCharacters());
      for (int i = 1; i < characterRules.size(); i++) {
        chars = chars.union(new UnicodeString(characterRules.get(i).getValidCharacters()), true);
      }
    }
    return chars != null ? chars : new UnicodeString();
  }


  /**
   * Returns the unique set of allowed characters as defined in the supplied list of {@link AllowedCharacterRule}. These
   * are the characters that are common to every rule in the list.
   *
   * @param  rules  to extract unique characters from
   *
   * @return  unicode string containing unique characters or empty string
   */
  private UnicodeString getAllowedCharacters(final List<? extends Rule> rules)
  {
    UnicodeString allowedChars = null;
    for (Rule rule : rules) {
      if (rule instanceof AllowedCharacterRule) {
        if (allowedChars == null) {
          allowedChars = ((AllowedCharacterRule) rule).getAllowedCharacters();
        } else {
          allowedChars = allowedChars.intersection(((AllowedCharacterRule) rule).getAllowedCharacters(), true);
        }
      }
    }
    return allowedChars != null ? allowedChars : new UnicodeString();
  }


  /**
   * Returns the unique set of illegal characters as defined in the supplied list of {@link IllegalCharacterRule}.
   *
   * @param  rules  to extract unique characters from
   *
   * @return  unicode string containing unique characters or empty string
   */
  private UnicodeString getIllegalCharacters(final List<? extends Rule> rules)
  {
    UnicodeString illegalChars = null;
    for (Rule rule : rules) {
      if (rule instanceof IllegalCharacterRule) {
        if (illegalChars == null) {
          illegalChars = ((IllegalCharacterRule) rule).getIllegalCharacters();
        } else {
          illegalChars = illegalChars.union(((IllegalCharacterRule) rule).getIllegalCharacters(), true);
        }
      }
    }
    return illegalChars != null ?  illegalChars : new UnicodeString();
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::retryCount=" + retryCount;
  }


  /**
   * Randomizes the contents of the given buffer.
   *
   * @param  buffer  character buffer whose contents will be randomized.
   */
  protected void randomize(final CharBuffer buffer)
  {
    char c;
    int n;
    for (int i = buffer.position(); i < buffer.limit(); i++) {
      n = random.nextInt(buffer.length());
      c = buffer.get(n);
      buffer.put(n, buffer.get(i));
      buffer.put(i, c);
    }
  }


  /** Character appender for {@link CharacterCharacteristicsRule}. */
  public static class CharacterCharacteristicsAppender implements CharacterAppender
  {
    /** Number of characteristics. */
    private final int numberOfCharacteristics;

    /** Character rule appenders. */
    private final List<CharacterRuleAppender> appenders;

    /** To select random characters. */
    private final Random random;


    /**
     * Creates a new character characteristics appender.
     *
     * @param  rule  to build appender from
     * @param  allowedChars  characters allowed in password generation
     * @param  illegalChars  characters not allowed in password generation
     * @param  rand  to randomize character selection
     */
    public CharacterCharacteristicsAppender(
      final CharacterCharacteristicsRule rule,
      final UnicodeString allowedChars,
      final UnicodeString illegalChars,
      final Random rand)
    {
      numberOfCharacteristics = rule.getNumberOfCharacteristics();
      appenders = rule.getRules().stream()
        .map(r -> new CharacterRuleAppender(r, allowedChars, illegalChars, rand))
        .collect(Collectors.toList());
      random = rand;
    }


    @Override
    public UnicodeString getCharacters()
    {
      UnicodeString characters = appenders.get(0).getCharacters();
      for (int i = 1; i < appenders.size(); i++) {
        characters = characters.union(appenders.get(i).getCharacters(), true);
      }
      return characters;
    }


    @Override
    public void append(final CharBuffer target, final int count)
    {
      final List<CharacterRuleAppender> randomAppender = new ArrayList<>(appenders);
      Collections.shuffle(randomAppender, random);
      for (int i = 0; i < numberOfCharacteristics; i++) {
        randomAppender.get(i).append(target, count);
      }
    }
  }


  /** Character appender for {@link CharacterRule}. */
  public static class CharacterRuleAppender implements CharacterAppender
  {
    /** Number of characters. */
    private final int numberOfCharacters;

    /** Valid characters. */
    private final UnicodeString characters;

    /** To select random characters. */
    private final Random random;


    /**
     * Creates a new character rule appender.
     *
     * @param  rule  to build appender from
     * @param  allowedChars  characters allowed for appending
     * @param  illegalChars  characters not allowed for appending
     * @param  rand  to randomize character selection
     */
    public CharacterRuleAppender(
      final CharacterRule rule, final UnicodeString allowedChars, final UnicodeString illegalChars, final Random rand)
    {
      numberOfCharacters = rule.getNumberOfCharacters();
      UnicodeString validChars = new UnicodeString(rule.getValidCharacters());
      if (!allowedChars.isEmpty()) {
        validChars = validChars.intersection(allowedChars, true);
      }
      if (!illegalChars.isEmpty()) {
        validChars = validChars.difference(illegalChars, true);
      }
      characters = validChars;
      random = rand;
    }


    @Override
    public UnicodeString getCharacters()
    {
      return characters;
    }


    @Override
    public void append(final CharBuffer target, final int count)
    {
      PassayUtils.appendRandomCharacters(characters, target, Math.min(count, numberOfCharacters), random);
    }
  }


  /** Interface for appending characters. */
  public interface CharacterAppender
  {


    /**
     * Returns the characters that may be used in by this appender.
     *
     * @return  valid characters
     */
    UnicodeString getCharacters();


    /**
     * Fills the target buffer with count characters from this appender.
     *
     * @param  target  buffer to add characters to
     * @param  count  number of characters to add to buffer
     */
    void append(CharBuffer target, int count);
  }
}

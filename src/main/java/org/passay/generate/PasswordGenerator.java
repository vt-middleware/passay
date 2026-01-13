/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.generate;

import java.nio.Buffer;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;
import org.passay.DefaultPasswordValidator;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.UnicodeString;
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
  private int retryLimit = 2;

  /** Source of random data. */
  private final Random random;

  /** Length of passwords to generate. */
  private final int length;

  /** Rules to determine password character appenders. */
  private final List<Rule> passwordRules = new ArrayList<>();

  /** Character appenders derived from the rules. */
  private final List<CharacterAppender> characterAppenders = new ArrayList<>();

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

    this.characterAppenders.addAll(
      getCharacterAppenders(rules, getAllowedCharacters(rules), getIllegalCharacters(rules), this.random));
    if (this.characterAppenders.isEmpty() ||
        this.characterAppenders.stream().anyMatch(c -> c.getCharacters().isEmpty()))
    {
      throw new IllegalArgumentException("Rules did not produce any combination of valid characters");
    }
    this.passwordRules.addAll(rules);
  }
  
  /**
   * Sets the retry limit for password generation.
   *
   * @param retryLimit maximum number of retries
   */
  public void setRetryLimit(final int retryLimit) {
    this.retryLimit = retryLimit;
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
      characterAppenders.forEach(appender -> appender.append(buffer, length));
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
    } while (++count <= retryLimit);
    if (count > retryLimit) {
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
   * Returns the list of character appenders used for password generation. The last element of this list is guaranteed
   * to be {@link FillRemainingCharactersAppender} to ensure password generation satisfies the requested length.
   *
   * @param  rules  to derive password characters
   * @param  allowedChars  characters allowed
   * @param  illegalChars  characters not allowed
   * @param  rand  to randomize character selection
   *
   * @return  unmodifiable list of character appenders
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
    // the last appender is used to fill any remaining characters to satisfy the length of the password
    appenders.add(new FillRemainingCharactersAppender(rules, allowedChars, illegalChars, rand));
    return Collections.unmodifiableList(appenders);
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
          allowedChars = allowedChars.intersection(((AllowedCharacterRule) rule).getAllowedCharacters());
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
          illegalChars = illegalChars.union(((IllegalCharacterRule) rule).getIllegalCharacters());
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
}

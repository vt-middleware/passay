/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.Buffer;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;
import org.passay.rule.CharacterRule;
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

  /** Tracks the total number of retries. */
  private int retryCount;


  /** Default constructor. Instantiates a secure random for password generation. */
  public PasswordGenerator()
  {
    this(new SecureRandom());
  }


  /**
   * Creates a new password generator with the supplied random.
   *
   * @param  r  random
   */
  public PasswordGenerator(final Random r)
  {
    random = PassayUtils.assertNotNullArg(r, "Random cannot be null");
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
   * See {@link #generatePassword(int, List)}.
   *
   * @param  <T>  type of rule
   * @param  length  of password to generate
   * @param  rules  to generate compliant password from
   *
   * @return  generated password
   */
  @SuppressWarnings("unchecked")
  public <T extends Rule> String generatePassword(final int length, final T... rules)
  {
    PassayUtils.assertNotNullArgOr(
      rules,
      v -> Stream.of(rules).anyMatch(Objects::isNull),
      "Rules cannot be null or contain null");
    return generatePassword(length, Arrays.asList(rules));
  }


  /**
   * Generates a password of the supplied length which meets the requirements of the supplied character rules. For
   * length to be evaluated it must be greater than the number of characters defined in the character rule.
   *
   * @param  length  of password to generate
   * @param  rules  to generate compliant password from
   *
   * @return  generated password
   */
  public String generatePassword(final int length, final List<? extends Rule> rules)
  {
    if (length <= 0) {
      throw new IllegalArgumentException("Length must be greater than 0");
    }
    if (length > MAX_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Length must be less than " + MAX_PASSWORD_LENGTH);
    }
    PassayUtils.assertNotNullArgOr(
      rules,
      v -> v.stream().anyMatch(Objects::isNull),
      "Rules cannot be null or contain null");

    final StringBuilder allChars = new StringBuilder();
    final CharBuffer buffer = CharBuffer.allocate(length);
    String generated;
    int count = 0;

    do {
      for (Rule rule : rules) {
        if (rule instanceof CharacterRule) {
          final CharacterRule characterRule = (CharacterRule) rule;
          fillRandomCharacters(
            characterRule.getValidCharacters(),
            Math.min(length, characterRule.getNumberOfCharacters()),
            buffer);
          if (count == 0) {
            allChars.append(characterRule.getValidCharacters());
          }
        }
      }
      fillRandomCharacters(allChars.toString(), length - buffer.position(), buffer);
      // cast to Buffer prevents NoSuchMethodError when compiled on JDK9+ and run on JDK8
      ((Buffer) buffer).flip();
      randomize(buffer);
      generated = buffer.toString();
      if (count > 0) {
        retryCount++;
      }
      final DefaultPasswordValidator validator = new DefaultPasswordValidator(rules);
      if (validator.validate(new PasswordData(generated)).isValid()) {
        break;
      }
    } while (++count <= RETRY_LIMIT);
    if (count > RETRY_LIMIT) {
      throw new IllegalStateException("Exceeded maximum number of password generation retries");
    }
    return generated;
  }


  /**
   * Fills the supplied target with count random characters from source.
   *
   * @param  source  of random characters.
   * @param  count  number of random characters.
   * @param  target  character sequence that will hold characters.
   */
  protected void fillRandomCharacters(final String source, final int count, final CharBuffer target)
  {
    final int[] indexes = codePointIndexes(source);
    for (int i = 0; i < count; i++) {
      final String s = UnicodeString.toString(source.codePointAt(indexes[random.nextInt(indexes.length)]));
      if (target.position() + s.length() <= target.limit()) {
        target.append(s);
      }
    }
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


  /**
   * Returns the indexes for every code point in the supplied string.
   *
   * @param  s  to find code point indexes
   *
   * @return  array of code point indexes
   */
  private static int[] codePointIndexes(final String s)
  {
    if (s == null || s.isEmpty()) {
      return new int[0];
    }
    final List<Integer> indexes = new ArrayList<>(UnicodeString.charCount(s));
    int i = 0;
    while (i < s.length()) {
      indexes.add(i);
      i += Character.charCount(s.codePointAt(i));
    }
    return indexes.stream().mapToInt(Integer::intValue).toArray();
  }
}

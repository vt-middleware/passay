/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Creates passwords that meet password character rule criteria.
 *
 * @author  Sean C. Sullivan
 * @author  Middleware Services
 */
public class PasswordGenerator
{
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
    random = r;
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
   * @param  length  of password to generate
   * @param  rules  to generate compliant password from
   *
   * @return  generated password
   *
   * @deprecated use {@link #generatePassword(int, Rule[])}
   */
  @Deprecated
  public String generatePassword(final int length, final CharacterRule... rules)
  {
    return generatePassword(length, Arrays.asList(rules));
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
      throw new IllegalArgumentException("length must be greater than 0");
    }

    final StringBuilder allChars = new StringBuilder();
    final CharBuffer buffer = CharBuffer.allocate(length);
    final PasswordValidator validator = new PasswordValidator(rules);
    String generated;
    int count = 0;

    do {
      if (rules != null) {
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
      }
      fillRandomCharacters(allChars.toString(), length - buffer.position(), buffer);
      // cast to Buffer prevents NoSuchMethodError when compiled on JDK9+ and run on JDK8
      ((Buffer) buffer).flip();
      randomize(buffer);
      generated = buffer.toString();
      if (count > 0) {
        retryCount++;
      }
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
  protected void fillRandomCharacters(final String source, final int count, final Appendable target)
  {
    final int[] indexes = PasswordUtils.codePointIndexes(source);
    for (int i = 0; i < count; i++) {
      try {
        target.append(PasswordUtils.toString(source.codePointAt(indexes[random.nextInt(indexes.length)])));
      } catch (IOException e) {
        throw new RuntimeException("Error appending characters.", e);
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
}

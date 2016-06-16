/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.IOException;
import java.nio.CharBuffer;
import java.security.SecureRandom;
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

  /** Source of random data. */
  private final Random random;


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
   * Generates a password of the supplied length which meets the requirements of the supplied character rules. For
   * length to be evaluated it must be greater than the number of characters defined in the character rule.
   *
   * @param  length  of password to generate
   * @param  rules  to generate compliant password from
   *
   * @return  generated password
   */
  public String generatePassword(final int length, final List<CharacterRule> rules)
  {
    if (length <= 0) {
      throw new IllegalArgumentException("length must be greater than 0");
    }

    final StringBuilder allChars = new StringBuilder();

    final CharBuffer buffer = CharBuffer.allocate(length);
    if (rules != null) {
      rules.stream().map((rule) -> {
        fillRandomCharacters(rule.getValidCharacters(), rule.getNumberOfCharacters(), buffer);
        return rule;
      }).forEach((rule) -> {
        allChars.append(rule.getValidCharacters());
      });
    }
    fillRandomCharacters(allChars, length - buffer.position(), buffer);
    buffer.flip();
    randomize(buffer);
    return buffer.toString();
  }


  /**
   * Fills the supplied target with count random characters from source.
   *
   * @param  source  of random characters.
   * @param  count  number of random characters.
   * @param  target  character sequence that will hold characters.
   */
  protected void fillRandomCharacters(final CharSequence source, final int count, final Appendable target)
  {
    for (int i = 0; i < count; i++) {
      try {
        target.append(source.charAt(random.nextInt(source.length())));
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

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.charset.Charset;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 */
public final class PasswordUtils
{

  /** UTF-8 character set. */
  public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");


  /** Default constructor. */
  private PasswordUtils() {}


  /**
   * Returns all the characters in the input string that are also in the
   * pattern.
   *
   * @param  pattern  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(
    final String pattern,
    final String input)
  {
    final StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      final char c = input.charAt(i);
      if (pattern.indexOf(c) != -1) {
        sb.append(c);
      }
    }
    return sb.toString();
  }


  /**
   * Returns a count of all the characters in the input string that are also in
   * the pattern. See {@link #getMatchingCharacters(String, String)}.
   *
   * @param  pattern  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  number of matching characters
   */
  public static int getMatchingCharacterCount(
    final String pattern,
    final String input)
  {
    final String s = getMatchingCharacters(pattern, input);
    return s.length();
  }
}

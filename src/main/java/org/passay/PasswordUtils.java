/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 */
public final class PasswordUtils
{


  /** Default constructor. */
  private PasswordUtils() {}


  /**
   * Returns all the characters in the input string that are also in the
   * pattern.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(
    final String characters,
    final String input)
  {
    final StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      final char c = input.charAt(i);
      if (characters.indexOf(c) != -1) {
        sb.append(c);
      }
    }
    return sb.toString();
  }
}

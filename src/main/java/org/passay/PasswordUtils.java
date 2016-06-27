/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;

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
   * Returns all the characters in the input string that are also in the characters array.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(final String characters, final String input)
  {
    return getMatchingCharacters(characters, input, Integer.MAX_VALUE);
  }


  /**
   * Returns all the characters in the input string that are also in the characters array.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   * @param  maximumLength maximum length of matching characters
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(final String characters, final String input, final int maximumLength)
  {
    final StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      final char c = input.charAt(i);
      if (characters.indexOf(c) != -1) {
        if (sb.length() < maximumLength) {
          sb.append(c);
        } else {
          break;
        }
      }
    }
    return sb.toString();
  }


  /**
   * Concatenates multiple character arrays together.
   *
   * @param  first  array to concatenate. Cannot be null.
   * @param  rest  of the arrays to concatenate. May be null.
   *
   * @return  array containing the concatenation of all parameters
   */
  public static char[] concatArrays(final char[] first, final char[]... rest)
  {
    int totalLength = first.length;
    for (char[] array : rest) {
      if (array != null) {
        totalLength += array.length;
      }
    }

    final char[] result = Arrays.copyOf(first, totalLength);

    int offset = first.length;
    for (char[] array : rest) {
      if (array != null) {
        System.arraycopy(array, 0, result, offset, array.length);
        offset += array.length;
      }
    }
    return result;
  }
}

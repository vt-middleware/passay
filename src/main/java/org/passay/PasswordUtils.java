/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
   * Returns all the characters in the input string that are also in the characters string.
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
    int i = 0;
    while (i < input.length()) {
      final int cp = input.codePointAt(i);
      if (characters.indexOf(cp) != -1) {
        if (sb.length() < maximumLength) {
          sb.append(toString(cp));
        } else {
          break;
        }
      }
      i += Character.charCount(cp);
    }
    return sb.toString();
  }


  /**
   * Returns the number of characters in the supplied input that existing from the supplied characters string.
   *
   * @param  codePoints  code points of characters to match
   * @param  input  to search for matches
   *
   * @return  character count
   */
  public static int countMatchingCharacters(final int[] codePoints, final String input)
  {
    return (int) input.codePoints().filter(x -> IntStream.of(codePoints).anyMatch(y -> y == x)).count();
  }


  /**
   * Returns the number of characters in the supplied input that existing from the supplied characters string.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  character count
   */
  public static int countMatchingCharacters(final String characters, final String input)
  {
    return (int) input.codePoints().filter(x -> characters.indexOf(x) != -1).count();
  }


  /**
   * Returns the number of code points in the supplied string.
   *
   * @param  s  to count characters
   *
   * @return  number of code points
   */
  public static int charCount(final String s)
  {
    return s != null ? s.codePointCount(0, s.length()) : 0;
  }


  /**
   * Creates a new string from the supplied code point.
   *
   * @param  codePoint  to create string with
   *
   * @return  new string
   */
  public static String toString(final int codePoint)
  {
    return toString(new int[] {codePoint});
  }


  /**
   * Creates a new string from the supplied code points.
   *
   * @param  codePoints  to create string with
   *
   * @return  new string
   */
  public static String toString(final int[] codePoints)
  {
    return new String(codePoints, 0, codePoints.length);
  }


  /**
   * Returns the indexes for every code point in the supplied string.
   *
   * @param  s  to find code point indexes
   *
   * @return  array of code point indexes
   */
  public static int[] codePointIndexes(final String s)
  {
    if (s == null || s.isEmpty()) {
      return new int[0];
    }
    final List<Integer> indexes = new ArrayList<>(PasswordUtils.charCount(s));
    int i = 0;
    while (i < s.length()) {
      indexes.add(i);
      i += Character.charCount(s.codePointAt(i));
    }
    return indexes.stream().mapToInt(Integer::intValue).toArray();
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

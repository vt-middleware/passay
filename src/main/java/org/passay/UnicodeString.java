/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Contains a list of character code points.
 *
 * @author  Middleware Services
 */
public final class UnicodeString
{

  /** Stores the character code points. */
  private final int[] codePoints;


  /**
   * Creates a new code points.
   *
   * @param  chars  characters
   */
  public UnicodeString(final char... chars)
  {
    this(new String(chars));
  }


  /**
   * Creates a new code points.
   *
   * @param  string  characters
   */
  public UnicodeString(final String string)
  {
    PassayUtils.assertNotNullArgOr(string, String::isEmpty, "String cannot be null or have a length of zero");
    codePoints = string.codePoints().toArray();
  }


  /**
   * Creates a new code points.
   *
   * @param  codePoints  character code points
   */
  public UnicodeString(final int... codePoints)
  {
    PassayUtils.assertNotNullArgOr(
      codePoints,
      v -> v.length == 0,
      "Code points cannot be null or have a length of zero");
    this.codePoints = Arrays.copyOf(codePoints, codePoints.length);
  }


  /**
   * Returns the code points.
   *
   * @return  code points
   */
  public int[] getCodePoints()
  {
    return Arrays.copyOf(codePoints, codePoints.length);
  }


  /**
   * Returns the number of code points.
   *
   * @return  number of code points
   */
  public int length()
  {
    return codePoints.length;
  }


  /**
   * Returns a new string containing the code points. See {@link #toString(int[])}.
   *
   * @return  new string
   */
  public String toString()
  {
    return toString(codePoints);
  }


  /**
   * Returns the number of code points in the supplied string.
   *
   * @param  string  to count characters
   *
   * @return  number of code points
   */
  public static int charCount(final String string)
  {
    return string != null ? string.codePointCount(0, string.length()) : 0;
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
}

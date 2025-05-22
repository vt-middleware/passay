/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Contains a list of character code points.
 *
 * @author  Middleware Services
 */
public final class CodePoints
{

  /** Stores the character code points. */
  private final int[] codePoints;


  /**
   * Creates a new code points.
   *
   * @param  c  characters
   */
  public CodePoints(final char... c)
  {
    this(new String(c));
  }


  /**
   * Creates a new code points.
   *
   * @param  s  characters
   */
  public CodePoints(final String s)
  {
    if (s == null || s.isEmpty()) {
      throw new IllegalArgumentException("String cannot be null or have a length of zero");
    }
    codePoints = s.codePoints().toArray();
  }


  /**
   * Creates a new code points.
   *
   * @param  cp  character code points
   */
  public CodePoints(final int... cp)
  {
    if (cp == null || cp.length == 0) {
      throw new IllegalArgumentException("Code points cannot be null or have a length of zero");
    }
    codePoints = Arrays.copyOf(cp, cp.length);
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

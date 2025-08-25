/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Contains a list of character code points.
 *
 * @author  Middleware Services
 */
public final class UnicodeString implements CharSequence
{

  /** Stores the character code points. */
  private final int[] codePoints;

  /** Stores the characters. Used to implement CharSequence API. */
  private final char[] characters;


  /**
   * Internal copy constructor.
   *
   * @param  codePoints  string code points
   * @param  characters  string characters
   */
  private UnicodeString(final int[] codePoints, final char[] characters)
  {
    this.codePoints = Arrays.copyOf(codePoints, codePoints.length);
    this.characters = Arrays.copyOf(characters, characters.length);
  }


  /**
   * Creates a new unicode string.
   *
   * @param  chars  characters
   */
  public UnicodeString(final char... chars)
  {
    PassayUtils.assertNotNullArg(chars, "Chars cannot be null");
    this.codePoints = new int[Character.codePointCount(chars, 0, chars.length)];
    int i = 0;
    int codePointsIndex = 0;
    while (i < chars.length) {
      final int cp = Character.codePointAt(chars, i);
      this.codePoints[codePointsIndex++] = cp;
      i += Character.charCount(cp);
    }
    this.characters = Arrays.copyOf(chars, chars.length);
  }


  /**
   * Creates a new unicode string.
   *
   * @param  cs  character sequence
   */
  public UnicodeString(final CharSequence cs)
  {
    PassayUtils.assertNotNullArg(cs, "Char sequence cannot be null");
    this.codePoints = cs.codePoints().toArray();
    final int[] chars = cs.chars().toArray();
    this.characters = new char[chars.length];
    for (int i = 0; i < chars.length; i++) {
      this.characters[i] = (char) chars[i];
    }
  }


  /**
   * Creates a new unicode string.
   *
   * @param  codePoints  character code points
   */
  public UnicodeString(final int... codePoints)
  {
    PassayUtils.assertNotNullArg(codePoints, "Code points cannot be null");
    for (int cp : codePoints) {
      if (!Character.isValidCodePoint(cp)) {
        throw new IllegalArgumentException("Invalid code point: " + cp);
      }
    }
    this.codePoints = Arrays.copyOf(codePoints, codePoints.length);
    this.characters = new char[IntStream.of(codePoints).map(Character::charCount).sum()];
    int cpIndex = 0;
    int charIndex = 0;
    while (cpIndex < codePoints.length) {
      if (Character.isSupplementaryCodePoint(codePoints[cpIndex])) {
        this.characters[charIndex++] = Character.highSurrogate(codePoints[cpIndex]);
        this.characters[charIndex++] = Character.lowSurrogate(codePoints[cpIndex++]);
      } else {
        this.characters[charIndex++] = (char) codePoints[cpIndex++];
      }
    }
  }


  /**
   * Returns the code point at the supplied index.
   *
   * @param  index  of the code point
   *
   * @return  code point
   */
  public int codePointAt(final int index)
  {
    if (index < 0) {
      throw new ArrayIndexOutOfBoundsException("Index " + index + " must be greater than or equal to zero");
    }
    if (index >= codePoints.length) {
      throw new ArrayIndexOutOfBoundsException("Index " + index + " must be less than " + codePoints.length);
    }
    return codePoints[index];
  }


  @Override
  public char charAt(final int index)
  {
    if (index < 0) {
      throw new ArrayIndexOutOfBoundsException("Index " + index + " must be greater than or equal to zero");
    }
    if (index >= characters.length) {
      throw new ArrayIndexOutOfBoundsException("Index " + index + " must be less than " + characters.length);
    }
    return characters[index];
  }


  /**
   * Returns the array of code points in this unicode string.
   *
   * @return  code points array
   */
  public int[] toCodePointArray()
  {
    return Arrays.copyOf(codePoints, codePoints.length);
  }


  /**
   * Returns the array of characters in this unicode string.
   *
   * @return  character array
   */
  public char[] toCharArray()
  {
    return Arrays.copyOf(characters, characters.length);
  }


  /**
   * Returns a new unicode string with each character lower cased. See {@link Character#toLowerCase(int)}.
   *
   * @return  lower cased unicode string
   */
  public UnicodeString toLowerCase()
  {
    return toLowerCase(false);
  }


  /**
   * Returns a new unicode string with each character lower cased. See {@link Character#toLowerCase(int)}.
   *
   * @param  clear  whether to invoke {@link #clear()} at the conclusion of this method
   *
   * @return  lower cased unicode string
   */
  public UnicodeString toLowerCase(final boolean clear)
  {
    try {
      return new UnicodeString(IntStream.of(codePoints).map(Character::toLowerCase).toArray());
    } finally {
      if (clear) {
        clear();
      }
    }
  }


  /**
   * Returns a new unicode string with each character upper cased. See {@link Character#toUpperCase(int)}.
   *
   * @return  upper cased unicode string
   */
  public UnicodeString toUpperCase()
  {
    return toUpperCase(false);
  }


  /**
   * Returns a new unicode string with each character upper cased. See {@link Character#toUpperCase(int)}.
   *
   * @param  clear  whether to invoke {@link #clear()} at the conclusion of this method
   *
   * @return  upper cased unicode string
   */
  public UnicodeString toUpperCase(final boolean clear)
  {
    try {
      return new UnicodeString(IntStream.of(codePoints).map(Character::toUpperCase).toArray());
    } finally {
      if (clear) {
        clear();
      }
    }
  }


  /**
   * Returns the number of code points.
   *
   * @return  number of code points
   */
  public int codePointCount()
  {
    return codePoints.length;
  }


  @Override
  public int length()
  {
    return characters.length;
  }


  @Override
  public UnicodeString subSequence(final int beginIndex, final int endIndex)
  {
    return substring(beginIndex, endIndex);
  }


  /**
   * Returns whether this unicode string has any code points.
   *
   * @return  whether this unicode string has any code points
   */
  public boolean isEmpty()
  {
    return codePoints.length == 0;
  }


  /**
   * Returns a new unicode string containing code points from the supplied begin index.
   *
   * @param  beginIndex  of the code point that would be the first code point in the new unicode string
   *
   * @return  new unicode substring
   */
  public UnicodeString substring(final int beginIndex)
  {
    return substring(beginIndex, codePoints.length);
  }


  /**
   * Returns a new unicode string containing code points from the supplied begin index (inclusive) to the the supplied
   * end index (exclusive).
   *
   * @param  beginIndex  of the code point that would be the first code point in the new unicode string
   * @param  endIndex  of the code point that would be the last code point in the new unicode string
   *
   * @return  new unicode substring
   */
  public UnicodeString substring(final int beginIndex, final int endIndex)
  {
    if (beginIndex < 0) {
      throw new IllegalArgumentException("Begin index cannot be negative");
    }
    if (beginIndex > endIndex) {
      throw new IllegalArgumentException("Begin index cannot be greater than end index");
    }
    if (endIndex > codePoints.length) {
      throw new IllegalArgumentException("End index cannot be greater than the number of code points");
    }
    if (beginIndex == 0 && endIndex == codePoints.length) {
      return new UnicodeString(codePoints);
    }
    final int length = endIndex - beginIndex;
    if (length == 0) {
      return new UnicodeString();
    }
    return new UnicodeString(Arrays.copyOfRange(codePoints, beginIndex, beginIndex + length));
  }


  /**
   * Returns a new unicode string with the code points reversed.
   *
   * @return  reversed unicode string
   */
  public UnicodeString reverse()
  {
    return reverse(false);
  }


  /**
   * Returns a new unicode string with the code points reversed.
   *
   * @param  clear  whether to invoke {@link #clear()} at the conclusion of this method
   *
   * @return  reversed unicode string
   */
  public UnicodeString reverse(final boolean clear)
  {
    try {
      final int[] reversedCodePoints = new int[codePoints.length];
      for (int i = 0; i < codePoints.length; i++) {
        reversedCodePoints[i] = codePoints[codePoints.length - i - 1];
      }
      return new UnicodeString(reversedCodePoints);
    } finally {
      if (clear) {
        clear();
      }
    }
  }


  /**
   * Writes zeros to the underlying code points array.
   */
  public void clear()
  {
    PassayUtils.clear(codePoints);
    PassayUtils.clear(characters);
  }


  /**
   * Returns the number of code points in the supplied input that exist in this unicode string.
   *
   * @param  input  code points to match
   *
   * @return  code point count
   */
  public int countMatchingCodePoints(final int[] input)
  {
    return (int) IntStream.of(codePoints).filter(x -> IntStream.of(input).anyMatch(y -> y == x)).count();
  }


  /**
   * Returns whether this unicode string starts with the supplied prefix.
   *
   * @param  prefix  to compare with this unicode string
   *
   * @return  whether this unicode string starts with prefix
   */
  public boolean startsWith(final UnicodeString prefix)
  {
    return startsWith(prefix, 0);
  }


  /**
   * Returns whether this unicode string starts with the supplied prefix at the supplied offset.
   *
   * @param  prefix  to compare with this unicode string
   * @param  offset  code point index at which to compare the prefix
   *
   * @return  whether this unicode string starts with prefix at the offset
   */
  boolean startsWith(final UnicodeString prefix, final int offset)
  {
    final int[] prefixCodePoints = prefix.codePoints;
    int i = 0;
    int j = offset;
    while (i < prefixCodePoints.length) {
      if (prefixCodePoints[i++] != codePoints[j++]) {
        return false;
      }
    }
    return true;
  }


  /**
   * Returns whether this unicode string ends with the supplied suffix.
   *
   * @param  suffix  to compare with this unicode string
   *
   * @return  whether this unicode string ends with suffix
   */
  public boolean endsWith(final UnicodeString suffix)
  {
    return startsWith(suffix, codePoints.length - suffix.codePointCount());
  }


  /**
   * Returns whether this unicode string contains the supplied string.
   *
   * @param  other  to compare with this unicode string
   *
   * @return  whether this unicode string contains other
   */
  public boolean contains(final UnicodeString other)
  {
    return indexOf(other) >= 0;
  }


  /**
   * Returns code point index in this unicode string of the supplied string.
   *
   * @param  other  to search this unicode string
   *
   * @return  the code point index of other in this unicdoe string
   */
  // CheckStyle:ReturnCount OFF
  int indexOf(final UnicodeString other)
  {
    final int[] otherCodePoints = other.codePoints;
    final int max = codePoints.length - otherCodePoints.length;
    for (int i = 0; i <= max; i++) {
      // search for the first code point
      if (codePoints[i] != otherCodePoints[0]) {
        do {
          i++;
        } while (i <= max && codePoints[i] != otherCodePoints[0]);
      }
      if (i <= max) {
        // first code point matched, iterate through the remaining code points
        int j = i + 1;
        final int end = j + otherCodePoints.length - 1;
        int k = 1;
        while (j < end && codePoints[j] == otherCodePoints[k]) {
          j++;
          k++;
        }
        if (j == end) {
          // all code points matched
          return i;
        }
      }
    }
    // all code points did not match
    return -1;
  }
  // CheckStyle:ReturnCount ON


  @Override
  public String toString()
  {
    return new String(codePoints, 0, codePoints.length);
  }


  @Override
  public boolean equals(final Object o)
  {
    if (o == this) {
      return true;
    }
    if (o instanceof UnicodeString) {
      final UnicodeString v = (UnicodeString) o;
      return Arrays.equals(codePoints, v.codePoints);
    }
    return false;
  }


  @Override
  public int hashCode()
  {
    // CheckStyle:MagicNumber OFF
    return 31 * Objects.hashCode(codePoints);
    // CheckStyle:MagicNumber ON
  }


  /**
   * Creates a copy of the supplied unicode string.
   *
   * @param  string  to copy
   *
   * @return  new unicode string
   */
  public static UnicodeString copy(final UnicodeString string)
  {
    return new UnicodeString(string.codePoints, string.characters);
  }
}

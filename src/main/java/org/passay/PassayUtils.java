/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 */
public final class PassayUtils
{


  /** Default constructor. */
  private PassayUtils() {}


  /**
   * Throws {@link IllegalArgumentException} if the supplied object is null.
   *
   * @param  <T>  type of object
   * @param  o to check
   * @param  msg to include in the exception
   *
   * @return  supplied object
   */
  public static <T> T assertNotNullArg(final T o, final String msg)
  {
    if (o == null) {
      throw new IllegalArgumentException(msg);
    }
    return o;
  }


  /**
   * Throws {@link IllegalArgumentException} if the supplied object is null or the supplied predicate returns true.
   *
   * @param  <T>  type of object
   * @param  o to check
   * @param  predicate  to test
   * @param  msg to include in the exception
   *
   * @return  supplied object
   */
  public static <T> T assertNotNullArgOr(final T o, final Predicate<T> predicate, final String msg)
  {
    try {
      if (o == null || predicate.test(o)) {
        throw new IllegalArgumentException(msg);
      }
    } catch (Exception e) {
      // treat a predicate exception as an illegal argument
      throw new IllegalArgumentException(e);
    }
    return o;
  }


  /**
   * Returns the number of code points in the supplied string.
   *
   * @param  string  to count characters
   *
   * @return  number of code points
   */
  public static int codePointCount(final String string)
  {
    return string != null && !string.isEmpty() ? string.codePointCount(0, string.length()) : 0;
  }


  /**
   * Creates a new string from the supplied code points.
   *
   * @param  codePoints  to create string with
   *
   * @return  new string
   */
  public static String toString(final int... codePoints)
  {
    return new String(codePoints, 0, codePoints.length);
  }


  /**
   * Converts the supplied text to a byte array using the supplied charset.
   *
   * @param  text  to convert to a byte array
   * @param  charset  to use for the conversion
   *
   * @return  encoded byte array
   */
  public static byte[] toByteArray(final CharSequence text, final Charset charset)
  {
    final ByteBuffer buffer = toByteBuffer(text, charset);
    try {
      final byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      return bytes;
    } finally {
      clear(buffer);
    }
  }


  /**
   * Converts the supplied text to a byte buffer using the supplied charset.
   *
   * @param  text  to convert to a byte array
   * @param  charset  to use for the conversion
   *
   * @return  encoded byte buffer
   */
  private static ByteBuffer toByteBuffer(final CharSequence text, final Charset charset)
  {
    final char[] chars = new char[text.length()];
    for (int i = 0; i < chars.length; i++) {
      chars[i] = text.charAt(i);
    }
    final CharBuffer buffer = CharBuffer.wrap(chars);
    try {
      return charset.encode(buffer);
    } finally {
      clear(buffer);
    }
  }


  /**
   * Clears the supplied object by writing zeros to its data structure. The following types are supported:
   * <ul>
   *   <li>char[]</li>
   *   <li>byte[]</li>
   *   <li>int[]</li>
   *   <li>long[]</li>
   *   <li>CharBuffer</li>
   *   <li>ByteBuffer</li>
   *   <li>IntBuffer</li>
   *   <li>LongBuffer</li>
   * </ul>
   *
   * @param  o  to clear
   *
   * @throws  IllegalArgumentException  if the supplied object cannot be cleared
   */
  public static void clear(final Object o)
  {
    if (o instanceof char[]) {
      Arrays.fill((char[]) o, '\u0000');
    } else if (o instanceof byte[]) {
      Arrays.fill((byte[]) o, (byte) 0);
    } else if (o instanceof int[]) {
      Arrays.fill((int[]) o, 0);
    } else if (o instanceof long[]) {
      Arrays.fill((long[]) o, 0L);
    } else if (o instanceof Buffer) {
      ((Buffer) o).rewind();
      while (((Buffer) o).hasRemaining()) {
        if (o instanceof CharBuffer) {
          ((CharBuffer) o).put((char) 0);
        } else if (o instanceof ByteBuffer) {
          ((ByteBuffer) o).put((byte) 0);
        } else if (o instanceof IntBuffer) {
          ((IntBuffer) o).put(0);
        } else if (o instanceof LongBuffer) {
          ((LongBuffer) o).put(0L);
        } else {
          throw new IllegalArgumentException("Unsupported buffer type: " + o.getClass().getName());
        }
      }
      ((Buffer) o).flip();
    } else {
      throw new IllegalArgumentException("Unsupported type: " + o.getClass().getName());
    }
  }


  /**
   * Appends characters to the supplied target with count random characters from source.
   *
   * @param  source  of random characters.
   * @param  target  character sequence that will hold characters.
   * @param  count  number of random characters.
   * @param  rand  to select characters from source.
   */
  public static void appendRandomCharacters(
    final UnicodeString source, final CharBuffer target, final int count, final Random rand)
  {
    if (source == null || source.isEmpty()) {
      return;
    }
    for (int i = 0; i < count; i++) {
      final String s = PassayUtils.toString(source.codePointAt(rand.nextInt(source.length())));
      if (target.position() + s.length() <= target.limit()) {
        target.append(s);
      }
    }
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Provides an implementation of a {@link WordList} that is backed by a file and leverages a {@link MappedByteBuffer}.
 * Each word is read from the file for every get, though the implementation supports a simple memory cache to improve
 * read performance. This implementation does not support files greater than 2GB in size. Use this implementation when
 * the initialization cost of {@link FileWordList} is too high.
 *
 * @author  Middleware Services
 */
public class MemoryMappedFileWordList extends AbstractFileWordList
{

  /** mapped byte buffer. */
  protected final MappedByteBuffer buffer;



  /**
   * Creates a new case-sensitive word list from the supplied file. The input file is read on initialization and is
   * maintained by this class.
   *
   * <p><strong>NOTE</strong> Attempts to close the source file will cause {@link IOException} when {@link #get(int)} is
   * called subsequently.</p>
   *
   * @param  raf  File containing words, one per line.
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public MemoryMappedFileWordList(final RandomAccessFile raf)
    throws IOException
  {
    this(raf, true);
  }


  /**
   * Creates a new word list from the supplied file. The input file is read on initialization and is maintained by this
   * class.
   *
   * <p><strong>NOTE</strong> Attempts to close the source file will cause {@link IOException} when {@link #get(int)} is
   * called subsequently.</p>
   *
   * @param  raf  File containing words, one per line.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public MemoryMappedFileWordList(final RandomAccessFile raf, final boolean caseSensitive)
    throws IOException
  {
    this(raf, caseSensitive, DEFAULT_CACHE_SIZE);
  }


  /**
   * Creates a new word list from the supplied file. The input file is read on initialization and is maintained by this
   * class. cachePercent is a percentage of the file size in bytes.
   *
   * <p><strong>NOTE</strong> Attempts to close the source file will cause {@link IOException} when {@link #get(int)} is
   * called subsequently.</p>
   *
   * @param  raf  File containing words, one per line.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   * @param  cachePercent  Percent (0-100) of file to cache in memory for improved read performance.
   *
   * @throws  IllegalArgumentException  if cache percent is out of range.
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public MemoryMappedFileWordList(final RandomAccessFile raf, final boolean caseSensitive, final int cachePercent)
    throws IOException
  {
    super(raf, caseSensitive, cachePercent);
    final FileChannel channel = file.getChannel();
    buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

    final int fileBytes = buffer.capacity();
    final int cacheSize = (fileBytes / HUNDRED_PERCENT) * cachePercent;
    final int cacheOffset = cacheSize == 0 ? fileBytes : cacheSize > fileBytes ? 1 : fileBytes / cacheSize;

    long pos = 0;
    String a;
    String b = null;
    while ((a = readLine(buffer)) != null) {
      if (b != null && comparator.compare(a, b) < 0) {
        throw new IllegalArgumentException("File is not sorted correctly for this comparator");
      }
      b = a;

      if (cacheSize > 0 && size % cacheOffset == 0) {
        cache.put(size, pos);
      }
      pos = buffer.position();
      size++;
    }
  }


  /**
   * Reads the file line by line and returns the word at the supplied index. Returns null if the index cannot be read.
   * This method leverages the cache to seek to the closest position of the supplied index.
   *
   * @param  index  to read word at
   *
   * @return  word at the supplied index
   *
   * @throws  IllegalStateException  if an error occurs reading the supplied file
   */
  @Override
  protected String readFile(final int index)
  {
    synchronized (buffer) {
      int i = 0;
      if (!cache.isEmpty() && cache.firstKey() <= index) {
        i = cache.floorKey(index);
      }

      final int pos = i > 0 ? cache.get(i).intValue() : 0;
      buffer.position(pos);

      String s;
      while (buffer.hasRemaining()) {
        s = readLine(buffer);
        if (i == index) {
          return s;
        }
        i++;
      }
    }
    return null;
  }


  /**
   * Reads a line from the supplied buffer. Buffer position will be set to the beginning of the next line. Line
   * terminator must be one of '\n', '\r', or "\r\n".
   *
   * @param  buffer  to read from
   *
   * @return  file line or null if end of file has been reached
   */
  private static String readLine(final MappedByteBuffer buffer)
  {
    final StringBuilder line = new StringBuilder();
    while (buffer.hasRemaining()) {
      final char c = (char) buffer.get();
      if (c == '\n' || c == '\r') {
        // Ignore leading line termination characters
        if (line.length() == 0) {
          continue;
        }
        break;
      }
      line.append(c);
    }
    return line.length() > 0 ? line.toString() : null;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Provides an implementation of a {@link WordList} that is backed by a file. Each word is read from the file for every
 * get, though the implementation supports a simple memory cache to improve read performance. This implementation should
 * be avoided for files greater than 100MB in size. Due to the inefficiencies in {@link RandomAccessFile#readLine()},
 * expect a 100MB file to require approximately 60 seconds to be read during initialization. Once this class is
 * initialized, reads are relatively fast since a {@link BufferedReader} is used.
 *
 * @author  Middleware Services
 */
public class FileWordList extends AbstractFileWordList
{


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
  public FileWordList(final RandomAccessFile raf)
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
  public FileWordList(final RandomAccessFile raf, final boolean caseSensitive)
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
  public FileWordList(final RandomAccessFile raf, final boolean caseSensitive, final int cachePercent)
    throws IOException
  {
    super(raf, caseSensitive, cachePercent);
    long pos = 0;
    file.seek(pos);

    final long fileBytes = file.length();
    final long cacheSize = (fileBytes / 100) * cachePercent;
    final long cacheOffset = cacheSize == 0 ? fileBytes : cacheSize > fileBytes ? 1 : fileBytes / cacheSize;

    String a;
    String b = null;
    while ((a = file.readLine()) != null) {
      if (b != null && comparator.compare(a, b) < 0) {
        throw new IllegalArgumentException("File is not sorted correctly for this comparator");
      }
      b = a;

      if (cacheSize > 0 && size % cacheOffset == 0) {
        cache.put(size, pos);
      }
      pos = file.getFilePointer();
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
    try {
      synchronized (file) {
        int i = 0;
        if (!cache.isEmpty() && cache.firstKey() <= index) {
          i = cache.floorKey(index);
        }

        final long pos = i > 0 ? cache.get(i) : 0L;
        file.seek(pos);

        String s;
        final BufferedReader reader = new BufferedReader(new FileReader(file.getFD()));
        while ((s = reader.readLine()) != null) {
          if (i == index) {
            return s;
          }
          i++;
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Error reading file", e);
    }
    return null;
  }
}

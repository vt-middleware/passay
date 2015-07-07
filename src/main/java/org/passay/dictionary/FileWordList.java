/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

/**
 * Provides an implementation of a {@link WordList} that is backed by a file. Each word is read from the file for every
 * get, though the implementation supports a simple memory cache to improve read performance.
 *
 * @author  Middleware Services
 */
public class FileWordList extends AbstractWordList
{

  /** default cache size. */
  public static final int DEFAULT_CACHE_SIZE = 5;

  /** 100 percent. */
  private static final int HUNDRED_PERCENT = 100;

  /** file containing words. */
  protected final RandomAccessFile file;

  /** size of the file. */
  protected int size;

  /** cache of indexes to file positions. */
  // CheckStyle:IllegalType OFF
  // uses the firstKey and floorKey implementations
  protected TreeMap<Integer, Long> cache = new TreeMap<>();
  // CheckStyle:IllegalType ON


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
   * class.
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
    if (cachePercent < 0 || cachePercent > HUNDRED_PERCENT) {
      throw new IllegalArgumentException("cachePercent must be between 0 and 100 inclusive");
    }
    file = raf;
    if (caseSensitive) {
      comparator = WordLists.CASE_SENSITIVE_COMPARATOR;
    } else {
      comparator = WordLists.CASE_INSENSITIVE_COMPARATOR;
    }
    synchronized (file) {
      file.seek(0L);

      String a;
      String b = null;
      while ((a = file.readLine()) != null) {
        if (b != null && comparator.compare(a, b) < 0) {
          throw new IllegalArgumentException("File is not sorted correctly for this comparator");
        }
        b = a;
        size++;
      }
      intializeCache(cachePercent * size / HUNDRED_PERCENT);
    }
  }


  /**
   * Reads the underlying file to cache the supplied percentage of line positions.
   *
   * @param  cacheSize  Number of entries in cache.
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  private void intializeCache(final int cacheSize)
    throws IOException
  {
    if (cacheSize > 0) {
      final int offset = cacheSize > size ? 1 : size / cacheSize;
      long pos = 0L;
      file.seek(pos);
      for (int i = 0; i < size; i++) {
        file.readLine();
        if (i != 0 && i % offset == 0) {
          cache.put(i, pos);
        }
        pos = file.getFilePointer();
      }
    }
  }


  @Override
  public String get(final int index)
  {
    checkRange(index);
    return readFile(index);
  }


  @Override
  public int size()
  {
    return size;
  }


  /**
   * Returns the file backing this list.
   *
   * @return  random access file that is backing this list
   */
  public RandomAccessFile getFile()
  {
    return file;
  }


  /**
   * Closes the underlying file and make the cache available for garbage collection.
   *
   * @throws  IOException  if an error occurs closing the file
   */
  public void close()
    throws IOException
  {
    synchronized (file) {
      file.close();
    }
    cache = null;
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
  private String readFile(final int index)
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
        while ((s = file.readLine()) != null) {
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

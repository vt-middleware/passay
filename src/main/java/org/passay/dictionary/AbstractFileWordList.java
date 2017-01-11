/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

/**
 * Common implementation for file based word lists.
 *
 * @author  Middleware Services
 */
public abstract class AbstractFileWordList extends AbstractWordList
{

  /** default cache size. */
  public static final int DEFAULT_CACHE_SIZE = 5;

  /** file containing words. */
  protected final RandomAccessFile file;

  /** number of lines in the file. */
  protected int size;

  /** cache of indexes to file positions. */
  // CheckStyle:IllegalType OFF
  // uses the firstKey and floorKey implementations
  protected TreeMap<Integer, Long> cache = new TreeMap<>();
  // CheckStyle:IllegalType ON


  /**
   * Creates a new abstract file word list from the supplied file.
   *
   * @param  raf  File containing words, one per line.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   * @param  cachePercent  Percent (0-100) of file to cache in memory for improved read performance.
   *
   * @throws  IllegalArgumentException  if cache percent is out of range.
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public AbstractFileWordList(final RandomAccessFile raf, final boolean caseSensitive, final int cachePercent)
    throws IOException
  {
    if (cachePercent < 0 || cachePercent > 100) {
      throw new IllegalArgumentException("cachePercent must be between 0 and 100 inclusive");
    }
    file = raf;
    if (caseSensitive) {
      comparator = WordLists.CASE_SENSITIVE_COMPARATOR;
    } else {
      comparator = WordLists.CASE_INSENSITIVE_COMPARATOR;
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
   * Reads the file line by line and returns the word at the supplied index.
   *
   * @param  index  to read word at
   *
   * @return  word at the supplied index
   *
   * @throws  IllegalStateException  if an error occurs reading the supplied file
   */
  protected abstract String readFile(final int index);
}

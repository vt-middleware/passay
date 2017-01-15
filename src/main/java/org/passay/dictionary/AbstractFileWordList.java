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

  /** number of words in the file. */
  protected int size;

  /** cache of indexes to file positions. */
  // CheckStyle:IllegalType OFF
  // uses the firstKey and floorKey implementations
  protected TreeMap<Integer, Long> cache = new TreeMap<>();
  // CheckStyle:IllegalType ON

  /** Synchronization lock. */
  private final Object lock = new Object();


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
    try {
      return readWord(index);
    } catch (IOException e) {
      throw new RuntimeException("Error reading from file backing word list", e);
    }
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
   * Reads words from the backing file to initialize the word list.
   *
   * @param  cachePercent  Percent of file in bytes to use for cache.
   *
   * @throws  IOException  on I/O errors reading file data.
   */
  protected void initialize(final int cachePercent) throws IOException
  {
    final long fileBytes = file.length();
    final long cacheSize = (fileBytes / 100) * cachePercent;
    final long cacheModulus = cacheSize == 0 ? fileBytes : cacheSize > fileBytes ? 1 : fileBytes / cacheSize;

    FileWord a;
    FileWord b = null;
    synchronized (lock) {
      seek(0);
      while ((a = nextWord()) != null) {
        if (b != null && comparator.compare(a.word, b.word) < 0) {
          throw new IllegalArgumentException("File is not sorted correctly for this comparator");
        }
        b = a;
        if (cacheSize > 0 && size % cacheModulus == 0) {
          cache.put(size, a.offset);
        }
        size++;
      }
    }
  }


  /**
   * Reads the word from the file at the given index of the word list.
   *
   * @param  index  ith word in the word list
   *
   * @return  word at the supplied index
   *
   * @throws  IOException  on I/O errors
   */
  protected String readWord(final int index) throws IOException
  {
    int i = 0;
    if (!cache.isEmpty() && cache.firstKey() <= index) {
      i = cache.floorKey(index);
    }
    final long pos = i > 0 ? cache.get(i) : 0L;
    FileWord w;
    synchronized (lock) {
      seek(pos);
      do {
        w = nextWord();
      } while (i++ < index && w != null);
      return w != null ? w.word : null;
    }
  }


  /**
   * Positions the read head of the backing file at the given byte offset.
   *
   * @param offset byte offset into file.
   *
   * @throws  IOException  on I/O errors seeking.
   */
  protected abstract void seek(long offset) throws IOException;


  /**
   * Reads the next word from the current position in the backing file.
   *
   * @return  Data structure containing word and byte offset into file where word begins.

   * @throws  IOException  on I/O errors reading file data.
   */
  protected abstract FileWord nextWord() throws IOException;


  /**
   * Data structure containing word and byte offset into file where word begins in backing file.
   */
  static class FileWord
  {

    // CheckStyle:VisibilityModifier OFF
    /** Word read from backing file. */
    String word;

    /** Byte offset into file where word begins. */
    long offset;
    // CheckStyle:VisibilityModifier OFF


    /**
     * Creates a new instance with a word and offset.
     *
     * @param  s  word.
     * @param  position  byte offset into file.
     */
    FileWord(final String s, final long position)
    {
      word = s;
      offset = position;
    }
  }
}

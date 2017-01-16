/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Common implementation for file based word lists.
 *
 * @author  Middleware Services
 */
public abstract class AbstractFileWordList extends AbstractWordList
{

  /** Default cache size. */
  public static final int DEFAULT_CACHE_SIZE = 5;

  /** Byte marker indicating non-UTF-8 encoding. */
  private static final byte NON_UTF8_MARKER = (byte) 0xF0;

  /** UTF-8 BOM. */
  private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

  /** File containing words. */
  protected final RandomAccessFile file;

  /** Number of words in the file. */
  protected int size;

  /** Cache of indexes to file positions. */
  // CheckStyle:IllegalType OFF
  // uses the firstKey and floorKey implementations
  protected TreeMap<Integer, Long> cache = new TreeMap<>();
  // CheckStyle:IllegalType ON

  /** Buffer to hold word read from file. */
  private final ByteBuffer wordBuf = ByteBuffer.allocate(256);

  /** Buffer to hold file header that may contain UTF-8 BOM. */
  private final byte[] header = new byte[UTF8_BOM.length];

  /** Current position into backing file. */
  private long position;


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
  public void close() throws IOException
  {
    synchronized (cache) {
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

    if (cache == null) {
      throw new IllegalStateException("Cannot initialize after close has been called.");
    }

    FileWord a;
    FileWord b = null;
    synchronized (cache) {
      position = 0;
      seek(0);
      handleBom();
      cache.clear();
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
    FileWord w;
    synchronized (cache) {
      position = i > 0 ? cache.get(i) : 0L;
      seek(position);
      if (position == 0) {
        handleBom();
      }
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
   * @return  Buffer around backing file.
   */
  protected abstract ByteBuffer buffer();


  /**
   * Fills the buffer from the backing file. This method may be a no-op if the buffer contains all file contents.
   *
   * @throws  IOException  on I/O errors filling buffer.
   */
  protected abstract void fill() throws IOException;


  /**
   * Reads the next word from the current position in the backing file.
   *
   * @return  Data structure containing word and byte offset into file where word begins.
   *
   * @throws  IOException  on I/O errors reading file data.
   */
  private FileWord nextWord() throws IOException
  {
    byte b;
    long start = position;
    wordBuf.clear();
    while (hasRemaining()) {
      b = buffer().get();
      position++;
      if ((char) b == '\n' || (char) b == '\r') {
        // Ignore leading line termination characters
        if (wordBuf.position() == 0) {
          start++;
          continue;
        }
        break;
      }
      if ((b & NON_UTF8_MARKER) == NON_UTF8_MARKER) {
        throw new IOException("Invalid file encoding. UTF-8 is required.");
      }
      wordBuf.put(b);
    }
    if (wordBuf.position() == 0) {
      return null;
    }
    final String word = new String(wordBuf.array(), 0, wordBuf.position(), StandardCharsets.UTF_8);
    return new FileWord(word, start);
  }


  /**
   * Determines whether the backing buffer has any more data to read. If the buffer is empty, it attempts
   * to read from the underlying file and then checks the buffer again.
   *
   * @return  True if there is any more data to read from the buffer, false otherwise.
   *
   * @throws  IOException  on I/O errors reading file data.
   */
  private boolean hasRemaining() throws IOException
  {
    if (buffer().hasRemaining()) {
      return true;
    }
    fill();
    return buffer().hasRemaining();
  }


  /**
   * Handle UTF-8 BOM that may appear at file start.
   *
   * @throws  IOException  On I/O errors.
   */
  private void handleBom() throws IOException
  {
    buffer().get(header);
    if (Arrays.equals(UTF8_BOM, header)) {
      position = buffer().position();
    } else {
      // No BOM found, return to start of file
      seek(0);
    }
  }


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

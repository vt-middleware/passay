/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.LongBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * Common implementation for file based word lists.
 *
 * @author  Middleware Services
 */
public abstract class AbstractFileWordList extends AbstractWordList
{

  /** Default cache percent. */
  public static final int DEFAULT_CACHE_PERCENT = 5;

  /** File containing words. */
  protected final RandomAccessFile file;

  /** Number of words in the file. */
  protected int size;

  /** Cache of indexes to file positions. */
  private Cache cache;

  /** Character decoder. */
  private final CharsetDecoder charDecoder;

  /** Buffer to hold word read from file. */
  private final ByteBuffer wordBuf = ByteBuffer.allocate(256);

  /** Buffer to hold decoded word read from file. */
  private final CharBuffer charBuf = CharBuffer.allocate(wordBuf.capacity() * 4);

  /** Current position into backing file. */
  private long position;


  /**
   * Creates a new abstract file word list from the supplied file.
   *
   * @param  raf  File containing words, one per line.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   * @param  decoder  Charset decoder for converting file bytes to characters
   *
   * @throws  IllegalArgumentException  if cache percent is out of range.
   */
  public AbstractFileWordList(
    final RandomAccessFile raf,
    final boolean caseSensitive,
    final CharsetDecoder decoder)
  {
    file = raf;

    if (caseSensitive) {
      comparator = WordLists.CASE_SENSITIVE_COMPARATOR;
    } else {
      comparator = WordLists.CASE_INSENSITIVE_COMPARATOR;
    }
    charDecoder = decoder;
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
   * @param  allocateDirect  whether buffers should be allocated with {@link ByteBuffer#allocateDirect(int)}
   *
   * @throws  IOException  on I/O errors reading file data.
   */
  protected void initialize(final int cachePercent, final boolean allocateDirect) throws IOException
  {
    cache = new Cache(file.length(), cachePercent, allocateDirect);
    FileWord a;
    FileWord b = null;
    synchronized (cache) {
      position = 0;
      seek(position);
      while ((a = nextWord()) != null) {
        if (b != null && comparator.compare(a.word, b.word) < 0) {
          throw new IllegalArgumentException("File is not sorted correctly for this comparator");
        }
        b = a;
        cache.put(size++, a.offset);
      }
      cache.initialized = true;
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
    FileWord w;
    int i;
    synchronized (cache) {
      final Cache.Entry entry = cache.get(index);
      i = entry.index;
      seek(entry.position);
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
      wordBuf.put(b);
    }
    if (wordBuf.position() == 0) {
      return null;
    }

    charBuf.clear();
    wordBuf.flip();
    final CoderResult result = charDecoder.decode(wordBuf, charBuf, true);
    if (result.isError()) {
      result.throwException();
    }
    return new FileWord(charBuf.flip().toString(), start);
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


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::size=%s,cache=%s,charDecoder=%s",
        getClass().getName(),
        hashCode(),
        size,
        cache,
        charDecoder);
  }


  /**
   * Data structure containing word and byte offset into file where word begins in backing file.
   */
  protected static class FileWord
  {

    // CheckStyle:VisibilityModifier OFF
    /** Word read from backing file. */
    String word;

    /** Byte offset into file where word begins. */
    long offset;
    // CheckStyle:VisibilityModifier ON


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


  /** Cache of word indices to byte offsets where word starts in backing file. */
  private static class Cache
  {
    /** Cache entry that indicates cached word index and byte offset of start of word in backing file. */
    static class Entry
    {
      // CheckStyle:VisibilityModifier OFF
      /** Cached word index. */
      int index;

      /** Byte offset where word starts in backing file. */
      long position;
      // CheckStyle:VisibilityModifier ON


      /**
       * Creates a new cache entry.
       *
       * @param  i  Cached word index.
       * @param  pos  Byte offset where word starts in backing file.
       */
      Entry(final int i, final long pos)
      {
        index = i;
        position = pos;
      }
    }

    /** Map of word indices to the byte offset in the file where the word starts. */
    private LongBuffer map;

    /** Modulus of indices to cache. */
    private int modulus;

    /** Whether to allocate a direct buffer. */
    private boolean allocateDirect;

    /** Whether this cache is ready for use. */
    private boolean initialized;


    /**
     * Creates a new cache instance.
     *
     * @param  fileSize  Size of file in bytes.
     * @param  cachePercent  Percent of words to cache.
     * @param  direct  Whether to allocate a direct byte buffer.
     *
     * @throws  IllegalArgumentException  if cachePercent is not between 0-100 or the computed cache size exceeds {@link
     *                                    Integer#MAX_VALUE}
     */
    Cache(final long fileSize, final int cachePercent, final boolean direct)
    {
      if (cachePercent < 0 || cachePercent > 100) {
        throw new IllegalArgumentException("cachePercent must be between 0 and 100 inclusive");
      }
      allocateDirect = direct;
      long cacheSize = (fileSize / 100) * cachePercent;
      if (cacheSize > 0) {
        // buffer implementation requires at least 2 bytes
        if (cacheSize < Byte.SIZE * 2) {
          cacheSize = Byte.SIZE * 2;
        }
        modulus = (int) (fileSize / cacheSize);
        resize(cacheSize);
      }
    }


    /**
     * Puts an entry that maps the word at given index to the byte offset in into the backing file. The operation only
     * succeeds if it is determined that the supplied index should be stored based on the cachePercent, otherwise this
     * is a no-op.
     *
     * @param  index  Word at index.
     * @param  position  Byte offset into backing for file where word starts.
     */
    void put(final int index, final long position)
    {
      if (initialized) {
        throw new IllegalStateException("Cache initialized, put is not allowed");
      }
      if (modulus == 0 || index % modulus > 0) {
        return;
      }
      if (map.position() == map.capacity()) {
        // 12 = 1.5 * 8 since this is a long view of a byte buffer
        final long newSize = map.capacity() * 12L;
        resize(newSize);
      }
      map.put(position);
    }


    /**
     * Gets the byte offset into the backing file for the word at the index that is less than or equal to the supplied
     * index.
     *
     * @param  index  Word at index.
     *
     * @return  Nearest cache entry for given index.
     */
    Entry get(final int index)
    {
      if (modulus == 0 || index < modulus) {
        return new Entry(0, 0);
      }
      final int i = index / modulus;
      map.position(i);
      return new Entry(i * modulus, map.get());
    }


    /**
     * Creates a new byte buffer of the supplied size for use as the cache. If the cache already exists, it's contents
     * are copied into the new buffer.
     *
     * @param  size  Of byte buffer to create
     *
     * @throws  IllegalArgumentException  if size exceeds {@link Integer#MAX_VALUE}
     */
    private void resize(final long size)
    {
      if (size > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Cache limit exceeded. Try reducing cacheSize.");
      }
      final LongBuffer temp = allocateDirect ?
        ByteBuffer.allocateDirect((int) size).asLongBuffer() : ByteBuffer.allocate((int) size).asLongBuffer();
      if (map == null) {
        map = temp;
      } else {
        map.rewind();
        temp.put(map);
        map = temp;
      }
    }


    @Override
    public String toString()
    {
      return
        String.format(
          "%s@%h::size=%s,modulus=%s,allocateDirect=%s,initialized=%s",
          getClass().getSimpleName(),
          hashCode(),
          map != null ? map.capacity() : 0,
          modulus,
          allocateDirect,
          initialized);
    }
  }
}

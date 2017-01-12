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

  /** Buffered reader around backing file. */
  private BufferedReader reader;

  /** Current position in backing file. */
  private long position;


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
    initialize(cachePercent);
  }


  @Override
  protected void seek(final long offset) throws IOException
  {
    position = offset;
    file.seek(offset);
    reader = new BufferedReader(new FileReader(file.getFD()));
  }


  @Override
  protected FileWord nextWord() throws IOException
  {
    final StringBuilder line = new StringBuilder();
    int value;
    long pos = position;
    while ((value = reader.read()) != -1) {
      position++;
      final char c = (char) value;
      if (c == '\n' || c == '\r') {
        // Ignore leading line termination characters
        if (line.length() == 0) {
          pos = position;
          continue;
        }
        break;
      }
      line.append(c);
    }
    return line.length() > 0 ? new FileWord(line.toString(), pos) : null;
  }
}

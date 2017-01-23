/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

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

  /** Memory-mapped buffer around file. */
  private final MappedByteBuffer buffer;


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
    this(raf, caseSensitive, DEFAULT_CACHE_PERCENT);
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
    this(raf, caseSensitive, cachePercent, StandardCharsets.UTF_8.newDecoder());
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
   * @param  decoder  Charset decoder for converting file bytes to characters
   *
   * @throws  IllegalArgumentException  if cache percent is out of range.
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public MemoryMappedFileWordList(
    final RandomAccessFile raf,
    final boolean caseSensitive,
    final int cachePercent,
    final CharsetDecoder decoder)
    throws IOException
  {
    this(raf, caseSensitive, cachePercent, decoder, false);
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
   * @param  decoder  Charset decoder for converting file bytes to characters
   * @param  allocateDirect  whether buffers should be allocated with {@link ByteBuffer#allocateDirect(int)}
   *
   * @throws  IllegalArgumentException  if cache percent is out of range.
   * @throws  IOException  if an error occurs reading the supplied file
   */
  public MemoryMappedFileWordList(
    final RandomAccessFile raf,
    final boolean caseSensitive,
    final int cachePercent,
    final CharsetDecoder decoder,
    final boolean allocateDirect)
    throws IOException
  {
    super(raf, caseSensitive, decoder);
    final FileChannel channel = file.getChannel();
    buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    initialize(cachePercent, allocateDirect);
  }


  @Override
  protected void seek(final long offset)
    throws IOException
  {
    buffer.clear().position((int) offset);
  }


  @Override
  protected ByteBuffer buffer()
  {
    return buffer;
  }


  @Override
  protected void fill() throws IOException {}
}

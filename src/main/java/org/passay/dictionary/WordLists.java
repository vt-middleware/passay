/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.passay.PassayUtils;
import org.passay.dictionary.sort.ArraySorter;

/**
 * Utility class for common operations on word lists.
 *
 * @author  Middleware Services
 */
public final class WordLists
{

  /** Case sensitive comparator. */
  public static final Comparator<String> CASE_SENSITIVE_COMPARATOR = new Comparator<String>() {
    @Override
    public int compare(final String a, final String b)
    {
      PassayUtils.assertNotNullArg(a, "Compare string cannot be null");
      PassayUtils.assertNotNullArg(b, "Compare string cannot be null");
      return a.compareTo(b);
    }
    @Override
    public String toString()
    {
      return String.format("%s-%s@%d", getClass().getName(), "CASE_SENSITIVE", hashCode());
    }
  };

  /** Case insensitive comparator. */
  public static final Comparator<String> CASE_INSENSITIVE_COMPARATOR = new Comparator<String>() {
    @Override
    public int compare(final String a, final String b)
    {
      PassayUtils.assertNotNullArg(a, "Compare string cannot be null");
      PassayUtils.assertNotNullArg(b, "Compare string cannot be null");
      return a.compareToIgnoreCase(b);
    }
    @Override
    public String toString()
    {
      return String.format("%s-%s@%d", getClass().getName(), "CASE_INSENSITIVE", hashCode());
    }
  };

  /** Index returned when word not found by binary search. */
  public static final int NOT_FOUND = -1;


  /** Private constructor of utility class. */
  private WordLists() {}


  /**
   * Performs a binary search of the given word list for the given word.
   *
   * @param  wordList  to search
   * @param  word  to search for
   *
   * @return  index of supplied word in list or a negative number if not found.
   */
  public static int binarySearch(final WordList wordList, final String word)
  {
    PassayUtils.assertNotNullArg(wordList, "Word list cannot be null");
    PassayUtils.assertNotNullArg(word, "Word cannot be null");
    final Comparator<String> comparator = wordList.getComparator();
    int low = 0;
    int high = wordList.size() - 1;
    int mid;
    while (low <= high) {
      mid = (low + high) >>> 1;

      final int cmp = comparator.compare(wordList.get(mid), word);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return NOT_FOUND;
  }


  /**
   * Creates a case-sensitive {@link ArrayWordList} by reading the contents of the given readers.
   *
   * @param  readers  array of readers
   *
   * @return  word list read from the given readers
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
  public static ArrayWordList createFromReader(final Reader[] readers) throws IOException
  {
    return createFromReader(readers, true);
  }


  /**
   * Creates an {@link ArrayWordList} by reading the contents of the given readers.
   *
   * @param  readers  array of readers
   * @param  caseSensitive  set to true to create case-sensitive word list (default), false otherwise
   *
   * @return  word list read from the given readers
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
  public static ArrayWordList createFromReader(final Reader[] readers, final boolean caseSensitive) throws IOException
  {
    return createFromReader(readers, caseSensitive, null);
  }


  /**
   * Creates an {@link ArrayWordList} by reading the contents of the given file with support for sorting file contents.
   *
   * @param  readers  array of readers
   * @param  caseSensitive  set to true to create case-sensitive word list (default), false otherwise
   * @param  sorter  to sort the input array with
   *
   * @return  word list read from given readers
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
  public static ArrayWordList createFromReader(final Reader[] readers, final boolean caseSensitive,
    final ArraySorter sorter) throws IOException
  {
    PassayUtils.assertNotNullArgOr(
      readers,
      v -> Stream.of(v).anyMatch(Objects::isNull),
      "Readers cannot be null or contain null");
    final List<String> words = new ArrayList<>();
    for (Reader r : readers) {
      readWordList(r, words);
    }
    return new ArrayWordList(words.toArray(new String[0]), caseSensitive, sorter);
  }


  /**
   * Reads words, one per line, from a reader into the given word list.
   * <p>
   * This method does <em>not</em> close the reader.
   *
   * @param reader the reader to read words from
   * @param words the list to which the words are added
   * @throws IOException if an error occurs
   */
  public static void readWords(final Reader reader, final List<String> words) throws IOException
  {
    PassayUtils.assertNotNullArg(reader, "Reader cannot be null");
    PassayUtils.assertNotNullArg(words, "Word list cannot be null");
    final BufferedReader bufferedReader = reader instanceof BufferedReader
            ? (BufferedReader) reader
            : new BufferedReader(reader);
    String word;
    while ((word = bufferedReader.readLine()) != null) {
      if (!word.isEmpty()) {
        words.add(word);
      }
    }
  }


  /**
   * Reads words, one per line, from an input stream into the given word list.
   * <p>
   * This method does <em>not</em> close the input stream.
   *
   * @param in the input stream to read words from
   * @param charset the charset used to decode text from the stream
   * @param words the list to which the words are added
   * @throws IOException if an error occurs
   */
  public static void readWords(final InputStream in, final String charset, final List<String> words) throws IOException
  {
    PassayUtils.assertNotNullArg(in, "Input stream cannot be null");
    PassayUtils.assertNotNullArg(charset, "Character set cannot be null");
    readWords(new InputStreamReader(in, charset), words);
  }


  /**
   * Reads words, one per line, from an input stream into the given word list.
   * The input stream is assumed to contain compressed data in the ZIP format.
   * <p>
   * This method does <em>not</em> close the input stream.
   *
   * @param in the input stream containing compressed data to read words from
   * @param charset the charset used to decode text from the stream
   * @param regex a regular expression that is used to match the ZIP entry names to
   *        determine which of the entries should be read, or null if all entries should be read
   * @param words the list to which the words are added
   * @throws IOException if an error occurs
   */
  public static void readZippedWords(final InputStream in, final String charset,
                                      final String regex, final List<String> words) throws IOException
  {
    PassayUtils.assertNotNullArg(in, "Input stream cannot be null");
    PassayUtils.assertNotNullArg(charset, "Character set cannot be null");
    PassayUtils.assertNotNullArg(words, "Word list cannot be null");
    final Pattern pattern = regex == null ? null : Pattern.compile(regex);
    final ZipInputStream zin = new ZipInputStream(in);
    ZipEntry entry;
    while ((entry = zin.getNextEntry()) != null) {
      if (!entry.isDirectory() && (pattern == null || pattern.matcher(entry.getName()).matches())) {
        // don't close the reader, since that will close the entire zip input stream
        readWords(zin, charset, words);
      }
      zin.closeEntry();
    }
  }


  /**
   * Reads words, one per line, from a reader into the given word list.
   *
   * @param  reader  Reader containing words, one per line. The reader is closed on completion.
   * @param  words  Destination word list.
   *
   * @throws  IOException  on IO errors reading from reader.
   */
  public static void readWordList(final Reader reader, final List<String> words) throws IOException
  {
    try {
      readWords(reader, words);
    } finally {
      reader.close();
    }
  }
}

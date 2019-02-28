/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
  public static ArrayWordList createFromReader(final Reader[] readers)
    throws IOException
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
  public static ArrayWordList createFromReader(final Reader[] readers, final boolean caseSensitive)
    throws IOException
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
  public static ArrayWordList createFromReader(
    final Reader[] readers,
    final boolean caseSensitive,
    final ArraySorter sorter)
    throws IOException
  {
    final List<String> words = new ArrayList<>();
    for (Reader r : readers) {
      readWordList(r, words);
    }
    return new ArrayWordList(words.toArray(new String[words.size()]), caseSensitive, sorter);
  }


  /**
   * Reads words, one per line, from a reader into the given word list.
   *
   * @param  reader  Reader containing words, one per line. The reader is closed on completion.
   * @param  wordList  Destination word list.
   *
   * @throws  IOException  on IO errors reading from reader.
   */
  public static void readWordList(final Reader reader, final List<String> wordList)
    throws IOException
  {
    try {
      final BufferedReader bufferedReader;
      if (reader instanceof BufferedReader) {
        bufferedReader = (BufferedReader) reader;
      } else {
        bufferedReader = new BufferedReader(reader);
      }

      String word;
      while ((word = bufferedReader.readLine()) != null) {
        if (!word.isEmpty()) {
          wordList.add(word);
        }
      }
    } finally {
      reader.close();
    }
  }
}

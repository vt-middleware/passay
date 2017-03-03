/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Arrays;
import org.passay.dictionary.sort.ArraySorter;

/**
 * Provides a {@link WordList} backed by a string array. Since the entire word list is stored in memory java heap
 * settings may need to be modified in order to store large word lists.
 *
 * @author  Middleware Services
 */
public class ArrayWordList extends AbstractWordList
{

  /** file containing words. */
  protected String[] words;


  /**
   * Creates a new case-sensitive word list backed by the given array.
   *
   * @param  array  Array of words.
   *
   * @throws  IllegalArgumentException  If array is null or contains any null entries.
   */
  public ArrayWordList(final String[] array)
  {
    this(array, true);
  }


  /**
   * Creates a new word list backed by the given array.
   *
   * @param  array  Array of words.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   *
   * @throws  IllegalArgumentException  If array is null or contains any null entries.
   */
  public ArrayWordList(final String[] array, final boolean caseSensitive)
  {
    this(array, caseSensitive, null);
  }


  /**
   * Creates a new word list backed by the given array with optional sorting of the input string array.
   *
   * @param  array  Array of words.
   * @param  caseSensitive  Set to true to create case-sensitive word list, false otherwise.
   * @param  sorter  To sort the input array with. The sort routine is consistent with {@link #getComparator()}, which
   *                 respects the case sensitivity of the word list.
   *
   * @throws  IllegalArgumentException  If array is null or contains any null entries.
   */
  public ArrayWordList(final String[] array, final boolean caseSensitive, final ArraySorter sorter)
  {
    if (array == null) {
      throw new IllegalArgumentException("Array cannot be null.");
    }
    if (caseSensitive) {
      comparator = WordLists.CASE_SENSITIVE_COMPARATOR;
    } else {
      comparator = WordLists.CASE_INSENSITIVE_COMPARATOR;
    }
    if (sorter != null) {
      sorter.sort(array, comparator);
    }
    for (int i = 0; i < array.length; i++) {
      if (array[i] == null) {
        throw new IllegalArgumentException(Arrays.toString(array) + " cannot contain null entry at index " + i);
      }
      if (i > 0 && comparator.compare(array[i], array[i - 1]) < 0) {
        throw new IllegalArgumentException(
          Arrays.toString(array) + " sorted by " + sorter + " is not correct for " + comparator + " at index " + i);
      }
    }
    words = array;
  }


  @Override
  public String get(final int index)
  {
    checkRange(index);
    return words[index];
  }


  @Override
  public int size()
  {
    return words.length;
  }


  @Override
  public String toString()
  {
    return String.format("%s@%h::size=%s", getClass().getName(), hashCode(), words.length);
  }
}

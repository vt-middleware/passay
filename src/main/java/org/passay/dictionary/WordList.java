/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Represents a random-access list of words.
 *
 * @author  Middleware Services
 */
public interface WordList
{


  /**
   * Returns the comparator that should be used to compare a search term with candidate words in the list. The
   * comparator naturally respects ordering and case sensitivity of the word list.
   *
   * @return  comparator for words in the list.
   */
  Comparator<String> getComparator();


  /**
   * Returns the word at the given 0-based index.
   *
   * @param  index  0-based index.
   *
   * @return  word at given index.
   */
  String get(int index);


  /**
   * Returns an iterator to traverse this word list from the 0th index.
   *
   * @return  iterator for this word list
   */
  Iterator<String> iterator();


  /**
   * Returns an iterator to traverse this word list by following a recursive sequence of medians.
   *
   * @return  iterator for this word list
   */
  Iterator<String> medianIterator();


  /**
   * Returns the number of words in the list.
   *
   * @return  total number of words in list.
   */
  int size();
}

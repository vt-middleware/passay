/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;

/**
 * Interface for array sort implementations.
 *
 * @author  Middleware Services
 */
public interface ArraySorter
{


  /**
   * This will sort the supplied string array.
   *
   * @param  array  To sort
   */
  void sort(String[] array);


  /**
   * This will sort the supplied string array.
   *
   * @param  array  To sort
   * @param  c  Comparator to sort with
   */
  void sort(String[] array, Comparator<String> c);
}

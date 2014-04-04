/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;
import org.passay.dictionary.WordLists;

/**
 * Provides an implementation of the selection sort algorithm.
 *
 * @author  Middleware Services
 */
public class SelectionSort implements ArraySorter
{


  @Override
  public void sort(final String[] array)
  {
    sort(array, WordLists.CASE_SENSITIVE_COMPARATOR);
  }


  @Override
  public void sort(final String[] array, final Comparator<String> c)
  {
    final int n = array.length;
    for (int i = 0; i < n - 1; i++) {
      int min = i;
      for (int j = i + 1; j < n; j++) {
        final String b = array[j];
        if (c.compare(b, array[min]) < 0) {
          min = j;
        }
      }

      final String s = array[min];
      array[min] = array[i];
      array[i] = s;
    }
  }
}

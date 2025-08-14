/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;
import org.passay.PassayUtils;

/**
 * Provides an implementation of the selection sort algorithm.
 *
 * @author  Middleware Services
 */
public class SelectionSort implements ArraySorter
{

  @Override
  public void sort(final String[] array, final Comparator<String> comparator)
  {
    PassayUtils.assertNotNullArg(array, "Array cannot be null");
    PassayUtils.assertNotNullArg(comparator, "Comparator cannot be null");
    final int n = array.length;
    for (int i = 0; i < n - 1; i++) {
      int min = i;
      for (int j = i + 1; j < n; j++) {
        final String b = array[j];
        if (comparator.compare(b, array[min]) < 0) {
          min = j;
        }
      }

      final String s = array[min];
      array[min] = array[i];
      array[i] = s;
    }
  }
}

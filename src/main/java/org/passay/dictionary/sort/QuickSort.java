/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;

/**
 * Provides an implementation of the quick sort algorithm.
 *
 * @author  Middleware Services
 */
public class QuickSort implements ArraySorter
{

  @Override
  public void sort(final String[] array, final Comparator<String> c)
  {
    if (array.length > 0) {
      sort(array, c, 0, array.length - 1);
    }
  }


  /**
   * This will sort the supplied array beginning at the lo index and ending at the hi index, using the quick sort
   * algorithm.
   *
   * @param  array  to sort
   * @param  c  comparator to sort with
   * @param  lo  index to beginning sorting at
   * @param  hi  index to stop sorting at
   */
  public static void sort(final String[] array, final Comparator<String> c, final int lo, final int hi)
  {
    final int m = (lo + hi) >>> 1;
    final String x = array[m];

    int i = lo;
    int j = hi;
    do {
      while (c.compare(x, array[i]) > 0) {
        i++;
      }

      while (c.compare(x, array[j]) < 0) {
        j--;
      }

      if (i <= j) {
        final String s = array[i];
        array[i] = array[j];
        array[j] = s;
        i++;
        j--;
      }
    } while (i <= j);

    if (lo < j) {
      sort(array, c, lo, j);
    }
    if (i < hi) {
      sort(array, c, i, hi);
    }
  }
}

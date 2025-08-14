/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;
import org.passay.PassayUtils;

/**
 * Provides an implementation of the quick sort algorithm.
 *
 * @author  Middleware Services
 */
public class QuickSort implements ArraySorter
{

  @Override
  public void sort(final String[] array, final Comparator<String> comparator)
  {
    PassayUtils.assertNotNullArg(array, "Array cannot be null");
    PassayUtils.assertNotNullArg(comparator, "Comparator cannot be null");
    if (array.length > 0) {
      sort(array, comparator, 0, array.length - 1);
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
    PassayUtils.assertNotNullArg(array, "Array cannot be null");
    PassayUtils.assertNotNullArg(c, "Comparator cannot be null");
    final int m = (lo + hi) >>> 1;
    final String x = array[m];

    int i = lo;
    int j = hi;
    do {
      while (i < array.length && c.compare(x, array[i]) > 0) {
        i++;
      }

      while (j > -1 && c.compare(x, array[j]) < 0) {
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

    if (j > lo && j < hi) {
      sort(array, c, lo, j);
    }
    if (i > lo && i < hi) {
      sort(array, c, i, hi);
    }
  }
}

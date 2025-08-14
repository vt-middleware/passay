/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;
import org.passay.PassayUtils;

/**
 * Provides an implementation of the bubble sort algorithm.
 *
 * @author  Middleware Services
 */
public class BubbleSort implements ArraySorter
{

  @Override
  public void sort(final String[] array, final Comparator<String> comparator)
  {
    PassayUtils.assertNotNullArg(array, "Array cannot be null");
    PassayUtils.assertNotNullArg(comparator, "Comparator cannot be null");
    final int n = array.length;
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - 1 - i; j++) {
        final String a = array[j];
        final String b = array[j + 1];
        if (comparator.compare(a, b) > 0) {
          array[j] = b;
          array[j + 1] = a;
        }
      }
    }
  }
}

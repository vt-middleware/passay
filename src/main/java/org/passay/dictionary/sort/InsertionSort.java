/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Comparator;
import org.passay.dictionary.WordLists;

/**
 * Provides an implementation of the insertion sort algorithm.
 *
 * @author  Middleware Services
 */
public class InsertionSort implements ArraySorter
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
    for (int i = 1; i < n; i++) {
      int j = i - 1;
      final String a = array[i];
      String b;
      // CheckStyle:InnerAssignment OFF
      while (j >= 0 && (c.compare(a, b = array[j]) < 0)) {
        array[j + 1] = b;
        j--;
      }
      // CheckStyle:InnerAssignment ON
      array[j + 1] = a;
    }
  }
}

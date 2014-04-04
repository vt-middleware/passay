/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Delegates sorting to {@link java.util.Arrays#sort(Object[], Comparator)}.
 *
 * @author  Middleware Services
 */
public class ArraysSort implements ArraySorter
{


  @Override
  public void sort(final String[] array)
  {
    Arrays.sort(array);
  }


  @Override
  public void sort(final String[] array, final Comparator<String> c)
  {
    Arrays.sort(array, c);
  }
}

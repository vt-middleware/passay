/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import org.passay.PassayUtils;

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
    Arrays.sort(
      PassayUtils.assertNotNullArgOr(
        array,
        v -> Stream.of(v).anyMatch(Objects::isNull),
        "Array cannot be null or contain null"));
  }


  @Override
  public void sort(final String[] array, final Comparator<CharSequence> comparator)
  {
    Arrays.sort(
      PassayUtils.assertNotNullArgOr(
        array,
        v -> Stream.of(v).anyMatch(Objects::isNull),
        "Array cannot be null or contain null"),
      PassayUtils.assertNotNullArg(comparator, "Comparator cannot be null"));
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import org.passay.dictionary.TestUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link ArraySorter} implementations.
 *
 * @author  Middleware Services
 */
public class SorterTest
{

  /** Test dictionary. */
  private static final String DICTIONARY = "src/test/resources/freebsd";

  /** Test dictionary sorted. */
  private static final String DICTIONARY_SORTED = "src/test/resources/freebsd.sort";

  /** word list to use for comparison. */
  private String[] sortedArray;


  /**
   * @throws  Exception  On test failure.
   */
  @BeforeClass
  public void create() throws Exception
  {
    sortedArray = TestUtil.fileToArray(DICTIONARY_SORTED);
  }

  /**
   * Close test resources.
   */
  @AfterClass
  public void destroy()
  {
    sortedArray = null;
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void bubbleSort() throws Exception
  {
    final String[] array = TestUtil.fileToArray(DICTIONARY);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new BubbleSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void collectionsSort() throws Exception
  {
    final String[] array = TestUtil.fileToArray(DICTIONARY);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new ArraysSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void insertionSort() throws Exception
  {
    final String[] array = TestUtil.fileToArray(DICTIONARY);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new InsertionSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void quickSort() throws Exception
  {
    final String[] array = TestUtil.fileToArray(DICTIONARY);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new QuickSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void selectionSort() throws Exception
  {
    final String[] array = TestUtil.fileToArray(DICTIONARY);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new SelectionSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  @Test
  public void emptyArraySort()
  {
    new BubbleSort().sort(new String[0]);
    new BubbleSort().sort(new String[]{""});
    new InsertionSort().sort(new String[0]);
    new InsertionSort().sort(new String[]{""});
    new QuickSort().sort(new String[0]);
    new QuickSort().sort(new String[]{""});
    new SelectionSort().sort(new String[0]);
    new SelectionSort().sort(new String[]{""});
  }


  @Test
  public void brokenComparatorSort()
  {
    new BubbleSort().sort(new java.lang.String[]{"a", "b", "c"}, (o1, o2) -> -1);
    new InsertionSort().sort(new java.lang.String[]{"a", "b", "c"}, (o1, o2) -> -1);
    new QuickSort().sort(new java.lang.String[]{"a", "b", "c"}, (o1, o2) -> -1);
    new SelectionSort().sort(new java.lang.String[]{"a", "b", "c"}, (o1, o2) -> -1);
  }


  /**
   * Sorts the supplied list with the supplied sorter.
   *
   * @param  s  sorter to sort with
   * @param  array  to sort
   */
  public void doSort(final ArraySorter s, final String[] array)
  {
    long t = System.nanoTime();
    s.sort(array);
    t = System.nanoTime() - t;
    System.out.println(s.getClass().getSimpleName() + " sort time (" + array.length + "): " + t + "ns");
  }
}

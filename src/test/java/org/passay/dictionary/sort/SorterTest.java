/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import org.passay.dictionary.TestUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link ArraySorter} implementations.
 *
 * @author  Middleware Services
 */
public class SorterTest
{

  /** word list to use for comparison. */
  private String[] sortedArray;


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFileSorted")
  @BeforeClass
  public void create(final String dict) throws Exception
  {
    sortedArray = TestUtil.fileToArray(dict);
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
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test
  public void bubbleSort(final String dict) throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new BubbleSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test
  public void collectionsSort(final String dict) throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new ArraysSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test
  public void insertionSort(final String dict) throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new InsertionSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test
  public void quickSort(final String dict) throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    assertThat(array).isNotEqualTo(sortedArray);
    doSort(new QuickSort(), array);
    assertThat(array).isEqualTo(sortedArray);
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test
  public void selectionSort(final String dict) throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
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

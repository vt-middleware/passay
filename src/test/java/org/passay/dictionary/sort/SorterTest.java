/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary.sort;

import java.util.Arrays;
import org.passay.dictionary.TestUtil;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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
  @BeforeClass(groups = {"sorttest"})
  public void create(final String dict)
    throws Exception
  {
    sortedArray = TestUtil.fileToArray(dict);
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"sorttest"})
  public void destroy()
    throws Exception
  {
    sortedArray = null;
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test(groups = {"sorttest"})
  public void bubbleSort(final String dict)
    throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    AssertJUnit.assertFalse(Arrays.equals(sortedArray, array));
    doSort(new BubbleSort(), array);
    AssertJUnit.assertTrue(Arrays.equals(sortedArray, array));
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test(groups = {"sorttest"})
  public void collectionsSort(final String dict)
    throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    AssertJUnit.assertFalse(Arrays.equals(sortedArray, array));
    doSort(new ArraysSort(), array);
    AssertJUnit.assertTrue(Arrays.equals(sortedArray, array));
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test(groups = {"sorttest"})
  public void insertionSort(final String dict)
    throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    AssertJUnit.assertFalse(Arrays.equals(sortedArray, array));
    doSort(new InsertionSort(), array);
    AssertJUnit.assertTrue(Arrays.equals(sortedArray, array));
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test(groups = {"sorttest"})
  public void quickSort(final String dict)
    throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    AssertJUnit.assertFalse(Arrays.equals(sortedArray, array));
    doSort(new QuickSort(), array);
    AssertJUnit.assertTrue(Arrays.equals(sortedArray, array));
  }


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFile")
  @Test(groups = {"sorttest"})
  public void selectionSort(final String dict)
    throws Exception
  {
    final String[] array = TestUtil.fileToArray(dict);
    AssertJUnit.assertFalse(Arrays.equals(sortedArray, array));
    doSort(new SelectionSort(), array);
    AssertJUnit.assertTrue(Arrays.equals(sortedArray, array));
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

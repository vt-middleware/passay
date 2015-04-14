/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.util.Arrays;
import org.passay.dictionary.sort.ArraySorter;
import org.passay.dictionary.sort.ArraysSort;
import org.passay.dictionary.sort.BubbleSort;
import org.passay.dictionary.sort.InsertionSort;
import org.passay.dictionary.sort.QuickSort;
import org.passay.dictionary.sort.SelectionSort;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link TernaryTreeDictionary}.
 *
 * @author  Middleware Services
 */
public class TernaryTreeDictionaryTest extends AbstractDictionaryTest
{

  /** Test dictionary. */
  private TernaryTreeDictionary caseSensitive;

  /** Test dictionary. */
  private TernaryTreeDictionary caseInsensitive;


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"ttdicttest"})
  public void createDictionary()
    throws Exception
  {
    final ArrayWordList awl1 = WordLists.createFromReader(
      new FileReader[] {new FileReader(webFile)},
      true,
      new ArraysSort());
    caseSensitive = new TernaryTreeDictionary(awl1);

    final ArrayWordList awl2 = WordLists.createFromReader(
      new FileReader[] {new FileReader(webFile)},
      false,
      new ArraysSort());
    caseInsensitive = new TernaryTreeDictionary(awl2);
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"ttdicttest"})
  public void closeDictionary()
    throws Exception
  {
    caseSensitive = null;
    caseInsensitive = null;
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"ttdicttest"})
  public void search()
    throws Exception
  {
    AssertJUnit.assertTrue(caseSensitive.search("manipular"));
    AssertJUnit.assertFalse(caseSensitive.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(caseInsensitive.search("manipular"));
    AssertJUnit.assertTrue(caseInsensitive.search("manipular".toUpperCase()));
    AssertJUnit.assertFalse(caseInsensitive.search(FALSE_SEARCH));
  }


  /**
   * This test is disabled by default. It produces a lot of testng report data which runs the process OOM.
   *
   * @param  word  to search for.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"ttdicttest"}, dataProvider = "all-web-words", enabled = false)
  public void searchAll(final String word)
    throws Exception
  {
    AssertJUnit.assertTrue(caseSensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toLowerCase()));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toUpperCase()));
  }


  /**
   * @param  word  to search for.
   * @param  results  case sensitive results
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "partialSearchWord", "partialSearchResults" })
  @Test(groups = {"ttdicttest"})
  public void partialSearch(final String word, final String results)
    throws Exception
  {
    AssertJUnit.assertTrue(Arrays.equals(results.split("\\|"), caseSensitive.partialSearch(word)));
    AssertJUnit.assertFalse(Arrays.equals(results.split("\\|"), caseSensitive.partialSearch(FALSE_SEARCH)));

    try {
      caseInsensitive.partialSearch(word);
      AssertJUnit.fail("Should have thrown UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      AssertJUnit.assertEquals(e.getClass(), UnsupportedOperationException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown UnsupportedOperationException, threw " + e.getMessage());
    }
  }


  /**
   * @param  word  to search for.
   * @param  distance  for near search
   * @param  results  case sensitive results
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "nearSearchWord", "nearSearchDistance", "nearSearchResults" })
  @Test(groups = {"ttdicttest"})
  public void nearSearch(final String word, final int distance, final String results)
    throws Exception
  {
    AssertJUnit.assertTrue(Arrays.equals(results.split("\\|"), caseSensitive.nearSearch(word, distance)));
    AssertJUnit.assertFalse(Arrays.equals(results.split("\\|"), caseSensitive.nearSearch(FALSE_SEARCH, distance)));

    try {
      caseInsensitive.nearSearch(word, distance);
      AssertJUnit.fail("Should have thrown UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      AssertJUnit.assertEquals(e.getClass(), UnsupportedOperationException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown UnsupportedOperationException, threw " + e.getMessage());
    }
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"ttdicttest"})
  public void bubbleSort()
    throws Exception
  {
    testSort(new BubbleSort());
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"ttdicttest"})
  public void selectionSort()
    throws Exception
  {
    testSort(new SelectionSort());
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"ttdicttest"})
  public void insertionSort()
    throws Exception
  {
    testSort(new InsertionSort());
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"ttdicttest"})
  public void quickSort()
    throws Exception
  {
    testSort(new QuickSort());
  }


  /**
   * @param  sorter  to sort with
   *
   * @throws  Exception  On test failure.
   */
  public void testSort(final ArraySorter sorter)
    throws Exception
  {
    ArrayWordList awl = new ArrayWordList(ANIMALS, true, sorter);
    final TernaryTreeDictionary sortCS = new TernaryTreeDictionary(awl);
    AssertJUnit.assertTrue(sortCS.search(ANIMAL_SEARCH_CS));
    AssertJUnit.assertFalse(sortCS.search(ANIMAL_SEARCH_CI));
    AssertJUnit.assertTrue(
      Arrays.equals(ANIMAL_PARTIAL_SEARCH_RESULTS_CS, sortCS.partialSearch(ANIMAL_PARTIAL_SEARCH)));
    AssertJUnit.assertFalse(
      Arrays.equals(ANIMAL_PARTIAL_SEARCH_RESULTS_CI, sortCS.partialSearch(ANIMAL_PARTIAL_SEARCH)));

    awl = new ArrayWordList(ANIMALS, false, sorter);

    final TernaryTreeDictionary sortCI = new TernaryTreeDictionary(awl);
    AssertJUnit.assertTrue(sortCI.search(ANIMAL_SEARCH_CS));
    AssertJUnit.assertTrue(sortCI.search(ANIMAL_SEARCH_CI));
  }
}

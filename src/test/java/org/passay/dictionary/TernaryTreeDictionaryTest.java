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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @BeforeClass(groups = "ttdicttest")
  public void createDictionary() throws Exception
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


  /**
   * Close test resources.
   */
  @AfterClass(groups = "ttdicttest")
  public void closeDictionary()
  {
    caseSensitive = null;
    caseInsensitive = null;
  }


  /**
   * Test search.
   */
  @Test(groups = "ttdicttest")
  public void search()
  {
    assertThat(caseSensitive.search("manipular")).isTrue();
    assertThat(caseSensitive.search(FALSE_SEARCH)).isFalse();
    assertThat(caseSensitive.search("z")).isTrue();
    assertThat(caseInsensitive.search("manipular")).isTrue();
    assertThat(caseInsensitive.search("manipular".toUpperCase())).isTrue();
    assertThat(caseInsensitive.search(FALSE_SEARCH)).isFalse();
    assertThat(caseInsensitive.search("z")).isTrue();
  }


  /**
   * This test is disabled by default. It produces a lot of testing report data which runs the process OOM.
   *
   * @param  word  to search for.
   */
  @Test(groups = "ttdicttest", dataProvider = "all-web-words", enabled = false)
  public void searchAll(final String word)
  {
    assertThat(caseSensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word.toLowerCase())).isTrue();
    assertThat(caseInsensitive.search(word.toUpperCase())).isTrue();
  }


  /**
   * @param  word  to search for.
   * @param  results  case sensitive results
   */
  @Parameters({ "partialSearchWord", "partialSearchResults" })
  @Test(groups = "ttdicttest")
  public void partialSearch(final String word, final String results)
  {
    assertThat(caseSensitive.partialSearch(word)).isEqualTo(results.split("\\|"));
    assertThat(Arrays.equals(results.split("\\|"), caseSensitive.partialSearch(FALSE_SEARCH))).isFalse();

    try {
      caseInsensitive.partialSearch(word);
      fail("Should have thrown UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertThat(e).isExactlyInstanceOf(UnsupportedOperationException.class);
    } catch (Exception e) {
      fail("Should have thrown UnsupportedOperationException, threw %s", e.getMessage());
    }
  }


  /**
   * @param  word  to search for.
   * @param  distance  for near search
   * @param  results  case sensitive results
   */
  @Parameters({ "nearSearchWord", "nearSearchDistance", "nearSearchResults" })
  @Test(groups = "ttdicttest")
  public void nearSearch(final String word, final int distance, final String results)
  {
    assertThat(caseSensitive.nearSearch(word, distance)).isEqualTo(results.split("\\|"));
    assertThat(Arrays.equals(results.split("\\|"), caseSensitive.nearSearch(FALSE_SEARCH, distance))).isFalse();

    try {
      caseInsensitive.nearSearch(word, distance);
      fail("Should have thrown UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertThat(e).isExactlyInstanceOf(UnsupportedOperationException.class);
    } catch (Exception e) {
      fail("Should have thrown UnsupportedOperationException, threw %s", e.getMessage());
    }
  }


  @Test(groups = "ttdicttest")
  public void bubbleSort()
  {
    testSort(new BubbleSort());
  }


  @Test(groups = "ttdicttest")
  public void selectionSort()
  {
    testSort(new SelectionSort());
  }


  @Test(groups = "ttdicttest")
  public void insertionSort()
  {
    testSort(new InsertionSort());
  }


  @Test(groups = "ttdicttest")
  public void quickSort()
  {
    testSort(new QuickSort());
  }


  /**
   * @param  sorter  to sort with
   */
  public void testSort(final ArraySorter sorter)
  {
    ArrayWordList awl = new ArrayWordList(getAnimals(), true, sorter);
    final TernaryTreeDictionary sortCS = new TernaryTreeDictionary(awl);
    assertThat(sortCS.search(ANIMAL_SEARCH_CS)).isTrue();
    assertThat(sortCS.search(ANIMAL_SEARCH_CI)).isFalse();
    assertThat(sortCS.partialSearch(ANIMAL_PARTIAL_SEARCH)).isEqualTo(ANIMAL_PARTIAL_SEARCH_RESULTS_CS);
    assertThat(sortCS.partialSearch(ANIMAL_PARTIAL_SEARCH)).isNotEqualTo(ANIMAL_PARTIAL_SEARCH_RESULTS_CI);

    awl = new ArrayWordList(getAnimals(), false, sorter);

    final TernaryTreeDictionary sortCI = new TernaryTreeDictionary(awl);
    assertThat(sortCI.search(ANIMAL_SEARCH_CS)).isTrue();
    assertThat(sortCI.search(ANIMAL_SEARCH_CI)).isTrue();
  }
}

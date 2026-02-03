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
  @BeforeClass
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
  @AfterClass
  public void closeDictionary()
  {
    caseSensitive = null;
    caseInsensitive = null;
  }


  /**
   * Test search.
   */
  @Test
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
  @Test(dataProvider = "all-web-words", enabled = false)
  public void searchAll(final String word)
  {
    assertThat(caseSensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word.toLowerCase())).isTrue();
    assertThat(caseInsensitive.search(word.toUpperCase())).isTrue();
  }


  /**
   * Test for partial word search.
   */
  @Test
  public void partialSearch()
  {
    final String word = ".e.e.e.e";
    final String results = "Genevese|reserene|teleseme|terebene";

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
   * Test for near word search.
   */
  @Test
  public void nearSearch()
  {
    final String word = "Jicaque";
    final int distance = 2;
    final String results = "Jicaque|Jicaquean|Xicaque|macaque";

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


  @Test
  public void bubbleSort()
  {
    testSort(new BubbleSort());
  }


  @Test
  public void selectionSort()
  {
    testSort(new SelectionSort());
  }


  @Test
  public void insertionSort()
  {
    testSort(new InsertionSort());
  }


  @Test
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

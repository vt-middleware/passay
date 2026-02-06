/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Arrays;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link TernaryTree}.
 *
 * @author  Middleware Services
 */
public class TernaryTreeTest
{


  /** Case sensitive ternary tree. */
  private TernaryTree caseSensitive = new TernaryTree(true);

  /** Case insensitive ternary tree. */
  private TernaryTree caseInsensitive = new TernaryTree(false);


  /**
   * @throws  Exception  On test failure.
   */
  @BeforeClass
  public void createTernaryTrees() throws Exception
  {
    caseSensitive.insert(TestUtil.fileToArray(Dictionaries.WEB_FILE));
    caseInsensitive.insert(TestUtil.fileToArray(Dictionaries.WEB_FILE_SORTED));
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
   * Creates search test data.
   *
   * @return  Search test data.
   */
  @DataProvider(name = "searchData")
  public Object[][] createSearchData()
  {
    return
      new Object[][] {
        {caseSensitive, "ornithopter", true},
        {caseSensitive, "Pawpaw", false},
        {caseInsensitive, "Jocular", true},
        {caseSensitive, "brujo", false},
      };
  }


  /**
   * Creates partial search test data.
   *
   * @return  Partial search test data.
   */
  @DataProvider(name = "partialSearchData")
  public Object[][] createPartialSearchData()
  {
    return
      new Object[][] {
        {
          caseSensitive,
          ".e.e.e.e",
          new String[] {"Genevese", "reserene", "teleseme", "terebene"},
        },
        {
          caseSensitive,
          ".ix",
          new String[] {"Aix", "fix", "mix", "nix", "pix", "rix", "six"},
        },
      };
  }


  /**
   * Creates near search test data.
   *
   * @return  Near search test data.
   */
  @DataProvider(name = "nearSearchData")
  public Object[][] createNearSearchData()
  {
    return
      new Object[][] {
        {
          caseSensitive,
          "Jicaque",
          2,
          new String[] {"Jicaque", "Jicaquean", "Xicaque", "macaque"},
        },
      };
  }


  /**
   * @param  tt  Ternary Tree searched for target word.
   * @param  word  Target word.
   * @param  expected  Expected search result.
   */
  @Test(dataProvider = "searchData")
  public void search(final TernaryTree tt, final String word, final boolean expected)
  {
    assertThat(tt.search(word)).isEqualTo(expected);
  }


  /**
   * @param  tt  Ternary Tree searched for target word.
   * @param  searchTerm  Partial search term.
   * @param  expected  Expected partial search results.
   */
  @Test(dataProvider = "partialSearchData")
  public void partialSearch(final TernaryTree tt, final String searchTerm, final String[] expected)
  {
    final CharSequence[] actual = tt.partialSearch(searchTerm);
    System.out.println("Partial search results: " + Arrays.toString(actual));
    System.out.println("Partial search expected: " + Arrays.toString(expected));
    assertThat(actual).isEqualTo(expected);
  }


  /**
   * @param  tt  Ternary Tree searched for target word.
   * @param  word  to search for.
   * @param  distance  for near search
   * @param  expected  Expected partial search results.
   */
  @Test(dataProvider = "nearSearchData")
  public void nearSearch(final TernaryTree tt, final String word, final int distance, final String[] expected)
  {
    final CharSequence[] actual = tt.nearSearch(word, distance);
    System.out.println("Near search results: " + Arrays.toString(actual));
    System.out.println("Near search expected: " + Arrays.toString(expected));
    assertThat(actual).isEqualTo(expected);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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
   * @param  dict1  to load.
   * @param  dict2  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "webFile", "webFileSorted" })
  @BeforeClass(groups = "tttest")
  public void createTernaryTrees(final String dict1, final String dict2) throws Exception
  {
    caseSensitive.insert(TestUtil.fileToArray(dict1));
    caseInsensitive.insert(TestUtil.fileToArray(dict2));
  }


  /**
   * Close test resources.
   */
  @AfterClass(groups = "tttest")
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
  @Test(groups = "tttest", dataProvider = "searchData")
  public void search(final TernaryTree tt, final String word, final boolean expected)
  {
    AssertJUnit.assertEquals(expected, tt.search(word));
  }


  /**
   * @param  tt  Ternary Tree searched for target word.
   * @param  searchTerm  Partial search term.
   * @param  expected  Expected partial search results.
   */
  @Test(groups = "tttest", dataProvider = "partialSearchData")
  public void partialSearch(final TernaryTree tt, final String searchTerm, final String[] expected)
  {
    final String[] actual = tt.partialSearch(searchTerm);
    System.out.println("Partial search results: " + Arrays.toString(actual));
    System.out.println("Partial search expected: " + Arrays.toString(expected));
    Assert.assertEquals(actual, expected);
  }


  /**
   * @param  tt  Ternary Tree searched for target word.
   * @param  word  to search for.
   * @param  distance  for near search
   * @param  expected  Expected partial search results.
   */
  @Test(groups = "tttest", dataProvider = "nearSearchData")
  public void nearSearch(final TernaryTree tt, final String word, final int distance, final String[] expected)
  {
    final String[] actual = tt.nearSearch(word, distance);
    System.out.println("Near search results: " + Arrays.toString(actual));
    System.out.println("Near search expected: " + Arrays.toString(expected));
    Assert.assertEquals(actual, expected);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test to measure search performance on array word lists.
 *
 * @author  Middleware Services
 */
public class ArrayWordListDictionaryPerfTest extends AbstractDictionaryPerfTest
{

  /** dictionary to test. */
  private WordListDictionary wld;

  /** total time for all searches. */
  private long wldSearchTime;


  /**
   * @param  dict1  to load.
   * @param  dict2  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "webFileSorted", "fbsdFileSorted" })
  @BeforeClass(groups = {"wlperftest"})
  public void createDictionary(final String dict1, final String dict2) throws Exception
  {
    super.initialize(dict1, dict2);

    long t = System.currentTimeMillis();
    wld = new WordListDictionary(WordLists.createFromReader(new FileReader[] {new FileReader(webFile)}));
    t = System.currentTimeMillis() - t;
    System.out.println(
      wld.getClass().getSimpleName() + " (" + ArrayWordList.class.getSimpleName() + ") time to construct: " + t + "ms");
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = {"wlperftest"})
  public void closeDictionary()
  {
    System.out.println(
      wld.getClass().getSimpleName() + " (" + ArrayWordList.class.getSimpleName() + ") total search time: " +
      (wldSearchTime / 1000 / 1000) + "ms");
    System.out.println(
      wld.getClass().getSimpleName() + " (" + ArrayWordList.class.getSimpleName() + ") avg time per search: " +
      (wldSearchTime / 10000) + "ns");
    wld = null;
  }


  /**
   * @param  word  to search for
   */
  @Test(groups = {"wlperftest"}, dataProvider = "search-words-web-large")
  public void wordListSearch(final String word)
  {
    wldSearchTime += doSearch(wld, word);
  }
}

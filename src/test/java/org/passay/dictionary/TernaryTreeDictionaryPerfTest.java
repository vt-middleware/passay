/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import java.util.Map;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test to measure search performance on ternary tree dictionaries.
 *
 * @author  Middleware Services
 */
public class TernaryTreeDictionaryPerfTest extends AbstractDictionaryPerfTest
{

  /** dictionary to test. */
  private TernaryTreeDictionary ttd;

  /** total time for all searches. */
  private long ttdSearchTime;


  /**
   * @param  dict1  to load.
   * @param  dict2  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "webFileSorted", "fbsdFileSorted" })
  @BeforeClass(groups = {"ttperftest"})
  public void createDictionary(final String dict1, final String dict2)
    throws Exception
  {
    super.initialize(dict1, dict2);

    long t = System.currentTimeMillis();
    ttd = new TernaryTreeDictionary(new FileWordList(new RandomAccessFile(webFile, "r")));
    t = System.currentTimeMillis() - t;
    System.out.println(ttd.getClass().getSimpleName() + " time to construct: " + t + "ms");
    final Map<Integer, Integer> depths = ttd.getTernaryTree().getNodeStats();
    System.out.println(ttd.getClass().getSimpleName() + " depth histogram: " + depths);
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = {"ttperftest"})
  public void closeDictionary()
  {
    System.out.println(ttd.getClass().getSimpleName() + " search time: " + (ttdSearchTime / 1000 / 1000) + "ms");
    System.out.println(ttd.getClass().getSimpleName() + " avg time per search: " + (ttdSearchTime / 10000) + "ns");
    ttd = null;
  }


  /**
   * @param  word  to search for
   */
  @Test(groups = {"ttperftest"}, dataProvider = "search-words-web-large")
  public void ternaryTreeSearch(final String word)
  {
    ttdSearchTime += doSearch(ttd, word);
  }
}

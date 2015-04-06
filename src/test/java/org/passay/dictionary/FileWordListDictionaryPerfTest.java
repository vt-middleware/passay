/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test to measure search performance on file word lists.
 *
 * @author  Middleware Services
 */
public class FileWordListDictionaryPerfTest extends AbstractDictionaryPerfTest
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
  public void createDictionary(final String dict1, final String dict2)
    throws Exception
  {
    super.initialize(dict1, dict2);

    long t = System.currentTimeMillis();
    wld = new WordListDictionary(
      new FileWordList(new RandomAccessFile(webFile, "r")));
    t = System.currentTimeMillis() - t;
    System.out.println(
      wld.getClass().getSimpleName() + " (" +
      FileWordList.class.getSimpleName() + ") time to construct: " + t + "ms");
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"wlperftest"})
  public void closeDictionary()
    throws Exception
  {
    System.out.println(
      wld.getClass().getSimpleName() + " (" +
      FileWordList.class.getSimpleName() + ") search time: " +
      (wldSearchTime / 1000 / 1000) + "ms");
    System.out.println(
      wld.getClass().getSimpleName() + " (" +
      FileWordList.class.getSimpleName() + ") avg time per search: " +
      (wldSearchTime / 1000 / 1000 / 10) + "ms");
    wld = null;
  }


  /**
   * @param  word  to search for
   *
   * @throws  Exception  On test failure.
   */
  @Test(
    groups = {"wlperftest"},
    dataProvider = "search-words-web-small")
  public void wordListSearch(final String word)
    throws Exception
  {
    wldSearchTime += doSearch(wld, word);
  }
}

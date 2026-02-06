/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
   * @throws  Exception  On test failure.
   */
  @BeforeClass
  public void createDictionary() throws Exception
  {
    super.initialize(Dictionaries.WEB_FILE_SORTED, Dictionaries.FREEBSD_FILE_SORTED);

    long t = System.currentTimeMillis();
    wld = new WordListDictionary(new FileWordList(new RandomAccessFile(webFile, "r")));
    t = System.currentTimeMillis() - t;
    System.out.println(
      wld.getClass().getSimpleName() + " (" + FileWordList.class.getSimpleName() + ") time to construct: " + t + "ms");
  }

  /**
   * Close test resources.
   */
  @AfterClass
  public void closeDictionary()
  {
    System.out.println(
      wld.getClass().getSimpleName() + " (" + FileWordList.class.getSimpleName() + ") total search time: " +
      (wldSearchTime / 1000 / 1000) + "ms");
    System.out.println(
      wld.getClass().getSimpleName() + " (" + FileWordList.class.getSimpleName() + ") avg time per search: " +
      (wldSearchTime / 10000) + "ns");
    wld = null;
  }


  /**
   * @param  word  to search for
   */
  @Test(dataProvider = "search-words-web-small")
  public void wordListSearch(final String word)
  {
    wldSearchTime += doSearch(wld, word);
  }
}

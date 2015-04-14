/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link WordListDictionary} that uses a {@link FileWordList}.
 *
 * @author  Middleware Services
 */
public class FileWordListDictionaryTest extends AbstractDictionaryTest
{

  /** Test dictionary. */
  private WordListDictionary caseSensitive;

  /** Test dictionary. */
  private WordListDictionary caseInsensitive;


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"wldicttest"})
  public void createDictionary()
    throws Exception
  {
    final FileWordList fwl1 = new FileWordList(new RandomAccessFile(fbsdFileSorted, "r"), true, 25);
    caseSensitive = new WordListDictionary(fwl1);

    final FileWordList fwl2 = new FileWordList(new RandomAccessFile(fbsdFileLowerCaseSorted, "r"), false, 25);
    caseInsensitive = new WordListDictionary(fwl2);
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"wldicttest"})
  public void closeDictionary()
    throws Exception
  {
    caseSensitive = null;
    caseInsensitive = null;
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"wldicttest"})
  public void search()
    throws Exception
  {
    AssertJUnit.assertTrue(caseSensitive.search("TrustedBSD"));
    AssertJUnit.assertFalse(caseSensitive.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(caseInsensitive.search("TrustedBSD"));
    AssertJUnit.assertFalse(caseInsensitive.search(FALSE_SEARCH));
  }


  /**
   * @param  word  to search for.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wldicttest"}, dataProvider = "all-fbsd-words")
  public void searchAll(final String word)
    throws Exception
  {
    AssertJUnit.assertTrue(caseSensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toLowerCase()));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toUpperCase()));
  }
}

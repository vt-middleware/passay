/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link WordListDictionary} that uses a {@link ArrayWordList}.
 *
 * @author  Middleware Services
 */
public class ArrayWordListDictionaryTest extends AbstractDictionaryTest
{

  /** Test dictionary. */
  private WordListDictionary caseSensitive;

  /** Test dictionary. */
  private WordListDictionary caseInsensitive;


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = "wldicttest")
  public void createDictionary() throws Exception
  {
    final ArrayWordList awl1 = WordLists.createFromReader(
      new FileReader[] {new FileReader(fbsdFile)},
      true,
      new ArraysSort());
    caseSensitive = new WordListDictionary(awl1);

    final ArrayWordList awl2 = WordLists.createFromReader(
      new FileReader[] {new FileReader(fbsdFile)},
      false,
      new ArraysSort());
    caseInsensitive = new WordListDictionary(awl2);
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = "wldicttest")
  public void closeDictionary()
  {
    caseSensitive = null;
    caseInsensitive = null;
  }


  /**
   * Test search.
   */
  @Test(groups = "wldicttest")
  public void search()
  {
    AssertJUnit.assertTrue(caseSensitive.search("TrustedBSD"));
    AssertJUnit.assertFalse(caseSensitive.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(caseInsensitive.search("TrustedBSD"));
    AssertJUnit.assertFalse(caseInsensitive.search(FALSE_SEARCH));
  }


  /**
   * @param  word  to search for.
   */
  @Test(groups = "wldicttest", dataProvider = "all-fbsd-words")
  public void searchAll(final String word)
  {
    AssertJUnit.assertTrue(caseSensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toLowerCase()));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toUpperCase()));
  }
}

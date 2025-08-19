/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @BeforeClass(groups = "wldicttest")
  public void createDictionary() throws Exception
  {
    final FileWordList fwl1 = new FileWordList(new RandomAccessFile(fbsdFileSorted, "r"), true, 25);
    caseSensitive = new WordListDictionary(fwl1);

    final FileWordList fwl2 = new FileWordList(new RandomAccessFile(fbsdFileLowerCaseSorted, "r"), false, 25);
    caseInsensitive = new WordListDictionary(fwl2);
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
    assertThat(caseSensitive.search("TrustedBSD")).isTrue();
    assertThat(caseSensitive.search(FALSE_SEARCH)).isFalse();
    assertThat(caseInsensitive.search("TrustedBSD")).isTrue();
    assertThat(caseInsensitive.search(FALSE_SEARCH)).isFalse();
  }


  /**
   * @param  word  to search for.
   */
  @Test(groups = "wldicttest", dataProvider = "all-fbsd-words")
  public void searchAll(final String word)
  {
    assertThat(caseSensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word.toLowerCase())).isTrue();
    assertThat(caseInsensitive.search(word.toUpperCase())).isTrue();
  }
}

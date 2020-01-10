/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link WordLists}.
 *
 * @author  Middleware Services
 */
public class WordListsTest
{

  /** Case sensitive word list. */
  private ArrayWordList caseSensitiveWordList;

  /** Case insensitive word list. */
  private ArrayWordList caseInsensitiveWordList;


  /**
   * @param  file1  dictionary to load.
   * @param  file2  dictionary to load.
   *
   * @throws  Exception  On word list creation.
   */
  @Parameters({ "fbsdFile", "webFile" })
  @BeforeClass(groups = "wltest")
  public void createWordLists(final String file1, final String file2) throws Exception
  {
    caseSensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(file1)},
      true,
      new ArraysSort());

    caseInsensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(file2)},
      false,
      new ArraysSort());
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = "wltest")
  public void closeWordLists()
  {
    caseSensitiveWordList = null;
    caseInsensitiveWordList = null;
  }


  /**
   * @return  Test data for creating key pair entries.
   */
  @DataProvider(name = "searchData")
  public Object[][] createTestData()
  {
    final ArrayWordList oneWord = new ArrayWordList(new String[] {"a"});
    final ArrayWordList twoWords = new ArrayWordList(new String[] {"a", "b"});
    final ArrayWordList threeWords = new ArrayWordList(new String[] {"a", "b", "c"});
    return
      new Object[][] {
        {oneWord, "a", 0},
        {oneWord, "b", WordLists.NOT_FOUND},
        {twoWords, "a", 0},
        {twoWords, "b", 1},
        {twoWords, "c", WordLists.NOT_FOUND},
        {threeWords, "a", 0},
        {threeWords, "b", 1},
        {threeWords, "c", 2},
        {threeWords, "d", WordLists.NOT_FOUND},
        {caseSensitiveWordList, "ISBN", 76},
        {caseSensitiveWordList, "guacamole", WordLists.NOT_FOUND},
        {caseInsensitiveWordList, "irresolute", 98323},
        {caseInsensitiveWordList, "brujo", WordLists.NOT_FOUND},
      };
  }


  /**
   * Test for {@link WordLists#binarySearch(WordList, String)}.
   *
   * @param  wl  Test word list.
   * @param  word  Word to search for.
   * @param  expectedResult  Expected result of test.
   */
  @Test(groups = "wltest", dataProvider = "searchData")
  public void binarySearch(final WordList wl, final String word, final int expectedResult)
  {
    AssertJUnit.assertEquals(expectedResult, WordLists.binarySearch(wl, word));
  }


  /**
   * Test for {@link WordLists#createFromReader(Reader[])}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = "wltest")
  public void createFromReader() throws Exception
  {
    // sorted list of words
    final String[] words = {
      " leading whitespace",
      " surrounding whitespace ",
      "bar",
      "foo",
      "trailing whitespace ",
    };
    final StringBuilder sb = new StringBuilder();
    for (String word : words) {
      sb.append(word).append("\n");
    }

    final ArrayWordList list = WordLists.createFromReader(
      new StringReader[] {new StringReader(sb.toString())},
      true,
      new ArraysSort());
    for (int i = 0; i < list.size(); i++) {
      AssertJUnit.assertEquals(words[i], list.get(i));
    }
  }

  /**
   * Test for {@link WordLists#readWords(Reader, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = "wltest")
  public void testReadWordsFromReader() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign");
         Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
      WordLists.readWords(reader, words);
    }
    AssertJUnit.assertTrue(words.contains("good"));
  }


  /**
   * Test for {@link WordLists#readWords(InputStream, String, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = "wltest")
  public void testReadWordsFromStream() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign")) {
      WordLists.readWords(in, "UTF-8", words);
    }
    AssertJUnit.assertTrue(words.contains("good"));
  }

  /**
   * Test for {@link WordLists#readZippedWords(InputStream, String, String, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = "wltest")
  public void testReadWordsFromZippedStream() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign.zip")) {
      WordLists.readZippedWords(in, "UTF-8", null, words);
    }
    // check that words are read from all files
    AssertJUnit.assertTrue(words.contains("good"));
    AssertJUnit.assertTrue(words.contains("newbies"));

    words.clear();
    try (InputStream in = getClass().getResourceAsStream("/eign.zip")) {
      WordLists.readZippedWords(in, "UTF-8", "ei.*", words);
    }
    // check that words are read only from matches files
    AssertJUnit.assertTrue(words.contains("good"));
    AssertJUnit.assertFalse(words.contains("newbies"));
  }
}

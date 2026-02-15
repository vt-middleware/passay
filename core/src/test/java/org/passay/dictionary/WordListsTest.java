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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
   * @throws  Exception  On word list creation.
   */
  @BeforeClass
  public void createWordLists() throws Exception
  {
    caseSensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(Dictionaries.FREEBSD_FILE)},
      true,
      new ArraysSort());

    caseInsensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(Dictionaries.WEB_FILE)},
      false,
      new ArraysSort());
  }

  /**
   * Close test resources.
   */
  @AfterClass
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
        {oneWord, "b", -1},
        {twoWords, "a", 0},
        {twoWords, "b", 1},
        {twoWords, "c", -1},
        {threeWords, "a", 0},
        {threeWords, "b", 1},
        {threeWords, "c", 2},
        {threeWords, "d", -1},
        {caseSensitiveWordList, "ISBN", 76},
        {caseSensitiveWordList, "guacamole", -1},
        {caseInsensitiveWordList, "irresolute", 98323},
        {caseInsensitiveWordList, "brujo", -1},
      };
  }


  /**
   * Test for {@link WordLists#binarySearch(WordList, CharSequence)}.
   *
   * @param  wl  Test word list.
   * @param  word  Word to search for.
   * @param  expectedResult  Expected result of test.
   */
  @Test(dataProvider = "searchData")
  public void binarySearch(final WordList wl, final String word, final int expectedResult)
  {
    assertThat(WordLists.binarySearch(wl, word)).isEqualTo(expectedResult);
  }


  /**
   * Test for {@link WordLists#createFromReader(Reader[])}.
   *
   * @throws  Exception  On test failure.
   */
  @Test
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
      assertThat(list.get(i)).isEqualTo(words[i]);
    }
  }

  /**
   * Test for {@link WordLists#readWords(Reader, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test
  public void testReadWordsFromReader() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign");
         Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
      WordLists.readWords(reader, words);
    }
    assertThat(words.contains("good")).isTrue();
  }


  /**
   * Test for {@link WordLists#readWords(InputStream, String, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test
  public void testReadWordsFromStream() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign")) {
      WordLists.readWords(in, "UTF-8", words);
    }
    assertThat(words.contains("good")).isTrue();
  }

  /**
   * Test for {@link WordLists#readZippedWords(InputStream, String, String, List)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test
  public void testReadWordsFromZippedStream() throws Exception
  {
    final List<String> words = new ArrayList<>();
    try (InputStream in = getClass().getResourceAsStream("/eign.zip")) {
      WordLists.readZippedWords(in, "UTF-8", null, words);
    }
    // check that words are read from all files
    assertThat(words.contains("good")).isTrue();
    assertThat(words.contains("newbies")).isTrue();

    words.clear();
    try (InputStream in = getClass().getResourceAsStream("/eign.zip")) {
      WordLists.readZippedWords(in, "UTF-8", "ei.*", words);
    }
    // check that words are read only from matches files
    assertThat(words.contains("good")).isTrue();
    assertThat(words.contains("newbies")).isFalse();
  }
}

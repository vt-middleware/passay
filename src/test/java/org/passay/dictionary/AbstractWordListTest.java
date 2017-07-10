/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Common unit tests for {@link WordList} implementations.
 *
 * @param  <T>  Type of word list under test.
 *
 * @author  Middleware Services
 */
public abstract class AbstractWordListTest<T extends WordList>
{
  /** Tracks word lists that have been created to facilitate test cleanup. */
  private List<T> wordLists = new ArrayList<>();


  /**
   * Word lists with expected size and words.
   *
   * @return  Array of word list, size, and array of expected words.
   *
   * @throws IOException  on file I/O errors.
   */
  @SuppressWarnings("unchecked")
  @DataProvider(name = "wordListsWithExpectedWords")
  public Object[][] provideWordListsWithExpectedWords() throws IOException
  {
    final Object[][] parameters = new Object[][] {
      new Object[] {
        createWordList("src/test/resources/dict-enUS.txt", true),
        48029,
        new ExpectedWord[] {
          new ExpectedWord("A", 0),
          new ExpectedWord("AA", 1),
          new ExpectedWord("Bernanke", 1076),
          new ExpectedWord("clammily", 16264),
          new ExpectedWord("clamminess", 16265),
          new ExpectedWord("exponential", 22000),
          new ExpectedWord("maple", 30256),
          new ExpectedWord("zymurgy", 48028),
        },
      },
      new Object[] {
        createWordList("src/test/resources/dict-frFR.txt", true),
        73424,
        new ExpectedWord[] {
          new ExpectedWord("A", 0),
          new ExpectedWord("Carol", 990),
          new ExpectedWord("caoutchouc", 15866),
          new ExpectedWord("peinture", 50303),
          new ExpectedWord("retrouvaille", 58153),
          new ExpectedWord("yaourt", 70997),
          new ExpectedWord("œuvée", 73423),
        },
      },
      new Object[] {
        createWordList("src/test/resources/dict-frFR-cr.txt", true),
        73424,
        new ExpectedWord[] {
          new ExpectedWord("A", 0),
          new ExpectedWord("Carol", 990),
          new ExpectedWord("caoutchouc", 15866),
          new ExpectedWord("peinture", 50303),
          new ExpectedWord("retrouvaille", 58153),
          new ExpectedWord("yaourt", 70997),
          new ExpectedWord("œuvée", 73423),
        },
      },
      new Object[] {
        createWordList("src/test/resources/dict-viVN.txt", true),
        6634,
        new ExpectedWord[] {
          new ExpectedWord("a", 0),
          new ExpectedWord("ai", 1),
          new ExpectedWord("giội", 1361),
          new ExpectedWord("giộp", 1362),
          new ExpectedWord("mướt", 2763),
          new ExpectedWord("mười", 2764),
          new ExpectedWord("mường", 2765),
          new ExpectedWord("ực", 6632),
          new ExpectedWord("ỷ", 6633),
        },
      },
      new Object[] {
        createWordList("src/test/resources/dict-viVN-crlf.txt", true),
        6634,
        new ExpectedWord[] {
          new ExpectedWord("a", 0),
          new ExpectedWord("ai", 1),
          new ExpectedWord("giội", 1361),
          new ExpectedWord("giộp", 1362),
          new ExpectedWord("mướt", 2763),
          new ExpectedWord("mười", 2764),
          new ExpectedWord("mường", 2765),
          new ExpectedWord("ực", 6632),
          new ExpectedWord("ỷ", 6633),
        },
      },
    };
    for (Object[] parameter : parameters) {
      wordLists.add((T) parameter[0]);
    }
    return parameters;
  }


  /**
   * Short word lists.
   *
   * @return  Array of word list.
   *
   * @throws IOException  on file I/O errors.
   */
  @SuppressWarnings("unchecked")
  @DataProvider(name = "shortWordLists")
  public Object[][] provideShortWordLists() throws IOException
  {
    final Object[][] parameters = new Object[][] {
      new Object[] {createWordList("src/test/resources/freebsd.sort", true)},
      new Object[] {createWordList("src/test/resources/freebsd.lc.sort", false)},
    };
    for (Object[] parameter : parameters) {
      wordLists.add((T) parameter[0]);
    }
    return parameters;
  }


  /**
   * Exercises reading words from a {@link WordList}.
   *
   * @param  list  Word list to test.
   * @param  expectedSize  Expected size of the word list.
   * @param  expectedWords  Test list of words to get and compare to expected result.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"}, dataProvider = "wordListsWithExpectedWords")
  public void get(final T list, final int expectedSize, final ExpectedWord ... expectedWords) throws Exception
  {
    AssertJUnit.assertEquals(expectedSize, list.size());

    try {
      list.get(-1);
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      AssertJUnit.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException, threw " + e.getMessage());
    }

    for (ExpectedWord expectedWord : expectedWords) {
      AssertJUnit.assertEquals(expectedWord.word, list.get(expectedWord.index));
    }

    try {
      list.get(expectedSize);
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      AssertJUnit.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException, threw " + e.getMessage());
    }
  }


  /**
   * Test for {@link WordList#iterator()}.
   *
   * @param  list  Word list to test.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"}, dataProvider = "shortWordLists")
  public void iterator(final T list) throws Exception
  {
    final Iterator<String> i = list.iterator();
    int index = 0;
    while (i.hasNext()) {
      final String s = i.next();
      AssertJUnit.assertEquals(list.get(index), s);
      index++;
    }
  }


  /**
   * Test for {@link WordList#medianIterator()}.
   *
   * @param  list  Word list to test.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"}, dataProvider = "shortWordLists")
  public void medianIterator(final T list) throws Exception
  {
    final Iterator<String> i = list.medianIterator();
    int index = list.size() / 2;
    int count = 0;
    while (i.hasNext()) {
      final String s = i.next();
      AssertJUnit.assertEquals(list.get(index), s);
      count++;
      if (count % 2 == 0) {
        index = index + count;
      } else {
        index = index - count;
      }
    }
  }


  /**
   * Creates a word list from that contains the words in the given file.
   *
   * @param  filePath  Path to file containing words.
   * @param  caseSensitive  True to create case-sensitive word list, false otherwise.
   *
   * @return  Word list.
   *
   * @throws  IOException  On I/O errors opening and initializing word list.
   */
  protected abstract T createWordList(String filePath, boolean caseSensitive) throws IOException;


  /**
   * Attempts to close word lists that have a close method.
   *
   * @throws  Exception  On test failure.
   */
  @AfterClass(groups = {"wltest"})
  public void cleanUp() throws Exception
  {
    for (T list : wordLists) {
      if (list != null) {
        try {
          final Method closeMethod = list.getClass().getMethod("close");
          closeMethod.invoke(list);
        } catch (NoSuchMethodException e) {
          continue;
        }
      }
    }
  }


  /** Expected result from {@link WordList#get(int)}. */
  static class ExpectedWord
  {
    // CheckStyle:VisibilityModifier OFF
    /** Expected word. */
    String word;

    /** Expected index. */
    int index;
    // CheckStyle:VisibilityModifier ON


    /**
     * Creates a new instance with a word and index where the word should appear in the word list.
     *
     * @param  s  Expected word.
     * @param  i  Expected index.
     */
    ExpectedWord(final String s, final int i)
    {
      word = s;
      index = i;
    }
  }
}

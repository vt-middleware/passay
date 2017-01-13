/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Iterator;
import org.testng.AssertJUnit;
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

  /** False contains. */
  public static final String FALSE_CONTAINS = "not-found-in-the-list";

  /** True contains. */
  public static final String TRUE_CONTAINS = "LinuxDoc";

  /** True contains index. */
  public static final int TRUE_CONTAINS_INDEX = 103;

  /** Test list. */
  protected T wordList;


  /**
   * Exercises reading words from {@link FileWordList}.
   *
   * @param  list  word list to test.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"}, dataProvider = "wordLists")
  public void get(final T list)
    throws Exception
  {
    AssertJUnit.assertEquals(26, list.size());

    try {
      list.get(-1);
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      AssertJUnit.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException, threw " + e.getMessage());
    }
    AssertJUnit.assertEquals("Alpha", list.get(0));
    AssertJUnit.assertEquals("Bravo", list.get(1));
    AssertJUnit.assertEquals("Charlie", list.get(2));
    AssertJUnit.assertEquals("Delta", list.get(3));
    AssertJUnit.assertEquals("Echo", list.get(4));
    AssertJUnit.assertEquals("Foxtrot", list.get(5));
    AssertJUnit.assertEquals("Mike", list.get(12));
    AssertJUnit.assertEquals("November", list.get(13));
    AssertJUnit.assertEquals("Yankee", list.get(24));
    AssertJUnit.assertEquals("Zulu", list.get(25));
    try {
      list.get(26);
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
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"})
  public void iterator()
    throws Exception
  {
    final Iterator<String> i = wordList.iterator();
    int index = 0;
    while (i.hasNext()) {
      final String s = i.next();
      AssertJUnit.assertEquals(wordList.get(index), s);
      index++;
    }
  }


  /**
   * Test for {@link WordList#medianIterator()}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"})
  public void medianIterator()
    throws Exception
  {
    final Iterator<String> i = wordList.medianIterator();
    int index = wordList.size() / 2;
    int count = 0;
    while (i.hasNext()) {
      final String s = i.next();
      AssertJUnit.assertEquals(wordList.get(index), s);
      count++;
      if (count % 2 == 0) {
        index = index + count;
      } else {
        index = index - count;
      }
    }
  }
}

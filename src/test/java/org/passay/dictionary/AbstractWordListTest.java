/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Iterator;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Common unit tests for {@link WordList} implementations.
 *
 * @author  Middleware Services
 */
public abstract class AbstractWordListTest
{

  /** False contains. */
  public static final String FALSE_CONTAINS = "not-found-in-the-list";

  /** True contains. */
  public static final String TRUE_CONTAINS = "LinuxDoc";

  /** True contains index. */
  public static final int TRUE_CONTAINS_INDEX = 103;

  /** Test list. */
  protected WordList wordList;


  /**
   * Test for {@link WordList#get(int)}.
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"})
  public void get()
    throws Exception
  {
    try {
      wordList.get(-1);
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      AssertJUnit.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    } catch (Exception e) {
      AssertJUnit.fail(
        "Should have thrown IndexOutOfBoundsException, threw " +
        e.getMessage());
    }

    try {
      wordList.get(282);
      AssertJUnit.fail("Should have thrown IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      AssertJUnit.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    } catch (Exception e) {
      AssertJUnit.fail(
        "Should have thrown IndexOutOfBoundsException, threw " +
        e.getMessage());
    }

    AssertJUnit.assertEquals("ABI", wordList.get(0));
    AssertJUnit.assertEquals("MIPS", wordList.get(107));
    AssertJUnit.assertEquals("website", wordList.get(281));
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

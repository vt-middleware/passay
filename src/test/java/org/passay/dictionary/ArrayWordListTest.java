/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.util.Arrays;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ArrayWordList}.
 *
 * @author  Middleware Services
 */
public class ArrayWordListTest extends AbstractWordListTest
{


  /**
   * @param  file  dictionary to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("fbsdFileSorted")
  @BeforeClass(groups = {"wltest"})
  public void createWordList(final String file)
    throws Exception
  {
    wordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(file)});
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"wltest"})
  public void closeWordList()
    throws Exception
  {
    wordList = null;
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"wltest"})
  public void construt()
    throws Exception
  {
    try {
      new ArrayWordList(null, true);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail(
        "Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    final String[] arrayWithNull = new String[] {"a", "b", null, "c"};
    try {
      new ArrayWordList(arrayWithNull, true);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail(
        "Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"wltest"})
  public void wordsWithSpace()
    throws Exception
  {
    final String[] arrayWithSpaces = new String[] {
      " Man",
      " cadet",
      "!@#$%^&*",
      "password",
      "inner ",
      "outer ",
    };
    Arrays.sort(arrayWithSpaces);

    final ArrayWordList wl = new ArrayWordList(arrayWithSpaces, true);
    AssertJUnit.assertEquals(arrayWithSpaces.length, wl.size());
    AssertJUnit.assertEquals(arrayWithSpaces[0], wl.get(0));
    AssertJUnit.assertEquals(
      arrayWithSpaces[arrayWithSpaces.length - 1],
      wl.get(wl.size() - 1));
  }
}

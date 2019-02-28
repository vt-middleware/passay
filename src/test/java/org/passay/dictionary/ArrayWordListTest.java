/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ArrayWordList}.
 *
 * @author  Middleware Services
 */
public class ArrayWordListTest extends AbstractWordListTest<ArrayWordList>
{

  @Override
  protected ArrayWordList createWordList(final String filePath, final boolean caseSensitive) throws IOException
  {
    return WordLists.createFromReader(new FileReader[] {new FileReader(filePath)}, caseSensitive);
  }


  /**
   * Test construct.
   */
  @Test(groups = {"wltest"})
  public void construct()
  {
    try {
      new ArrayWordList(null, true);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    final String[] arrayWithNull = {"a", "b", null, "c"};
    try {
      new ArrayWordList(arrayWithNull, true);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }
  }

  /**
   * Test words with space.
   */
  @Test(groups = {"wltest"})
  public void wordsWithSpace()
  {
    final String[] arrayWithSpaces = {
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
    AssertJUnit.assertEquals(arrayWithSpaces[arrayWithSpaces.length - 1], wl.get(wl.size() - 1));
  }
}

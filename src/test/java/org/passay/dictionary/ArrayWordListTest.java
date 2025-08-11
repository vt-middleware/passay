/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @Test(groups = "wltest")
  public void construct()
  {
    try {
      new ArrayWordList(null, true);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }

    final String[] arrayWithNull = {"a", "b", null, "c"};
    try {
      new ArrayWordList(arrayWithNull, true);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }
  }

  /**
   * Test words with space.
   */
  @Test(groups = "wltest")
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
    assertThat(wl.size()).isEqualTo(arrayWithSpaces.length);
    assertThat(wl.get(0)).isEqualTo(arrayWithSpaces[0]);
    assertThat(wl.get(wl.size() - 1)).isEqualTo(arrayWithSpaces[arrayWithSpaces.length - 1]);
  }
}

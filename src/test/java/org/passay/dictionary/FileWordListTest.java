/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link FileWordList}.
 *
 * @author  Middleware Services
 */
public class FileWordListTest extends AbstractWordListTest<FileWordList>
{
  /** Current cache percent. */
  private int cachePercent;


  @Override
  protected FileWordList createWordList(final String filePath, final boolean caseSensitive) throws IOException
  {
    final FileWordList list = new FileWordList(
        new RandomAccessFile(filePath, "r"), caseSensitive, Math.min(cachePercent, 100));
    cachePercent *= 3 / 2;
    return list;
  }


  /**
   * @param  file1  dictionary to load.
   * @param  file2  dictionary to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "fbsdFileSorted", "fbsdFileLowerCaseSorted" })
  @Test(groups = "wltest")
  public void construct(final String file1, final String file2) throws Exception
  {
    try {
      new FileWordList(new RandomAccessFile(file1, "r"), true, -1);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }

    try {
      new FileWordList(new RandomAccessFile(file1, "r"), true, 100 + 1);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }

    FileWordList fwl = new FileWordList(new RandomAccessFile(file1, "r"), true, 0);
    fwl.close();

    fwl = new FileWordList(new RandomAccessFile(file2, "r"), false, 0);
    fwl.close();
  }


  /**
   * @param  file  dictionary to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("eignFileSorted")
  @Test(groups = "wltest")
  public void smallFileCache(final String file) throws Exception
  {
    new FileWordList(new RandomAccessFile(file, "r"), true, 1);
  }
}

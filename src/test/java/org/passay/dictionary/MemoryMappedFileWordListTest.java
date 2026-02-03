/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link MemoryMappedFileWordList}.
 *
 * @author  Middleware Services
 */
public class MemoryMappedFileWordListTest extends AbstractWordListTest<MemoryMappedFileWordList>
{
  /** Current cache percent. */
  private int cachePercent;


  @Override
  protected MemoryMappedFileWordList createWordList(final String filePath, final boolean caseSensitive)
    throws IOException
  {
    final MemoryMappedFileWordList list = new MemoryMappedFileWordList(
      new RandomAccessFile(filePath, "r"), caseSensitive, Math.min(cachePercent, 100));
    cachePercent *= 3 / 2;
    return list;
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test
  public void construct() throws Exception
  {
    try {
      new MemoryMappedFileWordList(new RandomAccessFile("src/test/resources/freebsd.sort", "r"), true, -1);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }

    try {
      new MemoryMappedFileWordList(new RandomAccessFile("src/test/resources/freebsd.sort", "r"), true, 100 + 1);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    } catch (Exception e) {
      fail("Should have thrown IllegalArgumentException, threw %s", e.getMessage());
    }

    MemoryMappedFileWordList fwl = new MemoryMappedFileWordList(
      new RandomAccessFile("src/test/resources/freebsd.sort", "r"), true, 0);
    fwl.close();

    fwl = new MemoryMappedFileWordList(new RandomAccessFile("src/test/resources/freebsd.lc.sort", "r"), false, 0);
    fwl.close();
  }
}

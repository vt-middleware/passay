/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link MemoryMappedFileWordList}.
 *
 * @author  Middleware Services
 */
public class MemoryMappedFileWordListTest extends AbstractWordListTest
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
    wordList = new MemoryMappedFileWordList(new RandomAccessFile(file, "r"));
    AssertJUnit.assertEquals(282, wordList.size());
    AssertJUnit.assertEquals("DVD", wordList.get(42));
    AssertJUnit.assertEquals("UID", wordList.get(199));
  }


  /**
   * Test for {@link MemoryMappedFileWordList#close()}.
   *
   * @throws  Exception  On test failure.
   */
  @AfterClass(groups = {"wltest"})
  public void closeWordList()
    throws Exception
  {
    AssertJUnit.assertTrue(((MemoryMappedFileWordList) wordList).getFile().getFD().valid());
    ((MemoryMappedFileWordList) wordList).close();
    AssertJUnit.assertFalse(((MemoryMappedFileWordList) wordList).getFile().getFD().valid());
  }


  /**
   * @param  file1  dictionary to load.
   * @param  file2  dictionary to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "fbsdFileSorted", "fbsdFileLowerCaseSorted" })
  @Test(groups = {"wltest"})
  public void construct(final String file1, final String file2)
    throws Exception
  {
    try {
      new MemoryMappedFileWordList(new RandomAccessFile(file1, "r"), true, -1);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    try {
      new MemoryMappedFileWordList(new RandomAccessFile(file1, "r"), true, 101);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    MemoryMappedFileWordList fwl = new MemoryMappedFileWordList(new RandomAccessFile(file1, "r"), true, 0);
    fwl.close();

    fwl = new MemoryMappedFileWordList(new RandomAccessFile(file2, "r"), false, 0);
    fwl.close();
  }
}

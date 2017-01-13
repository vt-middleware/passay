/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link FileWordList}.
 *
 * @author  Middleware Services
 */
public class FileWordListTest extends AbstractWordListTest<FileWordList>
{
  /** Word list backed by file with Unix line endings. */
  private FileWordList unixWordList;

  /** Word list backed by file with Mac line endings. */
  private FileWordList macWordList;

  /** Word list backed by file with DOS line endings. */
  private FileWordList dosWordList;

  /**
   * @param  file  path to word list file
   * @param  unixFile  path to word list file with unix line endings
   * @param  macFile  path to word list file with mac line endings
   * @param  dosFile  path to word list file with dos line endings
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "fbsdFileSorted", "newLinesUnix", "newLinesMac", "newLinesDos" })
  @BeforeClass(groups = {"wltest"})
  public void createWordLists(final String file, final String unixFile, final String macFile, final String dosFile)
    throws Exception
  {
    wordList = new FileWordList(new RandomAccessFile(file, "r"));
    unixWordList = new FileWordList(new RandomAccessFile(unixFile, "r"), false, 0);
    macWordList = new FileWordList(new RandomAccessFile(macFile, "r"), false, 50);
    dosWordList = new FileWordList(new RandomAccessFile(dosFile, "r"), true, 100);
  }


  /**
   * Test for {@link FileWordList#close()}.
   *
   * @throws  Exception  On test failure.
   */
  @AfterClass(groups = {"wltest"})
  public void closeWordList()
    throws Exception
  {
    final FileWordList[] lists = {wordList, unixWordList, macWordList, dosWordList};
    for (FileWordList list : lists) {
      AssertJUnit.assertTrue(list.getFile().getFD().valid());
      list.close();
      AssertJUnit.assertFalse(list.getFile().getFD().valid());
    }
  }


  /**
   * Create test parameters.
   *
   * @return  Array of MemoryMappedFileWordListTest.
   *
   * @throws IOException  on file I/O errors.
   */
  @DataProvider(name = "wordLists")
  public Object[][] getWordLists()
    throws IOException
  {
    return new Object[][] {
      new Object[] {unixWordList},
      new Object[] {macWordList},
      new Object[] {dosWordList},
    };
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
      new FileWordList(new RandomAccessFile(file1, "r"), true, -1);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    try {
      new FileWordList(new RandomAccessFile(file1, "r"), true, 101);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      AssertJUnit.assertEquals(e.getClass(), IllegalArgumentException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown IllegalArgumentException, threw " + e.getMessage());
    }

    FileWordList fwl = new FileWordList(new RandomAccessFile(file1, "r"), true, 0);
    fwl.close();

    fwl = new FileWordList(new RandomAccessFile(file2, "r"), false, 0);
    fwl.close();
  }
}

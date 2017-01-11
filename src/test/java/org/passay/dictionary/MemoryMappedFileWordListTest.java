/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link MemoryMappedFileWordList}.
 *
 * @author  Middleware Services
 */
public class MemoryMappedFileWordListTest extends AbstractWordListTest
{

  /** File with unix new lines. */
  private FileChannel unixFileChannel;

  /** File with mac new lines. */
  private FileChannel macFileChannel;

  /** File with dos new lines. */
  private FileChannel dosFileChannel;


  /**
   * @param  file  dictionary to load.
   * @param  unixFile  file with unix line endings
   * @param  macFile  file with mac line endings
   * @param  dosFile  file with dos line endings
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "fbsdFileSorted", "newLinesUnix", "newLinesMac", "newLinesDos" })
  @BeforeClass(groups = {"wltest"})
  public void createWordList(final String file, final String unixFile, final String macFile, final String dosFile)
    throws Exception
  {
    wordList = new MemoryMappedFileWordList(new RandomAccessFile(file, "r"));
    AssertJUnit.assertEquals(282, wordList.size());
    AssertJUnit.assertEquals("DVD", wordList.get(42));
    AssertJUnit.assertEquals("UID", wordList.get(199));

    unixFileChannel = new RandomAccessFile(unixFile, "r").getChannel();
    macFileChannel = new RandomAccessFile(macFile, "r").getChannel();
    dosFileChannel = new RandomAccessFile(dosFile, "r").getChannel();
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

    unixFileChannel.close();
    macFileChannel.close();
    dosFileChannel.close();
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


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "newlineFiles")
  public Object[][] newlineFiles()
    throws Exception
  {
    return
      new Object[][] {
        {unixFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, unixFileChannel.size()), },
        {macFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, macFileChannel.size()), },
        {dosFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, dosFileChannel.size()), },
      };
  }


  /**
   * @param  buffer  to read lines from
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"wltest"}, dataProvider = "newlineFiles")
  public void readLine(final MappedByteBuffer buffer)
    throws Exception
  {
    AssertJUnit.assertEquals("This is the first line", MemoryMappedFileWordList.readLine(buffer));
    AssertJUnit.assertEquals("", MemoryMappedFileWordList.readLine(buffer));
    AssertJUnit.assertEquals("This is the second to last line", MemoryMappedFileWordList.readLine(buffer));
    AssertJUnit.assertEquals("", MemoryMappedFileWordList.readLine(buffer));
    // buffer should be empty
    AssertJUnit.assertNull(MemoryMappedFileWordList.readLine(buffer));
  }
}

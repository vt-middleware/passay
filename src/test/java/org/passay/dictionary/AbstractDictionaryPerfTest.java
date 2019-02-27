/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

/**
 * Base class for dictionary performance tests.
 *
 * @author  Middleware Services
 */
public abstract class AbstractDictionaryPerfTest
{

  /** location of webster's dictionary. */
  protected static String webFile;

  /** location of freebsd dictionary. */
  protected static String fbsdFile;

  /** Initialization lock. */
  private static final Object LOCK = new Object();

  /** store a large array of random words from {@link #webFile}. */
  private static Object[][] randomWebWordsLarge;

  /** store a small array of random words from {@link #webFile}. */
  private static Object[][] randomWebWordsSmall;

  /** store a large array of random words from {@link #fbsdFile}. */
  private static Object[][] randomFbsdWordsLarge;

  /** store a small array of random words from {@link #fbsdFile}. */
  private static Object[][] randomFbsdWordsSmall;


  /**
   * @param  dict1  to load.
   * @param  dict2  to load.
   *
   * @throws  Exception  On test failure.
   */
  public void initialize(final String dict1, final String dict2)
    throws Exception
  {
    synchronized (LOCK) {
      if (webFile == null) {
        webFile = dict1;
      }
      if (fbsdFile == null) {
        fbsdFile = dict2;
      }
      if (randomWebWordsLarge == null) {
        randomWebWordsLarge = createRandomWords(webFile, 10000);
      }
      if (randomWebWordsSmall == null) {
        randomWebWordsSmall = createRandomWords(webFile, 10);
      }
      if (randomFbsdWordsLarge == null) {
        randomFbsdWordsLarge = createRandomWords(fbsdFile, 10000);
      }
      if (randomFbsdWordsSmall == null) {
        randomFbsdWordsSmall = createRandomWords(fbsdFile, 10);
      }
    }
  }

  /**
   * Close test resources.
   */
  @AfterSuite(groups = {"ttperftest", "wlperftest"})
  public void tearDown()
  {
    randomFbsdWordsLarge = null;
    randomFbsdWordsSmall = null;
    randomWebWordsLarge = null;
    randomWebWordsSmall = null;
  }


  /**
   * Returns an array of random words from the supplied file of the supplied size.
   *
   * @param  dictFile  path to the file to read
   * @param  size  of array to return
   *
   * @return  parameters containing words
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  private Object[][] createRandomWords(final String dictFile, final int size)
    throws IOException
  {
    final FileWordList fwl = new FileWordList(new RandomAccessFile(dictFile, "r"));
    final Object[][] allWords = new Object[size][1];
    final Random r = new Random();
    for (int i = 0; i < size; i++) {
      allWords[i] = new Object[] {fwl.get(r.nextInt(fwl.size())), };
    }
    fwl.close();
    return allWords;
  }


  /**
   * Sample word data from the large random webster's dictionary.
   *
   * @return  parameters containing word data
   */
  @DataProvider(name = "search-words-web-large")
  public static Object[][] searchWordsWebLarge()
  {
    return randomWebWordsLarge;
  }


  /**
   * Sample word data from the small random webster's dictionary.
   *
   * @return  parameters containing word data
   */
  @DataProvider(name = "search-words-web-small")
  public static Object[][] searchWordsWebSmall()
  {
    return randomWebWordsSmall;
  }


  /**
   * Sample word data from the large random freebsd dictionary.
   *
   * @return  parameters containing word data
   */
  @DataProvider(name = "search-words-fbsd-large")
  public static Object[][] searchWordsFbsdLarge()
  {
    return randomFbsdWordsLarge;
  }


  /**
   * Sample word data from the small random freebsd dictionary.
   *
   * @return  parameters containing word data
   */
  @DataProvider(name = "search-words-fbsd-small")
  public static Object[][] searchWordsFbsdSmall()
  {
    return randomFbsdWordsSmall;
  }


  /**
   * Searches the supplied dictionary for the supplied word.
   *
   * @param  d  dictionary to search
   * @param  s  string to search for
   *
   * @return  time in nanoseconds to perform the search
   *
   * @throws  IllegalStateException  if the supplied word is not found
   */
  public long doSearch(final Dictionary d, final String s)
  {
    long t = System.nanoTime();
    final boolean b = d.search(s);
    t = System.nanoTime() - t;
    if (!b) {
      throw new IllegalStateException("Word " + s + " not found");
    }
    return t;
  }
}

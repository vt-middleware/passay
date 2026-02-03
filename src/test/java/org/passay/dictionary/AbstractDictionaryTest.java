/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Base class for dictionary tests.
 *
 * @author  Middleware Services
 */
public abstract class AbstractDictionaryTest
{

  /** Missing search. */
  public static final String FALSE_SEARCH = "not-found-in-the-dictionary";

  /** Case sensitive animal search. */
  public static final String ANIMAL_SEARCH_CS = "Kangaroo";

  /** Case insensitive animal search. */
  public static final String ANIMAL_SEARCH_CI = "kangaroo";

  /** Partial animal search. */
  public static final String ANIMAL_PARTIAL_SEARCH = ".a..us";

  /** Partial animal search results. */
  public static final String[] ANIMAL_PARTIAL_SEARCH_RESULTS_CS = {"Walrus", "Xantus"};

  /** Partial animal search results. */
  public static final String[] ANIMAL_PARTIAL_SEARCH_RESULTS_CI = {"walrus", "xantus"};

  /** Initialization lock. */
  private static final Object LOCK = new Object();

  /** Animal names for sorting. */
  private static final String[] ANIMALS;

  /** store words from {@link #webFile}. */
  private static Object[][] webWords;

  /** store words from {@link #fbsdFile}. */
  private static Object[][] fbsdWords;


  /*
   * Load animal names.
   */
  static {
    final List<String> animals = new ArrayList<>();
    animals.add("Aardvark");
    animals.add("Baboon");
    animals.add("Chinchilla");
    animals.add("Donkey");
    animals.add("Emu");
    animals.add("Flamingo");
    animals.add("Gorilla");
    animals.add("Hippopotamus");
    animals.add("Iguana");
    animals.add("Jackal");
    animals.add("Kangaroo");
    animals.add("Lemming");
    animals.add("Marmot");
    animals.add("Narwhal");
    animals.add("Ox");
    animals.add("Platypus");
    animals.add("Quail");
    animals.add("Rhinoceros");
    animals.add("Skunk");
    animals.add("Tortoise");
    animals.add("Uakari");
    animals.add("Vulture");
    animals.add("Walrus");
    animals.add("Xantus");
    animals.add("Yak");
    animals.add("Zebra");
    Collections.shuffle(animals);
    ANIMALS = animals.toArray(new String[0]);
  }

  /** Location of the dictionary file. */
  protected String webFile;

  /** Location of the dictionary file. */
  protected String webFileSorted;

  /** Location of the dictionary file. */
  protected String webFileLowerCase;

  /** Location of the dictionary file. */
  protected String webFileLowerCaseSorted;

  /** Location of the dictionary file. */
  protected String fbsdFile;

  /** Location of the dictionary file. */
  protected String fbsdFileSorted;

  /** Location of the dictionary file. */
  protected String fbsdFileLowerCase;

  /** Location of the dictionary file. */
  protected String fbsdFileLowerCaseSorted;


  /**
   * @throws  Exception  On test failure.
   */
  @BeforeClass
  public void createDictionaries() throws Exception
  {
    webFile = "src/test/resources/web2";
    webFileSorted = "src/test/resources/web2.sort";
    webFileLowerCase = "src/test/resources/web2.lc";
    webFileLowerCaseSorted = "src/test/resources/web2.lc.sort";
    fbsdFile = "src/test/resources/freebsd";
    fbsdFileSorted = "src/test/resources/freebsd.sort";
    fbsdFileLowerCase = "src/test/resources/freebsd.lc";
    fbsdFileLowerCaseSorted = "src/test/resources/freebsd.lc.sort";

    synchronized (LOCK) {
      if (webWords == null) {
        webWords = createWords(webFileSorted);
      }
      if (fbsdWords == null) {
        fbsdWords = createWords(fbsdFileSorted);
      }
    }
  }

  /**
   * Close test resources.
   */
  @AfterSuite
  public void tearDown()
  {
    webWords = null;
    fbsdWords = null;
  }


  /**
   * Returns an array of words from the supplied file.
   *
   * @param  dictFile  path to the file to read
   *
   * @return  parameters containing words
   *
   * @throws  IOException  if an error occurs reading the supplied file
   */
  private Object[][] createWords(final String dictFile) throws IOException
  {
    final FileWordList fwl = new FileWordList(new RandomAccessFile(dictFile, "r"));
    final Object[][] allWords = new Object[fwl.size()][1];
    for (int i = 0; i < fwl.size(); i++) {
      allWords[i] = new Object[] {fwl.get(i), };
    }
    fwl.close();
    return allWords;
  }


  /**
   * Sample word data.
   *
   * @return  word data
   */
  @DataProvider(name = "all-web-words")
  public Object[][] createAllWebWords()
  {
    return webWords;
  }


  /**
   * Sample word data.
   *
   * @return  word data
   */
  @DataProvider(name = "all-fbsd-words")
  public Object[][] createAllFbsdWords()
  {
    return fbsdWords;
  }


  /**
   * Returns a copy of {@link #ANIMALS}.
   *
   * @return  unsorted array of animal names
   */
  protected static String[] getAnimals()
  {
    final String[] s = new String[ANIMALS.length];
    System.arraycopy(ANIMALS, 0, s, 0, s.length);
    return s;
  }
}

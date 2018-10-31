/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.io.BaseEncoding;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link BloomFilterDictionary}.
 *
 * @author  Middleware Services
 */
public class BloomFilterDictionaryTest extends AbstractDictionaryTest
{

  /** Test dictionary. */
  private BloomFilterDictionary filterFromSerialized;

  /** Test dictionary. */
  private BloomFilterDictionary filterFromTxt;


  /**
   * @param  dict  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("bloomFile")
  @BeforeClass(groups = {"bloomdicttest"})
  public void createDictionary(final String dict)
    throws Exception
  {
    final BloomFilter<String> filter1 = BloomFilter.readFrom(
      new FileInputStream(dict), Funnels.stringFunnel(StandardCharsets.UTF_8));
    filterFromSerialized = new BloomFilterDictionary(filter1);

    final ArrayWordList awl = WordLists.createFromReader(
      new FileReader[] {new FileReader(webFile)},
      true,
      new ArraysSort());
    final BloomFilter<String> filter2 = BloomFilter.create(
      Funnels.stringFunnel(StandardCharsets.UTF_8), awl.size(), 0.0001);
    for (int i = 0; i < awl.size(); i++) {
      filter2.put(awl.get(i));
    }
    filterFromTxt = new BloomFilterDictionary(filter2);
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"bloomdicttest"})
  public void closeDictionary()
    throws Exception
  {
    filterFromSerialized = null;
    filterFromTxt = null;
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"bloomdicttest"})
  public void search()
    throws Exception
  {
    AssertJUnit.assertTrue(filterFromTxt.search("manipular"));
    AssertJUnit.assertFalse(filterFromTxt.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(filterFromTxt.search("z"));
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"bloomdicttest"})
  public void searchSerialized()
    throws Exception
  {
    AssertJUnit.assertFalse(filterFromSerialized.search(hashPassword(FALSE_SEARCH)));
    AssertJUnit.assertTrue(filterFromSerialized.search(hashPassword("bbeegguumm...123")));
    AssertJUnit.assertTrue(filterFromSerialized.search(hashPassword("JLR012686jlr")));
    AssertJUnit.assertTrue(filterFromSerialized.search(hashPassword("Pixiedusts123")));
  }


  /**
   * Returns a SHA-1 hash, hex encoded representation of the password.
   *
   * @param  password  to hash
   *
   * @return  hashed password
   *
   * @throws  Exception  On encoding failure.
   */
  private static String hashPassword(final String password)
    throws Exception
  {
    final byte[] hash = MessageDigest.getInstance("SHA-1").digest(password.getBytes(StandardCharsets.UTF_8));
    return BaseEncoding.base16().upperCase().encode(hash);
  }
}

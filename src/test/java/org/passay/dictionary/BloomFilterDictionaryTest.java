/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.io.BaseEncoding;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @BeforeClass(groups = "bloomdicttest")
  public void createDictionary(final String dict) throws Exception
  {
    final BloomFilter<CharSequence> filter1 = BloomFilter.readFrom(
      Files.newInputStream(Paths.get(dict)), Funnels.stringFunnel(StandardCharsets.UTF_8));
    filterFromSerialized = new BloomFilterDictionary(filter1);

    final ArrayWordList awl = WordLists.createFromReader(
      new FileReader[] {new FileReader(webFile)},
      true,
      new ArraysSort());
    final BloomFilter<CharSequence> filter2 = BloomFilter.create(
      Funnels.stringFunnel(StandardCharsets.UTF_8), awl.size(), 0.0001);
    for (int i = 0; i < awl.size(); i++) {
      filter2.put(awl.get(i));
    }
    filterFromTxt = new BloomFilterDictionary(filter2);
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = "bloomdicttest")
  public void closeDictionary()
  {
    filterFromSerialized = null;
    filterFromTxt = null;
  }

  /**
   * Test search.
   */
  @Test(groups = "bloomdicttest")
  public void search()
  {
    assertThat(filterFromTxt.search("manipular")).isTrue();
    assertThat(filterFromTxt.search(FALSE_SEARCH)).isFalse();
    assertThat(filterFromTxt.search("z")).isTrue();
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = "bloomdicttest")
  public void searchSerialized() throws Exception
  {
    assertThat(filterFromSerialized.search(hashPassword(FALSE_SEARCH))).isFalse();
    assertThat(filterFromSerialized.search(hashPassword("bbeegguumm...123"))).isTrue();
    assertThat(filterFromSerialized.search(hashPassword("JLR012686jlr"))).isTrue();
    assertThat(filterFromSerialized.search(hashPassword("Pixiedusts123"))).isTrue();
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
  private static String hashPassword(final String password) throws Exception
  {
    final byte[] hash = MessageDigest.getInstance("SHA-1").digest(password.getBytes(StandardCharsets.UTF_8));
    return BaseEncoding.base16().upperCase().encode(hash);
  }
}

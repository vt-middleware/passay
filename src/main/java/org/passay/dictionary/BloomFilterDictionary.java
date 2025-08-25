/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import com.google.common.hash.BloomFilter;
import org.passay.PassayUtils;

/**
 * Dictionary that is backed by a <a href="https://en.wikipedia.org/wiki/Bloom_filter">Bloom Filter</a>.
 * <strong>WARNING</strong> bloom filters may return true for a word that is <strong>NOT</strong> in the dictionary.
 * This implementation should only be used if false positives can be tolerated.
 *
 * @author  Middleware Services
 */
public class BloomFilterDictionary implements Dictionary
{

  /** Filter used for searching. */
  private final BloomFilter<CharSequence> bloomFilter;


  /**
   * Creates a new dictionary instance from the supplied {@link BloomFilter}. The fpp (false-positive probability)
   * parameter of the given Bloom filter is a vitally important configuration concern. If it is too high, one risks user
   * frustration due to rejection of valid passwords; if it is too low, one risks excessive storage costs. Finding the
   * proper balance between acceptable user experience and storage costs is worth the time and effort required in
   * testing. The Guava default value of 3% is likely unsuitable for many if not most deployments.
   *
   * @param  filter  bloom filter used to determine if a word exists.
   */
  public BloomFilterDictionary(final BloomFilter<CharSequence> filter)
  {
    bloomFilter = PassayUtils.assertNotNullArg(filter, "Bloom filter cannot be null");
  }


  /**
   * Returns the bloom filter used for searching.
   *
   * @return  bloom filter
   */
  public BloomFilter<CharSequence> getBloomFilter()
  {
    return bloomFilter;
  }


  /**
   * Returns an estimate for the number of words added to the dictionary. See {@link
   * BloomFilter#approximateElementCount()}.
   *
   * @return  approximate number of words in the dictionary
   */
  @Override
  public long size()
  {
    return bloomFilter == null ? 0 : bloomFilter.approximateElementCount();
  }


  /**
   * <strong>WARNING</strong> bloom filters may return true for a word that is <strong>NOT</strong> in the dictionary.
   * Please make sure you understand how bloom filters work before using this implementation. @see
   * <a href="https://en.wikipedia.org/wiki/Bloom_filter">Bloom Filter</a> and {@link BloomFilter#mightContain(Object)}.
   *
   * @param  word  to search for
   *
   * @return  true if the word <i>might</i> be in the bloom filter, false if the word is <i>definitely not</i> in the
   * bloom filter
   */
  @Override
  public boolean search(final CharSequence word)
  {
    return bloomFilter.mightContain(word);
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::bloomFilter=" + bloomFilter;
  }
}

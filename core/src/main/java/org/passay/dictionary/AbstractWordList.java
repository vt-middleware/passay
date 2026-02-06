/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides common operations implementations for word lists.
 *
 * @author  Middleware Services
 */
public abstract class AbstractWordList implements WordList
{

  /** Word comparator. */
  protected Comparator<CharSequence> comparator;


  @Override
  public Comparator<CharSequence> getComparator()
  {
    return comparator;
  }


  @Override
  public Iterator<String> iterator()
  {
    return new WordListIterator(false);
  }


  @Override
  public Iterator<String> medianIterator()
  {
    return new WordListIterator(true);
  }


  /**
   * Throws an {@link IndexOutOfBoundsException} if the supplied index is less than 0 or greater than or equal to the
   * size of this word list.
   *
   * @param  index  to check
   */
  protected void checkRange(final int index)
  {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException("Supplied index (" + index + ") does not exist");
    }
  }


  /**
   * An iterator over the {@link WordList}.
   * <p>
   * The iteration order can be either sequential, i.e. incrementing an index from 0 to {@link WordList#size()} - 1,
   * or following a sequence of medians, i.e. the global median, followed by the median of the left half,
   * the median of the right half, the median of the left half of the left half, etc. (recursively).
   * The sequence of medians enables the creation of a well-balanced search tree from a sorted word list.
   *
   * @author  Amichai Rothman
   * @author  Ronen Zilberman
   */
  private class WordListIterator implements Iterator<String>
  {

    /** Specifies whether to use medians or sequential order. */
    protected final boolean medians;

    /** Index of next word in the iterator sequence. */
    protected int index;

    /**
     * Constructs a word list iterator.
     *
     * @param useMedians specifies whether to iterate in medians order or sequential order
     */
    protected WordListIterator(final boolean useMedians)
    {
      medians = useMedians;
    }

    @Override
    public boolean hasNext()
    {
      return index < size();
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Remove not supported.");
    }

    @Override
    public String next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return get(medians ? toMedianIndex(index++, size()) : index++);
    }

    /**
     * Returns the i-th element in the sequence of median indices of the given size.
     *
     * @param i the index within the median sequence of the element to return
     * @param size the size of the sequence
     * @return the i-th element in the median indices sequence
     */
    int toMedianIndex(final int i, final int size)
    {
      // we use long multiplication to avoid int overflow
      // (good for all int inputs, beyond that double arithmetic is required)
      final int powerOfTwo = Integer.highestOneBit(i + 1);
      final long remainder = (i + 1) - powerOfTwo;
      final int leftovers = size - powerOfTwo;
      // all power of two levels that we can fill up completely go to the first case,
      // the leftovers in the last incomplete power of two level go to the second case
      // (also the special case of index 0 of the leftovers goes to the first
      // case just so it'll return 0 and not fail due to an edge-case of rounding)
      if (leftovers >= powerOfTwo || remainder == 0) {
        // find the correct fraction (power of two denominator
        // and indexed odd-numbered numerator), and multiply by size
        // to get our median index
        return (int) (size * (2 * remainder + 1) / (2 * powerOfTwo));
      } else {
        // find the correct fraction (denominator is the number of leftover
        // items in the incomplete last power of two level, and indexed
        // numerator), and multiply by size to get our median index.
        // note that the leftovers (indices that were not returned by
        // any of the smaller power of two levels) are evenly spaced,
        // so they can be trivially calculated, with the added -1 for
        // rounding them down properly)
        return (int) ((size * remainder - 1) / leftovers);
      }
    }
  }
}

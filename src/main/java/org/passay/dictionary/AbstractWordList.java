/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Provides common operations implementations for word lists.
 *
 * @author  Middleware Services
 */
public abstract class AbstractWordList implements WordList
{

  /** Word comparator. */
  protected Comparator<String> comparator;


  @Override
  public Comparator<String> getComparator()
  {
    return comparator;
  }


  @Override
  public Iterator<String> iterator()
  {
    return new SequentialIterator();
  }


  @Override
  public Iterator<String> medianIterator()
  {
    return new MedianIterator();
  }


  /**
   * Throws an {@link IndexOutOfBoundsException} if the supplied index is less
   * than 0 or greater than or equal to the size of this word list.
   *
   * @param  index  to check
   */
  protected void checkRange(final int index)
  {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException(
        "Supplied index (" + index + ") does not exist");
    }
  }


  /**
   * Throws a {@link ClassCastException} if the supplied object is not an
   * instance of {@link String}.
   *
   * @param  o  object to check
   */
  protected void checkIsString(final Object o)
  {
    if (!String.class.isInstance(o)) {
      throw new ClassCastException("Parameter must be of type String");
    }
  }


  /**
   * Abstract base class for all internal word list iterators.
   *
   * @author  Middleware Services
   */
  private abstract class AbstractWordListIterator implements Iterator<String>
  {

    /** Index of next word in list. */
    protected int index;


    @Override
    public void remove()
    {
      throw new UnsupportedOperationException("Remove not supported.");
    }
  }


  /**
   * Iterator implementation that iterates over a {@link WordList} by
   * incrementing an index from 0 to {@link WordList#size()} - 1.
   *
   * @author  Middleware Services
   */
  private class SequentialIterator extends AbstractWordListIterator
  {


    @Override
    public boolean hasNext()
    {
      return index < size();
    }


    @Override
    public String next()
    {
      return get(index++);
    }
  }


  /**
   * Iterator that iterates over a word list from the median outward to either
   * end. In particular, for a word list of N elements whose median index is M,
   * and for each i such that M-i >= 0 and M+i < N, the M-i element is visited
   * before the M+i element.
   *
   * @author  Middleware Services
   */
  private class MedianIterator extends AbstractWordListIterator
  {

    /** Index of median element in given list. */
    private final int median = size() / 2;

    /** Indicates direction of next item. */
    private int sign;


    @Override
    public boolean hasNext()
    {
      final int n = size();
      final boolean result;
      if (sign > 0) {
        result = median + index < n;
      } else if (sign < 0) {
        result = median - index >= 0;
      } else {
        result = n > 0;
      }
      return result;
    }


    @Override
    public String next()
    {
      final String next;
      if (sign > 0) {
        next = get(median + index);
        sign = -1;
        index++;
      } else if (sign < 0) {
        next = get(median - index);
        sign = 1;
      } else {
        next = get(median);
        sign = -1;
        index = 1;
      }
      return next;
    }
  }
}

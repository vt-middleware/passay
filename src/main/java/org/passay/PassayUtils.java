/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.function.Predicate;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 */
public final class PassayUtils
{


  /** Default constructor. */
  private PassayUtils() {}


  /**
   * Throws {@link IllegalArgumentException} if the supplied object is null.
   *
   * @param  <T>  type of object
   * @param  o to check
   * @param  msg to include in the exception
   *
   * @return  supplied object
   */
  public static <T> T assertNotNullArg(final T o, final String msg)
  {
    if (o == null) {
      throw new IllegalArgumentException(msg);
    }
    return o;
  }


  /**
   * Throws {@link IllegalArgumentException} if the supplied object is null or the supplied predicate returns true.
   *
   * @param  <T>  type of object
   * @param  o to check
   * @param  predicate  to test
   * @param  msg to include in the exception
   *
   * @return  supplied object
   */
  public static <T> T assertNotNullArgOr(final T o, final Predicate<T> predicate, final String msg)
  {
    try {
      if (o == null || predicate.test(o)) {
        throw new IllegalArgumentException(msg);
      }
    } catch (Exception e) {
      // treat a predicate exception as an illegal argument
      throw new IllegalArgumentException(e);
    }
    return o;
  }
}

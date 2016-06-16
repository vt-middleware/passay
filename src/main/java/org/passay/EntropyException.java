/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;


/**
 * Exception thrown when an entropy estimation method cannot succeed.
 * ie: {@link PasswordValidator#getEntropyBits(org.passay.PasswordData)}.
 *
 * @author  Middleware Services
 */
public class EntropyException extends Exception
{

  /** serialVersionUID. */
  private static final long serialVersionUID = -9210448091216749429L;

  /** Default constructor. **/
  public EntropyException() {}


  /**
   * Creates a new EntropyException exception.
   *
   * @param  message  describing this exception
   */
  public EntropyException(final String message)
  {
    super(message);
  }


  /**
   * Creates a new EntropyException exception.
   *
   * @param  message  describing this exception
   * @param  cause  root cause
   */
  public EntropyException(final String message, final Throwable cause)
  {
    super(message, cause);
  }


  /**
   * Creates a new EntropyException exception.
   *
   * @param  cause  root cause
   */
  public EntropyException(final Throwable cause)
  {
    super(cause);
  }
}

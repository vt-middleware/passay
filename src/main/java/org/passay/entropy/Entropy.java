/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

/**
 * Interface for entropy estimates.
 *
 * @author  Middleware Services
 */
public interface Entropy
{


  /**
   * Returns the estimated entropy bits of a password.
   *
   * @return  estimated entropy bits given password properties
   */
  double estimate();
}

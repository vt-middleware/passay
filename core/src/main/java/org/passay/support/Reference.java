/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import org.passay.UnicodeString;

/**
 * Reference to another password that should be considered as part of password validation.
 *
 * @author  Middleware Services
 */
public interface Reference
{


  /**
   * Returns the password associated with this reference.
   *
   * @return  password string
   */
  UnicodeString getPassword();


  /**
   * Clears the memory of the underlying objects in this reference.
   */
  void clear();


  /**
   * Returns the salt that was applied to the reference password before digesting it.
   *
   * @return  salt  the salt that was applied to the password,
   *          or null if no salt was applied
   */
  default Salt getSalt()
  {
    return null;
  }
}

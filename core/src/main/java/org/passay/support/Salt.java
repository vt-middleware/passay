/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import java.nio.CharBuffer;

/**
 * Combines salt (additional external data) with a password
 * before applying a digest algorithm to them.
 *
 * @author  Middleware Services
 */
public interface Salt
{

  /**
   * Applies the salt to the password, returning the combined string to be digested.
   *
   * @param password the cleartext password to apply the salt to
   * @return the salted password
   */
  CharBuffer applyTo(CharBuffer password);
}

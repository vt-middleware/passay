/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import java.nio.Buffer;
import java.nio.CharBuffer;

/**
 * A salt that is concatenated as a prefix to the password data.
 *
 * @author  Middleware Services
 */
public class PrefixSalt implements Salt
{

  /** The salt data. */
  private final String salt;

  /**
   * Creates a new salt with the given salt data.
   *
   * @param slt the salt data
   */
  public PrefixSalt(final String slt)
  {
    salt = slt;
  }

  @Override
  public CharBuffer applyTo(final CharBuffer password)
  {
    final CharBuffer salted = CharBuffer.allocate(salt.length() + password.length());
    salted.put(salt).put(password);
    ((Buffer) salted).flip();
    return salted;
  }
}

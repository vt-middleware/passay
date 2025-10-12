/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import java.nio.Buffer;
import java.nio.CharBuffer;

/**
 * A salt that is concatenated as a suffix to the password data.
 *
 * @author  Middleware Services
 */
public class SuffixSalt implements Salt
{

  /** The salt data. */
  private final String salt;

  /**
   * Creates a new salt with the given salt data.
   *
   * @param salt the salt data
   */
  public SuffixSalt(final String salt)
  {
    this.salt = salt;
  }

  @Override
  public CharBuffer applyTo(final CharBuffer password)
  {
    final CharBuffer salted = CharBuffer.allocate(salt.length() + password.length());
    salted.put(password).put(salt);
    ((Buffer) salted).flip();
    return salted;
  }
}

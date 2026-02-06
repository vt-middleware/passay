/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import org.passay.UnicodeString;

/**
 * Reference to a source password.
 *
 * @author  Middleware Services
 */
public class SourceReference extends AbstractReference
{


  /**
   * Creates a new source reference.
   *
   * @param  password  password string
   */
  public SourceReference(final CharSequence password)
  {
    super(null, password);
  }


  /**
   * Creates a new source reference.
   *
   * @param  password  password string
   */
  public SourceReference(final UnicodeString password)
  {
    super(null, password);
  }


  /**
   * Creates a new source reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public SourceReference(final String label, final CharSequence password)
  {
    super(label, password);
  }


  /**
   * Creates a new source reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public SourceReference(final String label, final UnicodeString password)
  {
    super(label, password);
  }


  /**
   * Creates a new source reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public SourceReference(final String label, final CharSequence password, final Salt salt)
  {
    super(label, password, salt);
  }


  /**
   * Creates a new source reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public SourceReference(final String label, final UnicodeString password, final Salt salt)
  {
    super(label, password, salt);
  }
}

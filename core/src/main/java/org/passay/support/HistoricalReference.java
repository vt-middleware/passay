/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import org.passay.UnicodeString;

/**
 * Reference to an historical password.
 *
 * @author  Middleware Services
 */
public class HistoricalReference extends AbstractReference
{


  /**
   * Creates a new historical reference.
   *
   * @param  password  password string
   */
  public HistoricalReference(final CharSequence password)
  {
    super(null, password);
  }


  /**
   * Creates a new historical reference.
   *
   * @param  password  password string
   */
  public HistoricalReference(final UnicodeString password)
  {
    super(null, password);
  }


  /**
   * Creates a new historical reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public HistoricalReference(final String label, final CharSequence password)
  {
    super(label, password);
  }


  /**
   * Creates a new historical reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public HistoricalReference(final String label, final UnicodeString password)
  {
    super(label, password);
  }


  /**
   * Creates a new historical reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public HistoricalReference(final String label, final CharSequence password, final Salt salt)
  {
    super(label, password, salt);
  }


  /**
   * Creates a new historical reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public HistoricalReference(final String label, final UnicodeString password, final Salt salt)
  {
    super(label, password, salt);
  }
}

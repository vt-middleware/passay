/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.support;

import org.passay.UnicodeString;

/**
 * Common password reference implementation.
 *
 * @author  Middleware Services
 */
public abstract class AbstractReference implements Reference
{

  /** Label to identify this password. */
  private final String label;

  /** Reference password. */
  private final UnicodeString password;

  /** Salt that was applied to reference password before digesting it. */
  private final Salt salt;


  /**
   * Creates a new abstract reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public AbstractReference(final String label, final CharSequence password)
  {
    this(label, password, null);
  }


  /**
   * Creates a new abstract reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   */
  public AbstractReference(final String label, final UnicodeString password)
  {
    this(label, password, null);
  }


  /**
   * Creates a new abstract reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public AbstractReference(final String label, final CharSequence password, final Salt salt)
  {
    this(label, new UnicodeString(password), salt);
  }


  /**
   * Creates a new abstract reference.
   *
   * @param  label  label for this password
   * @param  password  password string
   * @param  salt  salt that was applied to password
   */
  public AbstractReference(final String label, final UnicodeString password, final Salt salt)
  {
    this.label = label;
    this.password = password;
    this.salt = salt;
  }


  /**
   * Returns the label.
   *
   * @return  reference label
   */
  public String getLabel()
  {
    return label;
  }


  @Override
  public UnicodeString getPassword()
  {
    return password;
  }


  @Override
  public Salt getSalt()
  {
    return salt;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "label=" + label + ", " +
      "password=" + password + ", " +
      "salt=" + salt;
  }


  @Override
  public void clear()
  {
    if (password != null) {
      password.clear();
    }
  }
}

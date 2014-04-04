/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.charset.Charset;
import org.cryptacular.bean.EncodingHashBean;

/**
 * Rule for determining if a password matches one of any previous digested
 * password a user has chosen. If no password reference has been set that
 * matches the label on the rule, then passwords will meet this rule. See {@link
 * PasswordData#setPasswordReferences(List)}.
 *
 * @author  Middleware Services
 */
public class DigestHistoryRule extends HistoryRule
{

  /** UTF-8 charset. */
  private static final Charset UTF8 = Charset.forName("UTF-8");

  /** Hash bean to use for comparing hashed passwords. */
  private final EncodingHashBean hashBean;

  /** Character set to use for undigested passwords. */
  private Charset charset = UTF8;


  /**
   * Creates new digest history rule which operates on password references with
   * the supplied label.
   *
   * @param  bean  encoding hash bean
   */
  public DigestHistoryRule(final EncodingHashBean bean)
  {
    hashBean = bean;
  }


  /**
   * Sets the character set to use for undigested passwords.
   *
   * @param  set  to use for undigested passwords
   */
  public void setCharset(final Charset set)
  {
    if (set == null) {
      throw new NullPointerException("Character set cannot be null");
    }
    charset = set;
  }


  /**
   * Determines whether a digested password matches a reference value.
   *
   * @param  undigested  candidate clear text password.
   * @param  reference  reference digested password.
   *
   * @return  true if passwords match, false otherwise.
   */
  @Override
  protected boolean matches(
    final String undigested,
    final PasswordData.Reference reference)
  {
    return
      hashBean.compare(reference.getPassword(), undigested.getBytes(charset));
  }
}

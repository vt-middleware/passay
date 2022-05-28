/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.cryptacular.bean.HashBean;
import org.passay.logic.PasswordData;

/**
 * Rule for determining if a password matches one of any previous digested password a user has chosen. If no password
 * reference has been set that matches the label on the rule, then passwords will meet this rule. See {@link
 * PasswordData#setPasswordReferences}.
 *
 * @author  Middleware Services
 */
public class DigestHistoryRule extends HistoryRule
{

  /** Hash bean to use for comparing hashed passwords. */
  private final HashBean<String> hashBean;

  /** Character set to use for undigested passwords. */
  private Charset charset = StandardCharsets.UTF_8;


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   *
   * @param  bean  encoding hash bean
   */
  public DigestHistoryRule(final HashBean<String> bean)
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
   * @param  password  candidate clear text password.
   * @param  reference  reference digested password.
   *
   * @return  true if passwords match, false otherwise.
   */
  @Override
  protected boolean matches(final String password, final PasswordData.Reference reference)
  {
    final PasswordData.Salt salt = reference.getSalt();
    final String undigested = salt == null ? password : salt.applyTo(password);
    return hashBean.compare(reference.getPassword(), undigested.getBytes(charset));
  }
}

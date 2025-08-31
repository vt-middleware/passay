/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.cryptacular.bean.HashBean;
import org.passay.PassayUtils;
import org.passay.PasswordData;

/**
 * Rule for determining if a password matches a digested password from a different source. Useful for when separate
 * systems cannot have matching passwords. If no password reference has been set that matches the label on the rule,
 * then passwords will meet this rule. See {@link PasswordData#getPasswordReferences()}
 *
 * @author  Middleware Services
 */
public class DigestSourceRule extends SourceRule
{

  /** Hash bean to use for comparing hashed passwords. */
  private final HashBean<String> hashBean;

  /** Character set to use for undigested passwords. */
  private final Charset charset;


  /**
   * Creates new digest source rule which operates on password references with the supplied label.
   *
   * @param  bean  encoding hash bean
   */
  public DigestSourceRule(final HashBean<String> bean)
  {
    this(bean, StandardCharsets.UTF_8);
  }


  /**
   * Creates new digest source rule which operates on password references with the supplied label.
   *
   * @param  bean  encoding hash bean
   * @param  set  to use for undigested passwords
   */
  public DigestSourceRule(final HashBean<String> bean, final Charset set)
  {
    hashBean = PassayUtils.assertNotNullArg(bean, "Hash bean cannot be null");
    charset = PassayUtils.assertNotNullArg(set, "Character set cannot be null");
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

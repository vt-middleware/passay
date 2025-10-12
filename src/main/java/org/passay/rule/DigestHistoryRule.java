/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.cryptacular.bean.HashBean;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.UnicodeString;
import org.passay.support.Reference;
import org.passay.support.Salt;

/**
 * Rule for determining if a password matches one of any previous digested password a user has chosen. If no password
 * reference has been set that matches the label on the rule, then passwords will meet this rule. See {@link
 * PasswordData#getPasswordReferences()}.
 *
 * @author  Middleware Services
 */
public class DigestHistoryRule extends HistoryRule
{

  /** Hash bean to use for comparing hashed passwords. */
  private final HashBean<String> hashBean;

  /** Character set to use for undigested passwords. */
  private final Charset charset;


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   *
   * @param  bean  encoding hash bean
   */
  public DigestHistoryRule(final HashBean<String> bean)
  {
    this(bean, StandardCharsets.UTF_8);
  }


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   *
   * @param  bean  encoding hash bean
   * @param  set  to use for undigested passwords
   */
  public DigestHistoryRule(final HashBean<String> bean, final Charset set)
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
  protected boolean matches(final UnicodeString password, final Reference reference)
  {
    final Salt salt = reference.getSalt();
    final CharBuffer buffer = CharBuffer.wrap(password.toCharArray());
    final CharBuffer undigested = salt == null ? buffer : salt.applyTo(buffer);
    try {
      return hashBean.compare(reference.getPassword().toString(), PassayUtils.toByteArray(undigested, charset));
    } finally {
      PassayUtils.clear(buffer);
      PassayUtils.clear(undigested);
    }
  }
}

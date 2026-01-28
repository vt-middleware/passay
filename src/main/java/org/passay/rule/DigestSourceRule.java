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
 * Rule for determining if a password matches a digested password from a different source. Useful for when separate
 * systems cannot have matching passwords. If no password reference has been set that matches the label on the rule,
 * then passwords will meet this rule. See {@link PasswordData#getPasswordReferences()}
 *
 * @author  Middleware Services
 */
public class DigestSourceRule extends SourceRule
{

  /** Hash bean to use for comparing hashed passwords. */
  private final HashBean<CharSequence> hashBean;

  /** Character set to use for undigested passwords. */
  private final Charset charset;


  /**
   * Creates new digest source rule which operates on password references with the supplied label.
   *
   * @param  bean  encoding hash bean
   */
  public DigestSourceRule(final HashBean<CharSequence> bean)
  {
    this(bean, StandardCharsets.UTF_8);
  }


  /**
   * Creates new digest source rule which operates on password references with the supplied label.
   *
   * @param  bean  encoding hash bean
   * @param  set  to use for undigested passwords
   */
  public DigestSourceRule(final HashBean<CharSequence> bean, final Charset set)
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

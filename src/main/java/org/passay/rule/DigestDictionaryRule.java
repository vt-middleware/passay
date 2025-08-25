/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.cryptacular.bean.HashBean;
import org.passay.PassayUtils;
import org.passay.UnicodeString;
import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password matches a digested password that is stored in a dictionary.
 *
 * @author  Middleware Services
 */
public class DigestDictionaryRule extends AbstractDictionaryRule
{

  /** Error code for matching dictionary word. */
  public static final String ERROR_CODE = "ILLEGAL_DIGEST_WORD";

  /** Error code for matching reversed dictionary word. */
  public static final String ERROR_CODE_REVERSED = "ILLEGAL_DIGEST_WORD_REVERSED";

  /** Hash bean to use for comparing hashed passwords. */
  private final HashBean<String> hashBean;

  /** Character set to use for undigested passwords. */
  private final Charset charset;


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   * The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   * @param  bean  encoding hash bean
   */
  public DigestDictionaryRule(final HashBean<String> bean, final Dictionary dict)
  {
    this(bean, dict, false);
  }


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   * The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   * @param  bean  encoding hash bean
   * @param  matchBackwards  whether to match dictionary words backwards
   */
  public DigestDictionaryRule(final HashBean<String> bean, final Dictionary dict, final boolean matchBackwards)
  {
    this(bean, dict, matchBackwards, StandardCharsets.UTF_8);
  }


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   * The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   * @param  bean  encoding hash bean
   * @param  matchBackwards  whether to match dictionary words backwards
   * @param  set  to use for undigested passwords
   */
  public DigestDictionaryRule(
    final HashBean<String> bean, final Dictionary dict, final boolean matchBackwards, final Charset set)
  {
    super(dict, matchBackwards);
    hashBean = PassayUtils.assertNotNullArg(bean, "Hash bean cannot be null");
    charset = PassayUtils.assertNotNullArg(set, "Character set cannot be null");
  }


  @Override
  protected CharSequence doWordSearch(final UnicodeString text)
  {
    final byte[] bytes = PassayUtils.toByteArray(text, charset);
    try {
      return getDictionary().search(hashBean.hash(bytes)) ? text : null;
    } finally {
      PassayUtils.clear(bytes);
    }
  }


  @Override
  protected String getErrorCode(final boolean backwards)
  {
    return backwards ? ERROR_CODE_REVERSED : ERROR_CODE;
  }
}

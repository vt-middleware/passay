/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.cryptacular.bean.HashBean;

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
  private Charset charset = StandardCharsets.UTF_8;


  /**
   * Creates new digest history rule which operates on password references that were digested with the supplied hash.
   *
   * @param  bean  encoding hash bean
   */
  public DigestDictionaryRule(final HashBean<String> bean)
  {
    hashBean = bean;
  }


  /**
   * Sets the character set to use when converting a candidate password to bytes prior to hashing.
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


  @Override
  protected String doWordSearch(final String text)
  {
    return getDictionary().search(hashBean.hash(text.getBytes(charset))) ? text : null;
  }


  @Override
  protected String getErrorCode(final boolean backwards)
  {
    return backwards ? ERROR_CODE_REVERSED : ERROR_CODE;
  }
}

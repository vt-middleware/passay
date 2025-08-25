/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.UnicodeString;
import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password matches a dictionary word. This rule will optionally also check for reversed
 * words.
 *
 * @author  Middleware Services
 */
public class DictionaryRule extends AbstractDictionaryRule
{

  /** Error code for matching dictionary word. */
  public static final String ERROR_CODE = "ILLEGAL_WORD";

  /** Error code for matching reversed dictionary word. */
  public static final String ERROR_CODE_REVERSED = "ILLEGAL_WORD_REVERSED";


  /**
   * Creates a new dictionary rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionaryRule(final Dictionary dict)
  {
    this(dict, false);
  }


  /**
   * Creates a new dictionary rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   * @param  matchBackwards  whether to match dictionary words backwards
   */
  public DictionaryRule(final Dictionary dict, final boolean matchBackwards)
  {
    super(dict, matchBackwards);
  }


  @Override
  protected CharSequence doWordSearch(final UnicodeString text)
  {
    if (getDictionary().search(text)) {
      return text;
    }
    return null;
  }


  @Override
  protected String getErrorCode(final boolean backwards)
  {
    return backwards ? ERROR_CODE_REVERSED : ERROR_CODE;
  }
}

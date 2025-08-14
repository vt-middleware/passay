/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password contains a dictionary word with optional checking for reversed words.
 *
 * @author  Middleware Services
 */
public class DictionarySubstringRule extends AbstractDictionaryRule
{

  /** Error code for matching dictionary word. */
  public static final String ERROR_CODE = "ILLEGAL_WORD";

  /** Error code for matching reversed dictionary word. */
  public static final String ERROR_CODE_REVERSED = "ILLEGAL_WORD_REVERSED";


  /**
   * Creates a new dictionary substring rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionarySubstringRule(final Dictionary dict)
  {
    this(dict, false);
  }


  /**
   * Creates a new dictionary substring rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   * @param  matchBackwards  whether to match dictionary words backwards
   */
  public DictionarySubstringRule(final Dictionary dict, final boolean matchBackwards)
  {
    super(dict, matchBackwards);
  }


  @Override
  protected String doWordSearch(final String text)
  {
    int i = Character.charCount(text.codePointAt(0));
    while (i < text.length()) {
      int j = 0;
      while (j + i <= text.length()) {
        final String s = text.substring(j, j + i);
        if (getDictionary().search(s)) {
          return s;
        }
        j += Character.charCount(text.codePointAt(j));
      }
      i += Character.charCount(text.codePointAt(i));
    }
    return null;
  }


  @Override
  protected String getErrorCode(final boolean backwards)
  {
    return backwards ? ERROR_CODE_REVERSED : ERROR_CODE;
  }
}

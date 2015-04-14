/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password matches a dictionary word. This rule will optionally also check for reversed
 * words.
 *
 * @author  Middleware Services
 */
public class DictionaryRule extends AbstractDictionaryRule
{

  /**
   * Creates a new dictionary rule without supplying a dictionary. The dictionary should be set using {@link
   * #setDictionary(Dictionary)}.
   */
  public DictionaryRule() {}


  /**
   * Creates a new dictionary rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionaryRule(final Dictionary dict)
  {
    setDictionary(dict);
  }


  @Override
  protected String doWordSearch(final String text)
  {
    if (getDictionary().search(text)) {
      return text;
    }
    return null;
  }
}

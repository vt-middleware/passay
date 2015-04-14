/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password contains a dictionary word with optional checking for reversed words.
 *
 * @author  Middleware Services
 */
public class DictionarySubstringRule extends AbstractDictionaryRule
{

  /**
   * Creates a new dictionary substring rule. The dictionary should be set using the {@link #setDictionary(Dictionary)}
   * method.
   */
  public DictionarySubstringRule() {}


  /**
   * Creates a new dictionary substring rule. The dictionary should be ready to use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionarySubstringRule(final Dictionary dict)
  {
    setDictionary(dict);
  }


  @Override
  protected String doWordSearch(final String text)
  {
    for (int i = 1; i <= text.length(); i++) {
      for (int j = 0; j + i <= text.length(); j++) {
        final String s = text.substring(j, j + i);
        if (getDictionary().search(s)) {
          return s;
        }
      }
    }
    return null;
  }
}

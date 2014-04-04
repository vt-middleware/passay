/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.passay.dictionary.Dictionary;

/**
 * Rule for determining if a password matches a dictionary word, or if it
 * contains a dictionary word of a given minimum length or greater. This rule
 * will optionally also check for reversed words.
 *
 * @author  Middleware Services
 */
public class DictionarySubstringRule extends AbstractDictionaryRule
{

  /** Default word length. */
  public static final int DEFAULT_WORD_LENGTH = 4;

  /**
   * Minimum substring size to consider as a possible word within the password.
   */
  private int wordLength = DEFAULT_WORD_LENGTH;


  /**
   * Creates a new dictionary substring rule. The dictionary should be set using
   * the {@link #setDictionary(Dictionary)} method.
   */
  public DictionarySubstringRule() {}


  /**
   * Creates a new dictionary substring rule. The dictionary should be ready to
   * use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionarySubstringRule(final Dictionary dict)
  {
    dictionary = dict;
  }


  /**
   * Create a new dictionary substring rule. The dictionary should be ready to
   * use when passed to this constructor. See {@link #setWordLength(int)}.
   *
   * @param  dict  to use for searching
   * @param  n  number of characters to check in each dictionary word
   */
  public DictionarySubstringRule(final Dictionary dict, final int n)
  {
    setDictionary(dict);
    setWordLength(n);
  }


  /**
   * Sets the minimum number of characters that constitute a word in a password.
   * If n = 5 and the password contains 'test', then the password is valid.
   * However if n = 4 then 'test' will be found in the dictionary. The default
   * value is 4.
   *
   * @param  n  minimum number of characters to check in each dictionary word
   */
  public void setWordLength(final int n)
  {
    if (n >= 1) {
      wordLength = n;
    } else {
      throw new IllegalArgumentException("wordLength must be >= 1");
    }
  }


  /**
   * Returns the number of characters that constitute a word in a password.
   *
   * @return  minimum number of characters to check in each dictionary word
   */
  public int getWordLength()
  {
    return wordLength;
  }


  @Override
  protected String doWordSearch(final String text)
  {
    for (int i = wordLength; i <= text.length(); i++) {
      for (int j = 0; j + i <= text.length(); j++) {
        final String s = text.substring(j, j + i);
        if (dictionary.search(s)) {
          return s;
        }
      }
    }
    return null;
  }
}

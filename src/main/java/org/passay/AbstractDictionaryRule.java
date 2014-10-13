/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;
import org.passay.dictionary.Dictionary;

/**
 * Provides common implementation for password dictionary rules.
 *
 * @author  Middleware Services
 */
public abstract class AbstractDictionaryRule implements Rule
{

  /** Error code for matching dictionary word. */
  public static final String ERROR_CODE = "ILLEGAL_WORD";

  /** Error code for matching reversed dictionary word. */
  public static final String ERROR_CODE_REVERSED = "ILLEGAL_WORD_REVERSED";

  /** Dictionary of words. */
  protected Dictionary dictionary;

  /** Whether to search for dictionary words backwards. */
  protected boolean matchBackwards;


  /**
   * Sets the dictionary used to search for passwords.
   *
   * @param  dict  to use for searching
   */
  public void setDictionary(final Dictionary dict)
  {
    dictionary = dict;
  }


  /**
   * Returns the dictionary used to search for passwords.
   *
   * @return  dictionary used for searching
   */
  public Dictionary getDictionary()
  {
    return dictionary;
  }


  /**
   * This causes the verify method to search the password for dictionary words
   * spelled backwards as well as forwards.
   *
   * @param  b  whether to match dictionary words backwards
   */
  public void setMatchBackwards(final boolean b)
  {
    matchBackwards = b;
  }


  /**
   * Returns true if the verify method will search the password for dictionary
   * words spelled backwards as well as forwards.
   *
   * @return  whether to match dictionary words backwards
   */
  public boolean isMatchBackwards()
  {
    return matchBackwards;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    String text = passwordData.getPassword();
    String matchingWord = doWordSearch(text);
    if (matchingWord != null) {
      result.setValid(false);
      result.getDetails().add(
        new RuleResultDetail(
          ERROR_CODE,
          createRuleResultDetailParameters(matchingWord)));
    }
    if (matchBackwards && text.length() > 1) {
      text = new StringBuilder(passwordData.getPassword()).reverse()
        .toString();
      matchingWord = doWordSearch(text);
      if (matchingWord != null) {
        result.setValid(false);
        result.getDetails().add(
          new RuleResultDetail(
            ERROR_CODE_REVERSED,
            createRuleResultDetailParameters(matchingWord)));
      }
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  word  matching word
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(
    final String word)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("matchingWord", word);
    return m;
  }


  /**
   * Searches the dictionary with the supplied text.
   *
   * @param  text  to search dictionary with
   *
   * @return  matching word
   */
  protected abstract String doWordSearch(final String text);


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::dictionary=%s,matchBackwards=%s",
        getClass().getName(),
        hashCode(),
        dictionary,
        matchBackwards);
  }
}

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

  /** Dictionary of words. */
  private Dictionary dictionary;

  /** Whether to search for dictionary words backwards. */
  private boolean matchBackwards;


  /**
   * Sets the dictionary used to search for passwords.
   *
   * @param  dict  to use for searching
   */
  public void setDictionary(final Dictionary dict)
  {
    if (dict == null) {
      throw new NullPointerException("Dictionary cannot be null");
    }
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
   * This causes the verify method to search the password for dictionary words spelled backwards as well as forwards.
   *
   * @param  b  whether to match dictionary words backwards
   */
  public void setMatchBackwards(final boolean b)
  {
    matchBackwards = b;
  }


  /**
   * Returns true if the verify method will search the password for dictionary words spelled backwards as well as
   * forwards.
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
        new RuleResultDetail(getErrorCode(false), createRuleResultDetailParameters(matchingWord)));
    }
    if (matchBackwards && text.length() > 1) {
      text = new StringBuilder(passwordData.getPassword()).reverse().toString();
      matchingWord = doWordSearch(text);
      if (matchingWord != null) {
        result.setValid(false);
        result.getDetails().add(
          new RuleResultDetail(getErrorCode(true), createRuleResultDetailParameters(matchingWord)));
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
  protected Map<String, Object> createRuleResultDetailParameters(final String word)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("matchingWord", word);
    return m;
  }


  /**
   * Returns the error code for this rule.
   *
   * @param  backwards  whether to return the error code for a backwards match
   *
   * @return  properties error code
   */
  protected abstract String getErrorCode(boolean backwards);


  /**
   * Searches the dictionary with the supplied text.
   *
   * @param  text  to search dictionary with
   *
   * @return  matching word
   */
  protected abstract String doWordSearch(String text);


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

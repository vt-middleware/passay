/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SuccessRuleResult;
import org.passay.dictionary.Dictionary;

/**
 * Provides common implementation for password dictionary rules.
 *
 * @author  Middleware Services
 */
public abstract class AbstractDictionaryRule implements Rule
{

  /** Dictionary of words. */
  private final Dictionary dictionary;

  /** Whether to search for dictionary words backwards. */
  private final boolean matchBackwards;


  /**
   * Creates a new abstract dictionary rule.
   *
   * @param  dict  to use for searching
   * @param  matchBackwards  whether to match dictionary words backwards
   */
  public AbstractDictionaryRule(final Dictionary dict, final boolean matchBackwards)
  {
    this.dictionary = PassayUtils.assertNotNullArg(dict, "Dictionary cannot be null");
    this.matchBackwards = matchBackwards;
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
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<RuleResultDetail> details = new ArrayList<>();
    String text = passwordData.getPassword();
    String matchingWord = doWordSearch(text);
    if (matchingWord != null) {
      details.add(new RuleResultDetail(getErrorCode(false), createRuleResultDetailParameters(matchingWord)));
    }
    if (matchBackwards && text.length() > 1) {
      text = new StringBuilder(passwordData.getPassword()).reverse().toString();
      matchingWord = doWordSearch(text);
      if (matchingWord != null) {
        details.add(new RuleResultDetail(getErrorCode(true), createRuleResultDetailParameters(matchingWord)));
      }
    }
    return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
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

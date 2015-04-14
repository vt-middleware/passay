/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule for determining if a password matches an illegal regular expression. Passwords which match the expression will
 * fail validation.
 *
 * @author  Middleware Services
 */
public class IllegalRegexRule implements Rule
{

  /** Error code for regex validation failures. */
  public static final String ERROR_CODE = "ILLEGAL_MATCH";

  /** Regex pattern. */
  protected final Pattern pattern;


  /**
   * Creates a new illegal regex rule.
   *
   * @param  regex  regular expression
   */
  public IllegalRegexRule(final String regex)
  {
    pattern = Pattern.compile(regex);
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final Matcher m = pattern.matcher(passwordData.getPassword());
    if (m.find()) {
      result.setValid(false);
      result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(m.group())));
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  match  matching regex
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String match)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("match", match);
    m.put("pattern", pattern);
    return m;
  }


  @Override
  public String toString()
  {
    return String.format("%s@%h::pattern=%s", getClass().getName(), hashCode(), pattern);
  }
}

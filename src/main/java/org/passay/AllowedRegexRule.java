/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule for determining if a password matches a regular expression.
 *
 * @author  Middleware Services
 */
public class AllowedRegexRule implements Rule
{

  /** Error code for regex validation failures. */
  public static final String ERROR_CODE = "ALLOWED_MATCH";

  /** Regex pattern. */
  protected final Pattern pattern;


  /**
   * Creates a new allowed regex rule.
   *
   * @param  regex  regular expression
   */
  public AllowedRegexRule(final String regex)
  {
    pattern = Pattern.compile(regex);
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final Matcher m = pattern.matcher(passwordData.getPassword());
    if (!m.find()) {
      result.setValid(false);
      result.getDetails().add(
        new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters()));
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @return  map of parameter name to value
   */
  protected Map<String, ?> createRuleResultDetailParameters()
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("pattern", pattern);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::pattern=%s",
        getClass().getName(),
        hashCode(),
        pattern);
  }
}

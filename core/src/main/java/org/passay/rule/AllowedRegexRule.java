/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SuccessRuleResult;

/**
 * Rule for determining if a password matches an allowed regular expression. Passwords must match the expression or
 * validation will fail.
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


  /**
   * Creates a new allowed regex rule.
   *
   * @param  regex  regular expression
   * @param  regexFlags  regular expression flags
   */
  public AllowedRegexRule(final String regex, final int regexFlags)
  {
    pattern = Pattern.compile(regex, regexFlags);
  }


  /**
   * Returns the pattern for this rule.
   *
   * @return  pattern
   */
  public Pattern getPattern()
  {
    return pattern;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final Matcher m = pattern.matcher(passwordData.getPassword());
    if (!m.find()) {
      return new FailureRuleResult(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters()));
    }
    return new SuccessRuleResult();
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters()
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("pattern", pattern);
    return m;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::pattern=" + pattern;
  }
}

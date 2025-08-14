/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;

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
    try {
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }


  /**
   * Creates a new allowed regex rule.
   *
   * @param  regex  regular expression
   * @param  regexFlags  regular expression flags
   */
  public AllowedRegexRule(final String regex, final int regexFlags)
  {
    try {
      pattern = Pattern.compile(regex, regexFlags);
    } catch (PatternSyntaxException e) {
      throw new IllegalArgumentException(e);
    }
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
    final RuleResult result = new RuleResult();
    final Matcher m = pattern.matcher(passwordData.getPassword());
    if (!m.find()) {
      result.addError(ERROR_CODE, createRuleResultDetailParameters());
    }
    return result;
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
    return String.format("%s@%h::pattern=%s", getClass().getName(), hashCode(), pattern);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.passay.PasswordData;
import org.passay.RuleResult;

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

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;


  /**
   * Creates a new illegal regex rule.
   *
   * @param  regex  regular expression
   */
  public IllegalRegexRule(final String regex)
  {
    this(regex, true);
  }


  /**
   * Creates a new illegal regex rule.
   *
   * @param  regex  regular expression
   * @param  regexFlags  regular expression flags
   */
  public IllegalRegexRule(final String regex, final int regexFlags)
  {
    this(regex, regexFlags, true);
  }


  /**
   * Creates a new illegal regex rule.
   *
   * @param  regex  regular expression
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalRegexRule(final String regex, final boolean reportAll)
  {
    pattern = Pattern.compile(regex);
    reportAllFailures = reportAll;
  }


  /**
   * Creates a new illegal regex rule.
   *
   * @param  regex  regular expression
   * @param  regexFlags  regular expression flags
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalRegexRule(final String regex, final int regexFlags, final boolean reportAll)
  {
    pattern = Pattern.compile(regex, regexFlags);
    reportAllFailures = reportAll;
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
    final RuleResult result = new RuleResult();
    final Matcher m = pattern.matcher(passwordData.getPassword());
    final Set<String> matches = new HashSet<>();
    while (m.find()) {
      final String match = m.group();
      if (!matches.contains(match)) {
        result.addError(ERROR_CODE, createRuleResultDetailParameters(match));
        if (!reportAllFailures) {
          break;
        }
        matches.add(match);
      }
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

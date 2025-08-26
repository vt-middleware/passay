/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;

/**
 * Rule for determining if a password contains the desired complexity for a certain length. In order to meet the
 * criteria of this rule, passwords must meet all the supplied rules for a given password length.
 *
 * @author  Middleware Services
 */
public class LengthComplexityRule implements Rule
{

  /** Error code for insufficient complexity. */
  public static final String ERROR_CODE = "INSUFFICIENT_COMPLEXITY";

  /** Error code for missing complexity rules. */
  public static final String ERROR_CODE_RULES = "INSUFFICIENT_COMPLEXITY_RULES";

  /** Rules to apply when checking a password. */
  private final Map<Interval, List<Rule>> rules = new HashMap<>();

  /** Whether to report the details of this rule failure. */
  private final boolean reportFailure;

  /** Whether to report the details of each complexity rule failure. */
  private final boolean reportRuleFailures;


  public LengthComplexityRule(final Entry... intervals)
  {
    this(true, true, intervals);
  }


  public LengthComplexityRule(final Collection<Entry> intervals)
  {
    this(true, true, intervals);
  }


  public LengthComplexityRule(
    final boolean reportFailure, final boolean reportRuleFailures, final Entry... intervals)
  {
    this(
      reportFailure,
      reportRuleFailures,
      Arrays.asList(
        PassayUtils.assertNotNullArgOr(
          intervals,
          v -> v.length == 0 || Stream.of(v).anyMatch(Objects::isNull),
          "Intervals cannot be empty, null or contain null")));
  }


  public LengthComplexityRule(
    final boolean reportFailure, final boolean reportRuleFailures, final Collection<Entry> intervals)
  {
    PassayUtils.assertNotNullArgOr(
      intervals,
      v -> v.isEmpty() || v.stream().anyMatch(Objects::isNull),
      "Intervals cannot be empty, null or contain null");
    this.reportFailure = reportFailure;
    this.reportRuleFailures = reportRuleFailures;
    for (Entry entry : intervals) {
      final Interval i = new Interval(entry.getKey());
      for (Interval existingInterval : rules.keySet()) {
        if (existingInterval.intersects(i)) {
          throw new IllegalArgumentException("Interval " + i + " intersects existing interval " + existingInterval);
        }
      }
      this.rules.put(i, entry.getValue());
    }
  }


  /**
   * Returns whether to add the rule result detail of this rule to the rule result.
   *
   * @return  whether to add rule result detail of this rule
   */
  public boolean getReportFailure()
  {
    return reportFailure;
  }


  /**
   * Returns whether to add the rule result detail for each rule that fails to validate to the rule result.
   *
   * @return  whether to add rule result details
   */
  public boolean getReportRuleFailures()
  {
    return reportRuleFailures;
  }


  /**
   * Returns the password rules for this complexity rule.
   *
   * @return  unmodifiable map of password rules
   */
  public Map<Interval, List<? extends Rule>> getRules()
  {
    return Collections.unmodifiableMap(rules);
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final int passwordLength = passwordData.getCharacterCount();
    final List<Rule> rulesByLength = getRulesByLength(passwordLength);
    if (rulesByLength == null) {
      return new FailureRuleResult(
        new RuleResultDetail(ERROR_CODE_RULES, createRuleResultDetailParameters(passwordLength, 0, 0)));
    }
    int successCount = 0;
    final List<RuleResultDetail> details = new ArrayList<>();
    final List<RuleResultMetadata> metadata = new ArrayList<>();
    for (Rule rule : rulesByLength) {
      final RuleResult rr = rule.validate(passwordData);
      if (rr.isValid()) {
        successCount++;
      }
      if (reportRuleFailures) {
        details.addAll(rr.getDetails());
      }
      metadata.add(rr.getMetadata());
    }
    final boolean valid = successCount >= rulesByLength.size();
    if (!valid && reportFailure) {
      details.add(
        new RuleResultDetail(
          ERROR_CODE, createRuleResultDetailParameters(passwordLength, successCount, rulesByLength.size())));
    }
    return valid ?
      new SuccessRuleResult(new RuleResultMetadata(metadata)) :
      new FailureRuleResult(new RuleResultMetadata(metadata), details);
  }


  /**
   * Returns the rules that apply to the supplied password length.
   *
   * @param  length  of the password
   *
   * @return  rules or null if length is not included in any interval
   */
  private List<Rule> getRulesByLength(final int length)
  {
    return rules.entrySet().stream()
      .filter(e -> e.getKey().includes(length))
      .map(Map.Entry::getValue)
      .findFirst().orElse(null);
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  length  of the password
   * @param  success  number of successful rules
   * @param  ruleCount  number of total rules
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int length, final int success,
    final int ruleCount)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("passwordLength", length);
    m.put("successCount", success);
    m.put("ruleCount", ruleCount);
    return m;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "rules=" + rules + ", " +
      "reportFailure=" + reportFailure + ", " +
      "reportRuleFailures=" + reportRuleFailures;
  }


  /**
   * Class to contain a tuple of interval string to rules.
   */
  public static class Entry extends AbstractMap.SimpleImmutableEntry<String, List<Rule>>
  {


    /**
     * Creates a new entry.
     *
     * @param  interval  interval string
     * @param  rules  for the supplied interval
     */
    public Entry(final String interval, final Rule... rules)
    {
      super(
        interval,
        Arrays.asList(
          PassayUtils.assertNotNullArgOr(
            rules,
            v -> v.length == 0 || Stream.of(v).anyMatch(Objects::isNull),
            "Rules cannot be empty, null or contain null")));
    }


    /**
     * Creates a new entry.
     *
     * @param  interval  interval string
     * @param  rules  for the supplied interval
     */
    public Entry(final String interval, final List<Rule> rules)
    {
      super(
        interval,
        PassayUtils.assertNotNullArgOr(
          rules,
          v -> v.isEmpty() || v.stream().anyMatch(Objects::isNull),
          "Rules cannot be empty, null or contain null"));
    }
  }


  /**
   * Class that represents an interval of numbers and parses interval notation.
   */
  public static class Interval
  {

    /** Pattern for matching intervals. */
    private static final Pattern INTERVAL_PATTERN = Pattern.compile("^([(\\[])(\\d+),(\\d+|\\*)([)\\]])$");

    /** Interval pattern. */
    private final String boundsPattern;

    /** Lower bound of the interval (inclusive). */
    private final int lower;

    /** Upper bound of the interval (inclusive). */
    private final int upper;

    /**
     * Creates a new interval.
     *
     * @param  pattern  to parse
     */
    Interval(final String pattern)
    {
      final Matcher m = INTERVAL_PATTERN.matcher(pattern);
      if (!m.matches()) {
        throw new IllegalArgumentException("Invalid interval notation: " + pattern);
      }

      final String lowerType = m.group(1);
      final String lowerVal = m.group(2);
      final String upperVal = m.group(3);
      final String upperType = m.group(4);

      // parse the bounds and convert them to a closed (inclusive) interval
      lower = Integer.parseInt(lowerVal) + ("(".equals(lowerType) ? 1 : 0);
      upper = "*".equals(upperVal) ? Integer.MAX_VALUE : (Integer.parseInt(upperVal) - (")".equals(upperType) ? 1 : 0));
      boundsPattern = pattern;

      if (upper < lower) {
        throw new IllegalArgumentException("Invalid interval notation: " + pattern + " produced an empty set");
      }
    }


    /**
     * Returns whether this interval includes the supplied integer.
     *
     * @param  i  to test for inclusion
     *
     * @return  whether this interval includes the supplied integer
     */
    public boolean includes(final int i)
    {
      return lower <= i && i <= upper;
    }


    /**
     * Returns whether this interval intersects the supplied interval.
     *
     * @param  i  interval to test for intersection
     *
     * @return  whether this interval intersects the supplied interval
     */
    public boolean intersects(final Interval i)
    {
      return includes(i.lower) || includes(i.upper);
    }


    @Override
    public String toString()
    {
      return boundsPattern;
    }
  }
}

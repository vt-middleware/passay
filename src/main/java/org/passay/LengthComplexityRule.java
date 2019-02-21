/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private boolean reportFailure = true;

  /** Whether to report the details of each complexity rule failure. */
  private boolean reportRuleFailures = true;


  /**
   * Adds the rules to invoke for the supplied interval.
   *
   * @param  interval  of integers that the supplied rules apply to
   * @param  l  list of rules
   *
   * @throws  IllegalArgumentException  if the supplied rules are empty or null or if interval is invalid or intersects
   *                                    with an existing interval
   */
  public void addRules(final String interval, final List<Rule> l)
  {
    if (l == null || l.isEmpty()) {
      throw new IllegalArgumentException("Rules cannot be empty or null");
    }
    final Interval i = new Interval(interval);
    for (Interval existingInterval : rules.keySet()) {
      if (existingInterval.intersects(i)) {
        throw new IllegalArgumentException("Interval " + i + " intersects existing interval " + existingInterval);
      }
    }
    rules.put(i, l);
  }


  /**
   * Adds the rules to invoke for the supplied interval.
   *
   * @param  interval  of integers that the supplied rules apply to
   * @param  r  list of rules
   */
  public void addRules(final String interval, final Rule... r)
  {
    addRules(interval, r != null ? Arrays.asList(r) : null);
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
   * Sets whether to add the rule result detail of this rule to the rule result.
   *
   * @param  b  whether to add rule result detail of this rule
   */
  public void setReportFailure(final boolean b)
  {
    reportFailure = b;
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
   * Sets whether to add the rule result detail for each rule that fails to validate to the rule result.
   *
   * @param  b  whether to add rule result details
   */
  public void setReportRuleFailures(final boolean b)
  {
    reportRuleFailures = b;
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
    final int passwordLength = passwordData.getPassword().length();
    final List<Rule> rulesByLength = getRulesByLength(passwordLength);
    if (rulesByLength == null) {
      return new RuleResult(
        false,
        new RuleResultDetail(ERROR_CODE_RULES, createRuleResultDetailParameters(passwordLength, 0, 0)));
    }
    int successCount = 0;
    final RuleResult result = new RuleResult(true);
    for (Rule rule : rulesByLength) {
      final RuleResult rr = rule.validate(passwordData);
      if (!rr.isValid()) {
        if (reportRuleFailures) {
          result.getDetails().addAll(rr.getDetails());
        }
      } else {
        successCount++;
      }
      result.getMetadata().merge(rr.getMetadata());
    }
    if (successCount < rulesByLength.size()) {
      result.setValid(false);
      if (reportFailure) {
        result.getDetails().add(
          new RuleResultDetail(
            ERROR_CODE,
            createRuleResultDetailParameters(passwordLength, successCount, rulesByLength.size())));
      }
    }
    return result;
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
    final Optional<List<Rule>> match = rules.entrySet().stream().filter(
      e -> e.getKey().includes(length)).map(Map.Entry::getValue).findFirst();
    return match.isPresent() ? match.get() : null;
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
  protected Map<String, Object> createRuleResultDetailParameters(
    final int length,
    final int success,
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
    return
      String.format(
        "%s@%h::rules=%s,reportRuleFailures=%s",
        getClass().getName(),
        hashCode(),
        rules,
        reportRuleFailures);
  }


  /**
   * Class that represents an interval of numbers and parses interval notation.
   */
  public static class Interval
  {

    /** Type of bound type. */
    public enum BoundType {

      /** closed value. */
      CLOSED,

      /** open value. */
      OPEN;


      /**
       * Returns the enum associated with the supplied text. '[' and ']' return {@link #CLOSED}. '(' and ')' return
       * {@link #OPEN}.
       *
       * @param  text  to parse
       *
       * @return  bound type
       *
       * @throws  IllegalArgumentException  if text is not one of '[', ']', '(', ')'
       */
      public static BoundType parse(final String text)
      {
        if ("[".equals(text) || "]".equals(text)) {
          return CLOSED;
        }
        if ("(".equals(text) || ")".equals(text)) {
          return OPEN;
        }
        throw new IllegalArgumentException("Invalid interval notation: " + text);
      }
    }

    /** Pattern for matching intervals. */
    private static final Pattern INTERVAL_PATTERN = Pattern.compile("^([(|\\[])(\\d+),(\\d+|\\*)([)|\\]])$");

    /** Lower bound of the interval. */
    private final Bound lowerBound;

    /** Upper bound of the interval. */
    private final Bound upperBound;


    /**
     * Creates a new interval.
     *
     * @param  pattern  to parse
     */
    public Interval(final String pattern)
    {
      final Matcher m = INTERVAL_PATTERN.matcher(pattern);
      if (!m.matches()) {
        throw new IllegalArgumentException("Invalid interval notation: " + pattern);
      }

      final String lowerType = m.group(1);
      final String lower = m.group(2);
      final String upper = m.group(3);
      final String upperType = m.group(4);

      lowerBound = new Bound(Integer.parseInt(lower), BoundType.parse(lowerType));
      upperBound = new Bound(
        "*".equals(upper) ? Integer.MAX_VALUE : Integer.parseInt(upper),
        BoundType.parse(upperType));

      if (getUpperBoundClosed() - getLowerBoundClosed() < 0) {
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
      boolean includes = false;
      if (i >= lowerBound.value && i <= upperBound.value) {
        if (i > lowerBound.value && i < upperBound.value) {
          includes = true;
        } else if (i == lowerBound.value && lowerBound.isClosed()) {
          includes = true;
        } else if (i == upperBound.value && upperBound.isClosed()) {
          includes = true;
        }
      }
      return includes;
    }


    /**
     * Returns whether this interval includes the supplied interval.
     *
     * @param  i  interval to test for inclusion
     *
     * @return  whether this interval includes the supplied interval
     */
    public boolean includes(final Interval i)
    {
      return includes(i.getLowerBoundClosed()) && includes(i.getUpperBoundClosed());
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
      return includes(i.getLowerBoundClosed()) || includes(i.getUpperBoundClosed());
    }


    /**
     * Returns the closed lower bound for this interval.
     *
     * @return  closed lower bound
     */
    private int getLowerBoundClosed()
    {
      return lowerBound.isClosed() ? lowerBound.value : lowerBound.value + 1;
    }


    /**
     * Returns the closed upper bound for this interval.
     *
     * @return  closed upper bound
     */
    private int getUpperBoundClosed()
    {
      return upperBound.isClosed() ? upperBound.value : upperBound.value - 1;
    }


    @Override
    public boolean equals(final Object o)
    {
      if (o == this) {
        return true;
      }
      if (o != null && getClass() == o.getClass())  {
        final Interval other = (Interval) o;
        return lowerBound.equals(other.lowerBound) && upperBound.equals(other.upperBound);
      }
      return false;
    }


    @Override
    public int hashCode()
    {
      return Objects.hash(lowerBound, upperBound);
    }


    @Override
    public String toString()
    {
      return
        String.format(
          "%s%s,%s%s",
          lowerBound.isClosed() ? '[' : '(',
          lowerBound.value,
          upperBound.value,
          upperBound.isClosed() ? ']' : ')');
    }


    /**
     * Class that represents a single value in an interval.
     */
    private class Bound
    {

      /** Value in an interval. */
      private final int value;

      /** Bound type in an interval. */
      private final BoundType type;


      /**
       * Creates a new bound.
       *
       * @param  i  value
       * @param  bt  bound type
       */
      Bound(final int i, final BoundType bt)
      {
        value = i;
        type = bt;
      }


      /**
       * Whether this bound is closed.
       *
       * @return  whether this bound is closed
       */
      public boolean isClosed()
      {
        return BoundType.CLOSED == type;
      }


      /**
       * Whether this bound is open.
       *
       * @return  whether this bound is open
       */
      public boolean isOpen()
      {
        return BoundType.OPEN == type;
      }


      @Override
      public boolean equals(final Object o)
      {
        if (o == this) {
          return true;
        }
        if (o != null && getClass() == o.getClass())  {
          final Bound other = (Bound) o;
          return value == other.value && type == other.type;
        }
        return false;
      }


      @Override
      public int hashCode()
      {
        return Objects.hash(value, type);
      }
    }
  }
}

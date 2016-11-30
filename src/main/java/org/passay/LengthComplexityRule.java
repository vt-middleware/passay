/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
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
 * criteria of this rule, passwords must meet any number of supplied rules.
 *
 * @author  Middleware Services
 */
public class LengthComplexityRule implements Rule
{

  /** Error code for insufficient number of characteristics. */
  public static final String ERROR_CODE = "INSUFFICIENT_COMPLEXITY";

  /** Rules to apply when checking a password. */
  private final Map<Range, List<Rule>> rules = new HashMap<>();

  /** Whether to report the details of each character rule failure. */
  private boolean reportRuleFailures = true;


  /**
   * Sets the rules used by this rule.
   *
   * @param  range  of integers that the supplied rules apply to
   * @param  l  list of rules
   *
   * @throws  IllegalArgumentException  if the supplied rules are empty or null or if range is invalid or intersects
   *                                    with an existing range
   */
  public void addRules(final String range, final List<Rule> l)
  {
    if (l == null || l.isEmpty()) {
      throw new IllegalArgumentException("Rules cannot be empty or null");
    }
    final Range r = new Range(range);
    for (Range existingRange : rules.keySet()) {
      if (existingRange.intersects(r)) {
        throw new IllegalArgumentException("Range " + r + " intersects existing range " + existingRange);
      }
    }
    rules.put(r, l);
  }


  /**
   * Sets the rules used by this rule.
   *
   * @param  range  of integers that the supplied rules apply to
   * @param  r  list of rules
   */
  public void addRules(final String range, final Rule... r)
  {
    addRules(range, r != null ? Arrays.asList(r) : null);
  }


  /**
   * Returns whether to add the rule result detail for each character rule that fails to validate to the rule result.
   *
   * @return  whether to add character rule result details
   */
  public boolean getReportRuleFailures()
  {
    return reportRuleFailures;
  }


  /**
   * Sets whether to add the rule result detail for each character rule that fails to validate to the rule result.
   *
   * @param  b  whether to add character rule result details
   */
  public void setReportRuleFailures(final boolean b)
  {
    reportRuleFailures = b;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final List<Rule> rulesByLength = getRulesByLength(passwordData.getPassword().length());
    if (rulesByLength == null) {
      throw new IllegalStateException(
        "No rules have been configured for password length " + passwordData.getPassword().length());
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
    }
    if (successCount < rulesByLength.size()) {
      result.setValid(false);
      result.getDetails().add(
        new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(successCount, rulesByLength.size())));
    }
    return result;
  }


  /**
   * Returns the rules that apply to the supplied password length.
   *
   * @param  length  of the password
   *
   * @return  rules or null if length is not included in any range
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
   * @param  success  number of successful rules
   * @param  ruleCount  number of total rules
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int success, final int ruleCount)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
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
   * Class that represents a range of numbers and parses interval notation.
   */
  protected static class Range
  {

    /** Type of range value. */
    public enum BoundType {

      /** inclusive value. */
      INCLUSIVE,

      /** exclusive value. */
      EXCLUSIVE;


      /**
       * Returns the enum associated with the supplied text. '[' and ']' return {@link #INCLUSIVE}. '(' and ')' return
       * {@link #EXCLUSIVE}.
       *
       * @param  text  to parse
       *
       * @return  range value type
       *
       * @throws  IllegalArgumentException  if text is not one of '[', ']', '(', ')'
       */
      public static BoundType parse(final String text)
      {
        if ("[".equals(text) || "]".equals(text)) {
          return INCLUSIVE;
        }
        if ("(".equals(text) || ")".equals(text)) {
          return EXCLUSIVE;
        }
        throw new IllegalArgumentException("Invalid interval notation: " + text);
      }
    }

    /** Pattern for matching intervals. */
    private static final Pattern INTERVAL_PATTERN = Pattern.compile("^([\\(|\\[])(\\d+),(\\d+)([\\)|\\]])$");

    /** Lower bound of the range. */
    private final Bound lowerBound;

    /** Upper bound of the range. */
    private final Bound upperBound;


    /**
     * Creates a new range.
     *
     * @param  pattern  to parse
     */
    public Range(final String pattern)
    {
      final Matcher m = INTERVAL_PATTERN.matcher(pattern);
      if (!m.matches()) {
        throw new IllegalArgumentException("Invalid interval notation: " + pattern);
      }

      final String lowerType = m.group(1);
      final int lower = Integer.parseInt(m.group(2));
      final int upper = Integer.parseInt(m.group(3));
      final String upperType = m.group(4);

      lowerBound = new Bound(lower, BoundType.parse(lowerType));
      upperBound = new Bound(upper, BoundType.parse(upperType));
    }


    /**
     * Returns whether this range includes the supplied integer.
     *
     * @param  i  to test for inclusion
     *
     * @return  whether this range includes the supplied integer
     */
    public boolean includes(final int i)
    {
      boolean includes = false;
      if (i >= lowerBound.value && i <= upperBound.value) {
        if (i > lowerBound.value && i < upperBound.value) {
          includes = true;
        } else if (i == lowerBound.value && lowerBound.isInclusive()) {
          includes = true;
        } else if (i == upperBound.value && upperBound.isInclusive()) {
          includes = true;
        }
      }
      return includes;
    }


    /**
     * Returns whether this range includes the supplied range.
     *
     * @param  r  range to test for inclusion
     *
     * @return  whether this range includes the supplied range
     */
    public boolean includes(final Range r)
    {
      return includes(r.getLowerBoundInclusive()) && includes(r.getUpperBoundInclusive());
    }


    /**
     * Returns whether this range intersects the supplied range.
     *
     * @param  r  range to test for intersection
     *
     * @return  whether this range intersects the supplied range
     */
    public boolean intersects(final Range r)
    {
      return includes(r.getLowerBoundInclusive()) || includes(r.getUpperBoundInclusive());
    }


    /**
     * Returns the inclusive lower bound for this range.
     *
     * @return  inclusive lower bound
     */
    private int getLowerBoundInclusive()
    {
      return lowerBound.isInclusive() ? lowerBound.value : lowerBound.value + 1;
    }


    /**
     * Returns the inclusive upper bound for this range.
     *
     * @return  inclusive upper bound
     */
    private int getUpperBoundInclusive()
    {
      return upperBound.isInclusive() ? upperBound.value : upperBound.value - 1;
    }


    @Override
    public boolean equals(final Object o)
    {
      if (o == this) {
        return true;
      }
      if (o != null && getClass() == o.getClass())  {
        final Range other = (Range) o;
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
          lowerBound.isInclusive() ? '[' : '(',
          lowerBound.value,
          upperBound.value,
          upperBound.isInclusive() ? ']' : ')');
    }


    /**
     * Class that represents a single value in a range.
     */
    private class Bound
    {

      /** Value in a range. */
      private final int value;

      /** Whether this value is inclusive. */
      private final BoundType type;


      /**
       * Creates a new range value.
       *
       * @param  i  value
       * @param  bt  inclusive or exclusive
       */
      Bound(final int i, final BoundType bt)
      {
        value = i;
        type = bt;
      }


      /**
       * Whether this range value in inclusive.
       *
       * @return  whether this range value in inclusive
       */
      public boolean isInclusive()
      {
        return BoundType.INCLUSIVE == type;
      }


      /**
       * Whether this range value in exclusive.
       *
       * @return  whether this range value in exclusive
       */
      public boolean isExclusive()
      {
        return BoundType.EXCLUSIVE == type;
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

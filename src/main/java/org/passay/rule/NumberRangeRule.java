/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.logic.MatchBehavior;
import org.passay.logic.PasswordData;
import org.passay.rule.result.RuleResult;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule for determining if a password contains any number within a defined range, lower inclusive, upper exclusive.
 *
 * @author  Middleware Services
 */
public class NumberRangeRule implements Rule
{

  /** Error code for whitespace rule violation. */
  public static final String ERROR_CODE = "ILLEGAL_NUMBER_RANGE";

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;

  /** Lower end of the range. */
  private final int lowerRange;

  /** Upper end of the range. */
  private final int upperRange;

  /** Where to match each number. */
  private final MatchBehavior matchBehavior;


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   */
  public NumberRangeRule(final int lower, final int upper)
  {
    this(lower, upper, MatchBehavior.Contains, true);
  }


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  reportAll  whether to report all matches or just the first
   */
  public NumberRangeRule(final int lower, final int upper, final boolean reportAll)
  {
    this(lower, upper, MatchBehavior.Contains, reportAll);
  }


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  behavior  how to match number range
   */
  public NumberRangeRule(final int lower, final int upper, final MatchBehavior behavior)
  {
    this(lower, upper, behavior, true);
  }


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  behavior  how to match number range
   * @param  reportAll  whether to report all matches or just the first
   */
  public NumberRangeRule(final int lower, final int upper, final MatchBehavior behavior, final boolean reportAll)
  {
    if (lower >= upper) {
      throw new IllegalArgumentException("lower must be less than upper");
    }
    lowerRange = lower;
    upperRange = upper;
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the lower range for this rule.
   *
   * @return  lower range
   */
  public int getLowerRange()
  {
    return lowerRange;
  }


  /**
   * Returns the upper range for this rule.
   *
   * @return  upper range
   */
  public int getUpperRange()
  {
    return upperRange;
  }


  /**
   * Returns the match behavior for this rule.
   *
   * @return  match behavior
   */
  public MatchBehavior getMatchBehavior()
  {
    return matchBehavior;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult();
    final String text = passwordData.getPassword();
    for (int i = lowerRange; i < upperRange; i++) {
      if (matchBehavior.match(text, Integer.toString(i))) {
        result.addError(ERROR_CODE, createRuleResultDetailParameters(i));
        if (!reportAllFailures) {
          break;
        }
      }
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  number  matching number
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int number)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("number", number);
    m.put("matchBehavior", matchBehavior);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::lowerRange=%s,upperRange=%s,matchBehavior=%s",
        getClass().getName(),
        hashCode(),
        lowerRange,
        upperRange,
        matchBehavior);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

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
  private final StringMatch stringMatchType;


  /**
   * Creates a new number range rule using {@link StringMatch#Contains}.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   */
  public NumberRangeRule(final int lower, final int upper)
  {
    this(lower, upper, StringMatch.Contains, true);
  }


  /**
   * Creates a new number range rule using {@link StringMatch#Contains}.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  reportAll  whether to report all matches or just the first
   */
  public NumberRangeRule(final int lower, final int upper, final boolean reportAll)
  {
    this(lower, upper, StringMatch.Contains, reportAll);
  }


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  match  match type
   */
  public NumberRangeRule(final int lower, final int upper, final StringMatch match)
  {
    this(lower, upper, match, true);
  }


  /**
   * Creates a new number range rule.
   *
   * @param  lower  end of the number range, inclusive
   * @param  upper  end of the number range, exclusive
   * @param  match  match type
   * @param  reportAll  whether to report all matches or just the first
   */
  public NumberRangeRule(final int lower, final int upper, final StringMatch match, final boolean reportAll)
  {
    if (lower >= upper) {
      throw new IllegalArgumentException("lower must be less than upper");
    }
    lowerRange = lower;
    upperRange = upper;
    stringMatchType = match;
    reportAllFailures = reportAll;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final String text = passwordData.getPassword();
    for (int i = lowerRange; i < upperRange; i++) {
      if (stringMatchType.match(text, Integer.toString(i))) {
        result.setValid(false);
        result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(i)));
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
    m.put("stringMatchType", stringMatchType);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::lowerRange=%s,upperRange=%s,stringMatchType=%s",
        getClass().getName(),
        hashCode(),
        lowerRange,
        upperRange,
        stringMatchType);
  }
}

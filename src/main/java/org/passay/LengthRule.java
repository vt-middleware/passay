/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule for determining if a password is within a desired length. The minimum and maximum lengths are used inclusively
 * to determine if a password meets this rule.
 *
 * @author  Middleware Services
 */
public class LengthRule implements Rule
{

  /** Error code for password too short. */
  public static final String ERROR_CODE_MIN = "TOO_SHORT";

  /** Error code for password too long. */
  public static final String ERROR_CODE_MAX = "TOO_LONG";

  /** Stores the minimum length of a password. */
  private int minimumLength;

  /** Stores the maximum length of a password. */
  private int maximumLength;


  /** Creates a new length rule with lengths unset. The defaults are 0 and Integer.MAX_VALUE respectively. */
  public LengthRule()
  {
    this(0, Integer.MAX_VALUE);
  }


  /**
   * Creates a new length rule with the supplied length. Both the minimum and the maximum length will be set to this
   * value.
   *
   * @param  length  length of password
   */
  public LengthRule(final int length)
  {
    this(length, length);
  }


  /**
   * Create a new length rule.
   *
   * @param  minLength  minimum length of a password
   * @param  maxLength  maximum length of a password
   */
  public LengthRule(final int minLength, final int maxLength)
  {
    minimumLength = minLength;
    maximumLength = maxLength;
  }


  /**
   * Sets the minimum password length.
   *
   * @param  minLength  minimum length of a password
   */
  public void setMinimumLength(final int minLength)
  {
    minimumLength = minLength;
  }


  /**
   * Returns the minimum password length.
   *
   * @return  minimum password length
   */
  public int getMinimumLength()
  {
    return minimumLength;
  }


  /**
   * Sets the maximum password length.
   *
   * @param  maxLength  maximum length of a password
   */
  public void setMaximumLength(final int maxLength)
  {
    maximumLength = maxLength;
  }


  /**
   * Returns the maximum password length.
   *
   * @return  maximum length of a password
   */
  public int getMaximumLength()
  {
    return maximumLength;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult();
    final int length = passwordData.getPassword().length();
    if (length >= minimumLength && length <= maximumLength) {
      result.setValid(true);
    } else {
      result.setValid(false);
      if (length < minimumLength) {
        result.getDetails().add(new RuleResultDetail(ERROR_CODE_MIN, createRuleResultDetailParameters()));
      } else {
        result.getDetails().add(new RuleResultDetail(ERROR_CODE_MAX, createRuleResultDetailParameters()));
      }
    }
    result.setMetadata(createRuleResultMetadata(passwordData));
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
    m.put("minimumLength", minimumLength);
    m.put("maximumLength", maximumLength);
    return m;
  }


  /**
   * Creates the rule result metadata.
   *
   * @param  password  data used for metadata creation
   *
   * @return  rule result metadata
   */
  protected RuleResultMetadata createRuleResultMetadata(final PasswordData password)
  {
    return new RuleResultMetadata(RuleResultMetadata.CountCategory.Length, password.getPassword().length());
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::minimumLength=%s,maximumLength=%s",
        getClass().getName(),
        hashCode(),
        minimumLength,
        maximumLength);
  }
}

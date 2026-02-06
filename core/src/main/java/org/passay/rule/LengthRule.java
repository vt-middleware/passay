/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.LinkedHashMap;
import java.util.Map;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;

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
  private final int minimumLength;

  /** Stores the maximum length of a password. */
  private final int maximumLength;


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
   * Returns the minimum password length.
   *
   * @return  minimum password length
   */
  public int getMinimumLength()
  {
    return minimumLength;
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
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final int length = passwordData.getPassword().codePointCount();
    if (length < minimumLength) {
      return new FailureRuleResult(
        createRuleResultMetadata(passwordData),
        new RuleResultDetail(ERROR_CODE_MIN, createRuleResultDetailParameters()));
    } else if (length > maximumLength) {
      return new FailureRuleResult(
        createRuleResultMetadata(passwordData),
        new RuleResultDetail(ERROR_CODE_MAX, createRuleResultDetailParameters()));
    }
    return new SuccessRuleResult(createRuleResultMetadata(passwordData));
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
    return new RuleResultMetadata(RuleResultMetadata.CountCategory.Length, password.getPassword().codePointCount());
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "minimumLength=" + minimumLength + ", " +
      "maximumLength=" + maximumLength;
  }
}

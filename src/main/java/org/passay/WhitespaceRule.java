/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains whitespace characters.
 *
 * @author  Middleware Services
 */
public class WhitespaceRule implements Rule
{

  /** Error code for whitespace rule violation. */
  public static final String ERROR_CODE = "ILLEGAL_WHITESPACE";

  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    if (!passwordData.getPassword().containsWhitespace()) {
      return new RuleResult(true);
    } else {
      return new RuleResult(false, new RuleResultDetail(ERROR_CODE, null));
    }
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.LinkedHashMap;
import java.util.Map;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Rule for determining if a password contains the username associated with that password.  This rule returns true if a
 * supplied {@link PasswordData} returns a null or empty username.
 *
 * @author  Middleware Services
 */
public class UsernameRule implements Rule
{

  /** Error code for matching username. */
  public static final String ERROR_CODE = "ILLEGAL_USERNAME";

  /** Error code for matching reversed dictionary word. */
  public static final String ERROR_CODE_REVERSED = "ILLEGAL_USERNAME_REVERSED";

  /** Whether to search for username backwards. */
  private final boolean matchBackwards;

  /** Whether to ignore case when checking for usernames. */
  private final boolean ignoreCase;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /** Default constructor. */
  public UsernameRule()
  {
    this(false, false, MatchBehavior.Contains);
  }


  /**
   * Create a new username rule.
   *
   * @param  behavior  how to match username
   */
  public UsernameRule(final MatchBehavior behavior)
  {
    this(false, false, behavior);
  }


  /**
   * Create a new username rule.
   *
   * @param  matchBackwards  whether to match backwards
   * @param  ignoreCase  whether to ignore case
   */
  public UsernameRule(final boolean matchBackwards, final boolean ignoreCase)
  {
    this(matchBackwards, ignoreCase, MatchBehavior.Contains);
  }


  /**
   * Create a new username rule.
   *
   * @param  matchBackwards  whether to match backwards
   * @param  ignoreCase  whether to ignore case
   * @param  behavior  how to match username
   */
  public UsernameRule(final boolean matchBackwards, final boolean ignoreCase, final MatchBehavior behavior)
  {
    this.matchBackwards = matchBackwards;
    this.ignoreCase = ignoreCase;
    matchBehavior = behavior;
  }


  /**
   * Returns whether to match the username backwards.
   *
   * @return  whether to match username backwards
   */
  public boolean isMatchBackwards()
  {
    return matchBackwards;
  }


  /**
   * Returns whether to ignore the case of the username.
   *
   * @return  whether to ignore case
   */
  public boolean isIgnoreCase()
  {
    return ignoreCase;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final RuleResult result = new RuleResult();
    String user = passwordData.getUsername();
    if (user != null && !user.isEmpty()) {
      String text = passwordData.getPassword();
      if (ignoreCase) {
        text = text.toLowerCase();
        user = user.toLowerCase();
      }
      if (matchBehavior.match(text, user)) {
        final String[] codes = {
          ERROR_CODE + "." + matchBehavior.upperSnakeName(),
          ERROR_CODE,
        };
        result.addError(codes, createRuleResultDetailParameters(user));
      }
      if (matchBackwards) {
        final String reverseUser = new StringBuilder(user).reverse().toString();
        if (matchBehavior.match(text, reverseUser)) {
          final String[] codes = {
            ERROR_CODE_REVERSED + "." + matchBehavior.upperSnakeName(),
            ERROR_CODE_REVERSED,
          };
          result.addError(codes, createRuleResultDetailParameters(user));
        }
      }
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  username  matching username
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String username)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("username", username);
    m.put("matchBehavior", matchBehavior);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::ignoreCase=%s,matchBackwards=%s,matchBehavior=%s",
        getClass().getName(),
        hashCode(),
        ignoreCase,
        matchBackwards,
        matchBehavior);
  }
}

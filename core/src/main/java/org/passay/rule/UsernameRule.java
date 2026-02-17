/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SuccessRuleResult;
import org.passay.UnicodeString;

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

  /** Where to match username. */
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
    this.matchBehavior = behavior;
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
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<RuleResultDetail> details = new ArrayList<>();
    if (passwordData.getUsername() != null && !passwordData.getUsername().isEmpty()) {
      UnicodeString text = UnicodeString.copy(passwordData.getPassword());
      UnicodeString user = UnicodeString.copy(passwordData.getUsername());
      try {
        if (ignoreCase) {
          text = text.toLowerCase(true);
          user = user.toLowerCase(true);
        }
        if (matchBehavior.match(text, user)) {
          final String[] codes = {
            ERROR_CODE + "." + matchBehavior.upperSnakeName(),
            ERROR_CODE,
          };
          details.add(new RuleResultDetail(codes, createRuleResultDetailParameters(user.toString())));
        }
        if (matchBackwards) {
          final UnicodeString reverseUser = user.reverse();
          try {
            if (matchBehavior.match(text, reverseUser)) {
              final String[] codes = {
                ERROR_CODE_REVERSED + "." + matchBehavior.upperSnakeName(),
                ERROR_CODE_REVERSED,
              };
              details.add(new RuleResultDetail(codes, createRuleResultDetailParameters(user.toString())));
            }
          } finally {
            reverseUser.clear();
          }
        }
      } finally {
        text.clear();
        user.clear();
      }
    }
    return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  username  matching username
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final CharSequence username)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("username", username);
    m.put("matchBehavior", matchBehavior);
    return m;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "matchBackwards=" + matchBackwards + ", "+
      "ignoreCase=" + ignoreCase + ", " +
      "matchBehavior=" + matchBehavior;
  }
}

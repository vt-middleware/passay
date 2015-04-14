/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule for determining if a password contains the username associated with that password.
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
  private boolean matchBackwards;

  /** Whether to ignore case when checking for usernames. */
  private boolean ignoreCase;


  /** Default constructor. */
  public UsernameRule() {}


  /**
   * Create a new username rule.
   *
   * @param  mb  whether to match backwards
   * @param  ic  whether to ignore case
   */
  public UsernameRule(final boolean mb, final boolean ic)
  {
    setMatchBackwards(mb);
    setIgnoreCase(ic);
  }


  /**
   * Sets whether the verify method will search the password for the username spelled backwards as well as forwards.
   *
   * @param  b  whether to match username backwards
   */
  public void setMatchBackwards(final boolean b)
  {
    matchBackwards = b;
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
   * Sets whether the verify method will ignore case when searching the for a username.
   *
   * @param  b  whether to ignore case
   */
  public void setIgnoreCase(final boolean b)
  {
    ignoreCase = b;
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
    final RuleResult result = new RuleResult(true);
    String text = passwordData.getPassword();
    String user = passwordData.getUsername();
    String reverseUser = new StringBuilder(user).reverse().toString();
    if (ignoreCase) {
      text = text.toLowerCase();
      user = user.toLowerCase();
      reverseUser = reverseUser.toLowerCase();
    }
    if (text.contains(user)) {
      result.setValid(false);
      result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(user)));
    }
    if (matchBackwards && text.contains(reverseUser)) {
      result.setValid(false);
      result.getDetails().add(new RuleResultDetail(ERROR_CODE_REVERSED, createRuleResultDetailParameters(user)));
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
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::ignoreCase=%s,matchBackwards=%s",
        getClass().getName(),
        hashCode(),
        ignoreCase,
        matchBackwards);
  }
}

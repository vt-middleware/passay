/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule for determining if a password contains whitespace characters. Whitespace is defined as tab (0x09), line feed
 * (0x0A), vertical tab (0x0B), form feed (0x0C), carriage return (0x0D), and space (0x20).
 *
 * @author  Middleware Services
 */
public class WhitespaceRule implements Rule
{

  /** Error code for whitespace rule violation. */
  public static final String ERROR_CODE = "ILLEGAL_WHITESPACE";

  /** Characters: TAB,LF,VT,FF,CR,Space. */
  protected static final char[] CHARS = new char[] {
    (byte) 0x09,
    (byte) 0x0A,
    (byte) 0x0B,
    (byte) 0x0C,
    (byte) 0x0D,
    (byte) 0x20,
  };

  /** Whether to report all whitespace matches or just the first. */
  protected boolean reportAllFailures;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Creates a new whitespace rule.
   */
  public WhitespaceRule()
  {
    this(MatchBehavior.Contains);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  behavior  how to match whitespace
   */
  public WhitespaceRule(final MatchBehavior behavior)
  {
    this(behavior, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  behavior  how to match whitespace
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final MatchBehavior behavior, final boolean reportAll)
  {
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final String text = passwordData.getPassword();
    for (char c : CHARS) {
      if (matchBehavior.match(text, c)) {
        result.setValid(false);
        result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(c)));
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
   * @param  c  whitespace character
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final char c)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("whitespaceCharacter", c);
    m.put("matchBehavior", matchBehavior);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::reportAllFailures=%s,matchBehavior=%s",
        getClass().getName(),
        hashCode(),
        reportAllFailures,
        matchBehavior);
  }
}

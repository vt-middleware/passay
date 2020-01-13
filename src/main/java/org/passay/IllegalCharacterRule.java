/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Rule for determining if a password contains an illegal character. Validation will fail if the password contains any
 * of the illegal characters.
 *
 * @author  Middleware Services
 */
public class IllegalCharacterRule implements Rule
{

  /** Error code for illegal character failures. */
  public static final String ERROR_CODE = "ILLEGAL_CHAR";

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;

  /** Stores the characters that are not allowed. */
  private final char[] illegalCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Create a new illegal character rule.
   *
   * @param  c  illegal characters
   */
  public IllegalCharacterRule(final char[] c)
  {
    this(c, MatchBehavior.Contains, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  c  illegal characters
   * @param  behavior  how to match illegal characters
   */
  public IllegalCharacterRule(final char[] c, final MatchBehavior behavior)
  {
    this(c, behavior, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  c  illegal characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final char[] c, final boolean reportAll)
  {
    this(c, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  c  illegal characters
   * @param  behavior  how to match illegal characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final char[] c, final MatchBehavior behavior, final boolean reportAll)
  {
    if (c.length > 0) {
      illegalCharacters = c;
    } else {
      throw new IllegalArgumentException("illegal characters length must be greater than zero");
    }
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the illegal characters for this rule.
   *
   * @return  illegal characters
   */
  public char[] getIllegalCharacters()
  {
    return illegalCharacters;
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
    final Set<Character> matches = new HashSet<>();
    final String text = passwordData.getPassword();
    for (char c : illegalCharacters) {
      if (matchBehavior.match(text, c) && !matches.contains(c)) {
        result.addError(createRuleResultDetailParameters(c), ERROR_CODE + "." + (int) c, ERROR_CODE);
        if (!reportAllFailures) {
          break;
        }
        matches.add(c);
      }
    }
    result.setMetadata(createRuleResultMetadata(passwordData));
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  c  illegal character
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final char c)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("illegalCharacter", c);
    m.put("matchBehavior", matchBehavior);
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
    return new RuleResultMetadata(
      RuleResultMetadata.CountCategory.Illegal,
      PasswordUtils.countMatchingCharacters(String.valueOf(illegalCharacters), password.getPassword()));
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::reportAllFailures=%s,matchBehavior=%s,illegalCharacters=%s",
        getClass().getName(),
        hashCode(),
        reportAllFailures,
        matchBehavior,
        Arrays.toString(illegalCharacters));
  }
}

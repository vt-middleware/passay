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

  /** Stores the character code points that are not allowed. */
  private final int[] illegalCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Create a new illegal character rule.
   *
   * @param  codePoints  illegal character code points
   */
  public IllegalCharacterRule(final CodePoints codePoints)
  {
    this(codePoints, MatchBehavior.Contains, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  codePoints  illegal character code points
   * @param  behavior  how to match illegal characters
   */
  public IllegalCharacterRule(final CodePoints codePoints, final MatchBehavior behavior)
  {
    this(codePoints, behavior, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  codePoints  illegal character code points
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final CodePoints codePoints, final boolean reportAll)
  {
    this(codePoints, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  codePoints  illegal character code points
   * @param  behavior  how to match illegal characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final CodePoints codePoints, final MatchBehavior behavior, final boolean reportAll)
  {
    illegalCharacters = codePoints.getCodePoints();
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the illegal character code points for this rule.
   *
   * @return  illegal character code points
   */
  public CodePoints getIllegalCharacters()
  {
    return new CodePoints(illegalCharacters);
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
    final Set<String> matches = new HashSet<>();
    final String text = passwordData.getPassword();
    for (int cp : illegalCharacters) {
      if (matchBehavior.match(text, cp) && !matches.contains(CodePoints.toString(cp))) {
        final String[] codes = {
          ERROR_CODE + "." + cp,
          ERROR_CODE + "." + matchBehavior.upperSnakeName(),
          ERROR_CODE,
        };
        result.addError(codes, createRuleResultDetailParameters(cp));
        if (!reportAllFailures) {
          break;
        }
        matches.add(CodePoints.toString(cp));
      }
    }
    result.setMetadata(createRuleResultMetadata(passwordData));
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  cp  illegal character code point
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int cp)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("illegalCharacter", CodePoints.toString(cp));
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
      CodePoints.countMatchingCharacters(illegalCharacters, password.getPassword()));
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

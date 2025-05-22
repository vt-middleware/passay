/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Rule for determining if a password contains allowed characters. Validation will fail unless the password contains
 * only allowed characters.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRule implements Rule
{

  /** Error code for allowed character failures. */
  public static final String ERROR_CODE = "ALLOWED_CHAR";

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;

  /** Stores the character code points that are allowed. */
  private final int[] allowedCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Create a new allowed character rule.
   *
   * @param  codePoints  allowed characters
   */
  public AllowedCharacterRule(final CodePoints codePoints)
  {
    this(codePoints, MatchBehavior.Contains, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  codePoints  allowed characters
   * @param  behavior  how to match allowed characters
   */
  public AllowedCharacterRule(final CodePoints codePoints, final MatchBehavior behavior)
  {
    this(codePoints, behavior, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  codePoints  allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final CodePoints codePoints, final boolean reportAll)
  {
    this(codePoints, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  codePoints  allowed characters
   * @param  behavior  how to match allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final CodePoints codePoints, final MatchBehavior behavior, final boolean reportAll)
  {
    allowedCharacters = codePoints.getCodePoints();
    Arrays.sort(allowedCharacters);
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the allowed characters for this rule.
   *
   * @return  allowed character code points
   */
  public CodePoints getAllowedCharacters()
  {
    return new CodePoints(allowedCharacters);
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
    for (int cp : text.codePoints().toArray()) {
      if (Arrays.binarySearch(allowedCharacters, cp) < 0 && !matches.contains(CodePoints.toString(cp))) {
        if (MatchBehavior.Contains.equals(matchBehavior) || matchBehavior.match(text, cp)) {
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
      RuleResultMetadata.CountCategory.Allowed,
      CodePoints.countMatchingCharacters(allowedCharacters, password.getPassword()));
  }


  @Override
  public String toString()
  {
    return
      String.format("%s@%h::reportAllFailures=%s,matchBehavior=%s,allowedCharacters=%s",
        getClass().getName(),
        hashCode(),
        reportAllFailures,
        matchBehavior,
        Arrays.toString(allowedCharacters));
  }
}

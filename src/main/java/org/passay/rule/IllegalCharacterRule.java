/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;
import org.passay.UnicodeString;

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
  protected final boolean reportAllFailures;

  /** Stores the character code points that are not allowed. */
  private final int[] illegalCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Create a new illegal character rule.
   *
   * @param  unicodeString  illegal character code points
   */
  public IllegalCharacterRule(final UnicodeString unicodeString)
  {
    this(unicodeString, MatchBehavior.Contains, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  unicodeString  illegal character code points
   * @param  behavior  how to match illegal characters
   */
  public IllegalCharacterRule(final UnicodeString unicodeString, final MatchBehavior behavior)
  {
    this(unicodeString, behavior, true);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  unicodeString  illegal character code points
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final UnicodeString unicodeString, final boolean reportAll)
  {
    this(unicodeString, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new illegal character rule.
   *
   * @param  unicodeString  illegal character code points
   * @param  behavior  how to match illegal characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public IllegalCharacterRule(final UnicodeString unicodeString, final MatchBehavior behavior, final boolean reportAll)
  {
    illegalCharacters = unicodeString.toCodePointArray();
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the illegal character code points for this rule.
   *
   * @return  illegal character code points
   */
  public UnicodeString getIllegalCharacters()
  {
    return new UnicodeString(illegalCharacters);
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
    final Set<String> matches = new HashSet<>();
    final UnicodeString text = passwordData.getPassword();
    for (int cp : illegalCharacters) {
      if (matchBehavior.match(text, cp) && !matches.contains(PassayUtils.toString(cp))) {
        final String[] codes = {
          ERROR_CODE + "." + cp,
          ERROR_CODE + "." + matchBehavior.upperSnakeName(),
          ERROR_CODE,
        };
        details.add(new RuleResultDetail(codes, createRuleResultDetailParameters(cp)));
        if (!reportAllFailures) {
          break;
        }
        matches.add(PassayUtils.toString(cp));
      }
    }
    return details.isEmpty() ?
      new SuccessRuleResult(createRuleResultMetadata(passwordData)) :
      new FailureRuleResult(createRuleResultMetadata(passwordData), details);
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  codePoint  illegal character code point
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int codePoint)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("illegalCharacter", PassayUtils.toString(codePoint));
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
      password.getPassword().countMatchingCodePoints(illegalCharacters));
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "reportAllFailures=" + reportAllFailures + ", " +
      "illegalCharacters=" + Arrays.toString(illegalCharacters) + ", " +
      "matchBehavior=" + matchBehavior;
  }
}

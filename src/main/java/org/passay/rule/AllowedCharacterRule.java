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
  protected final boolean reportAllFailures;

  /** Stores the character code points that are allowed. */
  private final int[] allowedCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Create a new allowed character rule.
   *
   * @param  unicodeString  allowed characters
   */
  public AllowedCharacterRule(final UnicodeString unicodeString)
  {
    this(unicodeString, MatchBehavior.Contains, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  unicodeString  allowed characters
   * @param  behavior  how to match allowed characters
   */
  public AllowedCharacterRule(final UnicodeString unicodeString, final MatchBehavior behavior)
  {
    this(unicodeString, behavior, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  unicodeString  allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final UnicodeString unicodeString, final boolean reportAll)
  {
    this(unicodeString, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  unicodeString  allowed characters
   * @param  behavior  how to match allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final UnicodeString unicodeString, final MatchBehavior behavior, final boolean reportAll)
  {
    allowedCharacters = unicodeString.getCodePoints();
    Arrays.sort(allowedCharacters);
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the allowed characters for this rule.
   *
   * @return  allowed character code points
   */
  public UnicodeString getAllowedCharacters()
  {
    return new UnicodeString(allowedCharacters);
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
    final String text = passwordData.getPassword();
    for (int cp : text.codePoints().toArray()) {
      if (Arrays.binarySearch(allowedCharacters, cp) < 0 && !matches.contains(UnicodeString.toString(cp))) {
        if (MatchBehavior.Contains.equals(matchBehavior) || matchBehavior.match(text, cp)) {
          final String[] codes = {
            ERROR_CODE + "." + cp,
            ERROR_CODE + "." + matchBehavior.upperSnakeName(),
            ERROR_CODE,
          };
          details.add(new RuleResultDetail(codes, createRuleResultDetailParameters(cp)));
          if (!reportAllFailures) {
            break;
          }
          matches.add(UnicodeString.toString(cp));
        }
      }
    }
    return details.isEmpty() ?
      new SuccessRuleResult(createRuleResultMetadata(passwordData)) :
      new FailureRuleResult(createRuleResultMetadata(passwordData), details);
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
    m.put("illegalCharacter", UnicodeString.toString(cp));
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
      UnicodeString.countMatchingCharacters(allowedCharacters, password.getPassword()));
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

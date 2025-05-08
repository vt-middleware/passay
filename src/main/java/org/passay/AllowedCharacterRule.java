/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.CharBuffer;
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
   * @param  c  allowed characters
   */
  public AllowedCharacterRule(final char[] c)
  {
    this(c, MatchBehavior.Contains, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  cp  allowed character code points
   */
  public AllowedCharacterRule(final int[] cp)
  {
    this(cp, MatchBehavior.Contains, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  behavior  how to match allowed characters
   */
  public AllowedCharacterRule(final char[] c, final MatchBehavior behavior)
  {
    this(c, behavior, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  cp  allowed character code points
   * @param  behavior  how to match allowed characters
   */
  public AllowedCharacterRule(final int[] cp, final MatchBehavior behavior)
  {
    this(cp, behavior, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final char[] c, final boolean reportAll)
  {
    this(c, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  cp  allowed character code points
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final int[] cp, final boolean reportAll)
  {
    this(cp, MatchBehavior.Contains, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  behavior  how to match allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final char[] c, final MatchBehavior behavior, final boolean reportAll)
  {
    this(CharBuffer.wrap(c).chars().toArray(), behavior, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  cp  allowed character code points
   * @param  behavior  how to match allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final int[] cp, final MatchBehavior behavior, final boolean reportAll)
  {
    if (cp.length > 0) {
      allowedCharacters = cp;
    } else {
      throw new IllegalArgumentException("allowed characters length must be greater than zero");
    }
    Arrays.sort(allowedCharacters);
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the allowed characters for this rule.
   *
   * @return  allowed character code points
   */
  public int[] getAllowedCharacters()
  {
    return allowedCharacters;
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
      if (Arrays.binarySearch(allowedCharacters, cp) < 0 && !matches.contains(PasswordUtils.toString(cp))) {
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
          matches.add(PasswordUtils.toString(cp));
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
    m.put("illegalCharacter", PasswordUtils.toString(cp));
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
      PasswordUtils.countMatchingCharacters(allowedCharacters, password.getPassword()));
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

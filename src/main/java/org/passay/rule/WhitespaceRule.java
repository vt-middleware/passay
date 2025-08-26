/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;
import org.passay.UnicodeString;

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
  protected static final UnicodeString CHARS = new UnicodeString(
    (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x20);

  /** Whether to report all whitespace matches or just the first. */
  protected final boolean reportAllFailures;

  /** Stores the whitespace characters that are allowed. */
  private final int[] whitespaceCharacters;

  /** Where to match whitespace. */
  private final MatchBehavior matchBehavior;


  /**
   * Creates a new whitespace rule.
   */
  public WhitespaceRule()
  {
    this(CHARS, MatchBehavior.Contains, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  behavior  how to match whitespace
   */
  public WhitespaceRule(final MatchBehavior behavior)
  {
    this(CHARS, behavior, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  unicodeString  character code points that are whitespace
   */
  public WhitespaceRule(final UnicodeString unicodeString)
  {
    this(unicodeString, MatchBehavior.Contains, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  behavior  how to match whitespace
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final MatchBehavior behavior, final boolean reportAll)
  {
    this(CHARS, behavior, reportAll);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  unicodeString  whitespace character code points
   * @param  behavior  how to match whitespace
   */
  public WhitespaceRule(final UnicodeString unicodeString, final MatchBehavior behavior)
  {
    this(unicodeString, behavior, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  unicodeString  whitespace character code points
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final UnicodeString unicodeString, final boolean reportAll)
  {
    this(unicodeString, MatchBehavior.Contains, reportAll);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  unicodeString  whitespace characters
   * @param  behavior  how to match whitespace
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final UnicodeString unicodeString, final MatchBehavior behavior, final boolean reportAll)
  {
    final int[] cp = unicodeString.getCodePoints();
    for (int c : cp) {
      if (!Character.isWhitespace(c)) {
        throw new IllegalArgumentException("Character '" + c + "' is not whitespace");
      }
    }
    whitespaceCharacters = cp;
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the whitespace characters for this rule.
   *
   * @return  whitespace characters
   */
  public UnicodeString getWhitespaceCharacters()
  {
    return new UnicodeString(whitespaceCharacters);
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
    final String text = passwordData.getPassword();
    for (int cp : whitespaceCharacters) {
      if (matchBehavior.match(text, cp)) {
        final String[] codes = {
          ERROR_CODE + "." + matchBehavior.upperSnakeName(),
          ERROR_CODE,
        };
        details.add(new RuleResultDetail(codes, createRuleResultDetailParameters(cp)));
        if (!reportAllFailures) {
          break;
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
   * @param  cp  whitespace character code point
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int cp)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("whitespaceCharacter", UnicodeString.toString(cp));
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
      RuleResultMetadata.CountCategory.Whitespace,
      UnicodeString.countMatchingCharacters(whitespaceCharacters, password.getPassword()));
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "reportAllFailures=" + reportAllFailures + ", " +
      "whitespaceCharacters" + Arrays.toString(whitespaceCharacters) + ", " +
      "matchBehavior=" + matchBehavior;
  }
}

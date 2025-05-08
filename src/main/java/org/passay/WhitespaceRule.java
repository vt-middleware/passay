/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.CharBuffer;
import java.util.Arrays;
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
  protected static final int[] CHARS = {(byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x20};

  /** Whether to report all whitespace matches or just the first. */
  protected boolean reportAllFailures;

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
   * @param  chars  characters that are whitespace
   */
  public WhitespaceRule(final char[] chars)
  {
    this(chars, MatchBehavior.Contains, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  chars  character code points that are whitespace
   */
  public WhitespaceRule(final int[] chars)
  {
    this(chars, MatchBehavior.Contains, true);
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
   * @param  chars  whitespace characters
   * @param  behavior  how to match whitespace
   */
  public WhitespaceRule(final char[] chars, final MatchBehavior behavior)
  {
    this(chars, behavior, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  chars  whitespace character code points
   * @param  behavior  how to match whitespace
   */
  public WhitespaceRule(final int[] chars, final MatchBehavior behavior)
  {
    this(chars, behavior, true);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  chars  whitespace characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final char[] chars, final boolean reportAll)
  {
    this(chars, MatchBehavior.Contains, reportAll);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  cp  whitespace character code points
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final int[] cp, final boolean reportAll)
  {
    this(cp, MatchBehavior.Contains, reportAll);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  chars  whitespace characters
   * @param  behavior  how to match whitespace
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final char[] chars, final MatchBehavior behavior, final boolean reportAll)
  {
    this(CharBuffer.wrap(chars).chars().toArray(), behavior, reportAll);
  }


  /**
   * Creates a new whitespace rule.
   *
   * @param  chars  whitespace characters
   * @param  behavior  how to match whitespace
   * @param  reportAll  whether to report all matches or just the first
   */
  public WhitespaceRule(final int[] chars, final MatchBehavior behavior, final boolean reportAll)
  {
    for (int cp : chars) {
      if (!Character.isWhitespace(cp)) {
        throw new IllegalArgumentException("Character '" + cp + "' is not whitespace");
      }
    }
    whitespaceCharacters = chars;
    matchBehavior = behavior;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the whitespace characters for this rule.
   *
   * @return  whitespace characters
   */
  public int[] getWhitespaceCharacters()
  {
    return whitespaceCharacters;
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
    final String text = passwordData.getPassword();
    for (int cp : whitespaceCharacters) {
      if (matchBehavior.match(text, cp)) {
        final String[] codes = {
          ERROR_CODE + "." + matchBehavior.upperSnakeName(),
          ERROR_CODE,
        };
        result.addError(codes, createRuleResultDetailParameters(cp));
        if (!reportAllFailures) {
          break;
        }
      }
    }
    result.setMetadata(createRuleResultMetadata(passwordData));
    return result;
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
    m.put("whitespaceCharacter", PasswordUtils.toString(cp));
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
      PasswordUtils.countMatchingCharacters(whitespaceCharacters, password.getPassword()));
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::reportAllFailures=%s,matchBehavior=%s,whitespaceCharacters=%s",
        getClass().getName(),
        hashCode(),
        reportAllFailures,
        matchBehavior,
        Arrays.toString(whitespaceCharacters));
  }
}

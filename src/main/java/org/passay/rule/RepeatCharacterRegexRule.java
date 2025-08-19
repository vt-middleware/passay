/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.Locale;
import org.passay.PassayUtils;

/**
 * Rule for determining if a password contains a duplicate ASCII keyboard sequence. See {@link java.util.regex.Pattern}
 * /p{ASCII}. The default sequence length is 5 characters.
 *
 * <ul>
 *   <li>Sequences are of the form: 'bbbbb' or '#####'</li>
 * </ul>
 *
 * @author  Middleware Services
 */
public class RepeatCharacterRegexRule extends IllegalRegexRule
{

  /** Default length of sequence, value is {@value}. */
  public static final int DEFAULT_SEQUENCE_LENGTH = 5;

  /** Minimum length of sequence, value is {@value}. */
  public static final int MINIMUM_SEQUENCE_LENGTH = 3;

  /** Regular expression used by this rule, value is {@value}. */
  private static final String REPEAT_CHAR_REGEX = "([^\\x00-\\x1F])\\1{%d}";

  /** Number of characters in sequence to match. */
  protected final int sequenceLength;


  /** Creates a new repeat character regex rule with the default sequence length. */
  public RepeatCharacterRegexRule()
  {
    this(DEFAULT_SEQUENCE_LENGTH);
  }


  /**
   * Creates a new repeat character regex rule.
   *
   * @param  length  sequence length
   */
  public RepeatCharacterRegexRule(final int length)
  {
    this(length, true);
  }


  /**
   * Creates a new repeat character regex rule.
   *
   * @param  length  sequence length
   * @param  reportAll  whether to report all matches or just the first
   */
  public RepeatCharacterRegexRule(final int length, final boolean reportAll)
  {
    super(
      String.format(
        Locale.ENGLISH,
        REPEAT_CHAR_REGEX,
        PassayUtils.assertNotNullArgOr(
          length,
          l -> l < MINIMUM_SEQUENCE_LENGTH,
          "Sequence length must be >= " + MINIMUM_SEQUENCE_LENGTH) - 1),
      reportAll);
    sequenceLength = length;
  }


  /**
   * Returns the sequence length for this rule.
   *
   * @return  sequence length
   */
  public int getSequenceLength()
  {
    return sequenceLength;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "pattern=" + pattern + ", " +
      "reportAllFailures=" + reportAllFailures + ", " +
      "sequenceLength=" + sequenceLength;
  }
}

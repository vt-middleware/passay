/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Locale;

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
  protected int sequenceLength;


  /** Creates a new repeat character regex rule with the default sequence length. */
  public RepeatCharacterRegexRule()
  {
    this(DEFAULT_SEQUENCE_LENGTH);
  }


  /**
   * Creates a new repeat character regex rule.
   *
   * @param  sl  sequence length
   */
  public RepeatCharacterRegexRule(final int sl)
  {
    this(sl, true);
  }


  /**
   * Creates a new repeat character regex rule.
   *
   * @param  sl  sequence length
   * @param  reportAll  whether to report all matches or just the first
   */
  public RepeatCharacterRegexRule(final int sl, final boolean reportAll)
  {
    super(String.format(Locale.ENGLISH, REPEAT_CHAR_REGEX, sl - 1), reportAll);
    if (sl < MINIMUM_SEQUENCE_LENGTH) {
      throw new IllegalArgumentException(String.format("sequence length must be >= %s", MINIMUM_SEQUENCE_LENGTH));
    }
    sequenceLength = sl;
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
}

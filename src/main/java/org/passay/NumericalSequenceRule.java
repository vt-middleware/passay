/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains a numerical keyboard sequence.
 * The default sequence length is 5 characters.
 *
 * <ul>
 *   <li>Sequences are of the form: '23456'</li>
 *   <li>If wrap=true: '90123' will match</li>
 * </ul>
 *
 * @author  Middleware Services
 */
public class NumericalSequenceRule extends AbstractSequenceRule
{

  /** Digits of a numerical sequence. */
  private static final char[][] DIGITS = new char[][] {
    new char[] {'0', '0'},
    new char[] {'1', '1'},
    new char[] {'2', '2'},
    new char[] {'3', '3'},
    new char[] {'4', '4'},
    new char[] {'5', '5'},
    new char[] {'6', '6'},
    new char[] {'7', '7'},
    new char[] {'8', '8'},
    new char[] {'9', '9'},
  };

  /** Array of all the characters in this sequence rule. */
  private static final char[][][] ALL_CHARS = new char[][][] {DIGITS, };


  /** Creates a new numerical sequence rule with the default sequence length. */
  public NumericalSequenceRule()
  {
    this(DEFAULT_SEQUENCE_LENGTH, false);
  }


  /**
   * Creates a new numerical sequence rule.
   *
   * @param  length  of sequence to search for.
   * @param  wrap  true to wrap sequences when searching for matches, false
   *               otherwise.
   */
  public NumericalSequenceRule(final int length, final boolean wrap)
  {
    setSequenceLength(length);
    wrapSequence = wrap;
  }


  /**
   * Creates a new numerical sequence rule.
   *
   * @param  length  of sequence to search for.
   * @param  wrap  true to wrap sequences when searching for matches, false
   *               otherwise.
   * @param  b  whether to report all sequence matches or just the first
   */
  public NumericalSequenceRule(
    final int length,
    final boolean wrap,
    final boolean b)
  {
    setSequenceLength(length);
    wrapSequence = wrap;
    reportAllFailures = b;
  }


  @Override
  protected char[][] getSequence(final int n)
  {
    return ALL_CHARS[n];
  }


  @Override
  protected int getSequenceCount()
  {
    return ALL_CHARS.length;
  }
}

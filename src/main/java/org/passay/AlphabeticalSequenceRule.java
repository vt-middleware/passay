/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains an alphabetical keyboard
 * sequence. Both uppercase and lowercase sequences are checked. The default
 * sequence length is 5 characters.
 *
 * <ul>
 *   <li>Sequences are of the form: 'stuvw' or 'KLMNO'</li>
 *   <li>If wrap=true: 'yzabc' will match</li>
 * </ul>
 *
 * @author  Middleware Services
 */
public class AlphabeticalSequenceRule extends AbstractSequenceRule
{

  /** Letters of the alphabet. */
  private static final char[][] LETTERS = new char[][] {
    new char[] {'a', 'A'},
    new char[] {'b', 'B'},
    new char[] {'c', 'C'},
    new char[] {'d', 'D'},
    new char[] {'e', 'E'},
    new char[] {'f', 'F'},
    new char[] {'g', 'G'},
    new char[] {'h', 'H'},
    new char[] {'i', 'I'},
    new char[] {'j', 'J'},
    new char[] {'k', 'K'},
    new char[] {'l', 'L'},
    new char[] {'m', 'M'},
    new char[] {'n', 'N'},
    new char[] {'o', 'O'},
    new char[] {'p', 'P'},
    new char[] {'q', 'Q'},
    new char[] {'r', 'R'},
    new char[] {'s', 'S'},
    new char[] {'t', 'T'},
    new char[] {'u', 'U'},
    new char[] {'v', 'V'},
    new char[] {'w', 'W'},
    new char[] {'x', 'X'},
    new char[] {'y', 'Y'},
    new char[] {'z', 'Z'},
  };

  /** Array of all the characters in this sequence rule. */
  private static final char[][][] ALL_CHARS = new char[][][] {LETTERS, };


  /** Default constructor. */
  public AlphabeticalSequenceRule()
  {
    this(DEFAULT_SEQUENCE_LENGTH, false);
  }


  /**
   * Creates a new alphabetical sequence rule.
   *
   * @param  sl  sequence length
   * @param  wrap  whether to wrap search sequences
   */
  public AlphabeticalSequenceRule(final int sl, final boolean wrap)
  {
    setSequenceLength(sl);
    wrapSequence = wrap;
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

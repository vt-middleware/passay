/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Contains common character sequences.
 *
 * @author  Middleware Services
 */
public final class CharacterSequences
{

  /** English QWERTY sequence. */
  public static final char[][][] EN_QWERTY = new char[][][] {
    new char[][] {
      new char[] {'`', '~'},
      new char[] {'1', '!'},
      new char[] {'2', '@'},
      new char[] {'3', '#'},
      new char[] {'4', '$'},
      new char[] {'5', '%'},
      new char[] {'6', '^'},
      new char[] {'7', '&'},
      new char[] {'8', '*'},
      new char[] {'9', '('},
      new char[] {'0', ')'},
      new char[] {'-', '_'},
      new char[] {'=', '+'},
    },
    new char[][] {
      new char[] {'q', 'Q'},
      new char[] {'w', 'W'},
      new char[] {'e', 'E'},
      new char[] {'r', 'R'},
      new char[] {'t', 'T'},
      new char[] {'y', 'Y'},
      new char[] {'u', 'U'},
      new char[] {'i', 'I'},
      new char[] {'o', 'O'},
      new char[] {'p', 'P'},
      new char[] {'[', '{'},
      new char[] {']', '}'},
      new char[] {'\\', '|'},
    },
    new char[][] {
      new char[] {'a', 'A'},
      new char[] {'s', 'S'},
      new char[] {'d', 'D'},
      new char[] {'f', 'F'},
      new char[] {'g', 'G'},
      new char[] {'h', 'H'},
      new char[] {'j', 'J'},
      new char[] {'k', 'K'},
      new char[] {'l', 'L'},
      new char[] {';', ':'},
      new char[] {'\'', '"'},
    },
    new char[][] {
      new char[] {'z', 'Z'},
      new char[] {'x', 'X'},
      new char[] {'c', 'C'},
      new char[] {'v', 'V'},
      new char[] {'b', 'B'},
      new char[] {'n', 'N'},
      new char[] {'m', 'M'},
      new char[] {',', '<'},
      new char[] {'.', '>'},
      new char[] {'/', '?'},
    },
  };

  /** English alphabetical sequence. */
  public static final char[][][] EN_ALPHABETICAL = new char[][][] {
    new char[][] {
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
    },
  };

  /** English numerical squence. */
  public static final char[][][] EN_NUMERICAL = new char[][][] {
    new char[][] {
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
    },
  };


  /** Default constructor. */
  private CharacterSequences() {}
}

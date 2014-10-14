/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Contains common characters.
 *
 * @author  Middleware Services
 */
public final class Characters
{


  /** English lowercase characters. */
  public static final char[] EN_LOWERCASE = new char[] {
    'a',
    'b',
    'c',
    'd',
    'e',
    'f',
    'g',
    'h',
    'i',
    'j',
    'k',
    'l',
    'm',
    'n',
    'o',
    'p',
    'q',
    'r',
    's',
    't',
    'u',
    'v',
    'w',
    'x',
    'y',
    'z',
  };

  /** English uppercase characters. */
  public static final char[] EN_UPPERCASE = new char[] {
    'A',
    'B',
    'C',
    'D',
    'E',
    'F',
    'G',
    'H',
    'I',
    'J',
    'K',
    'L',
    'M',
    'N',
    'O',
    'P',
    'Q',
    'R',
    'S',
    'T',
    'U',
    'V',
    'W',
    'X',
    'Y',
    'Z',
  };

  /** English lowercase and uppercase characters. */
  public static final char[] EN_ALPHABETICAL = PasswordUtils.concatArrays(
    EN_LOWERCASE, EN_UPPERCASE);

  /** English digit characters. */
  public static final char[] EN_DIGIT = new char[] {
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
  };

  /** English special characters. */
  public static final char[] EN_SPECIAL = new char[] {
    '!',
    '\"',
    '#',
    '$',
    '%',
    '&',
    '\'',
    '(',
    ')',
    '*',
    '+',
    ',',
    '-',
    '.',
    '/',
    ':',
    ';',
    '<',
    '=',
    '>',
    '?',
    '@',
    '[',
    '\\',
    ']',
    '^',
    '_',
    '`',
    '{',
    '|',
    '}',
    '~',
  };


  /** Default constructor. */
  private Characters() {}
}

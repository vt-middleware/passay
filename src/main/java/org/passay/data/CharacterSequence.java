/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data;

import org.passay.PassayUtils;

/**
 * Models a sequence of characters in one or more forms as strings of equal length where each string represents one form
 * of characters in the sequence. For example, <code>new CharacterSequence("ABCDEF", "abcdef")</code> would represent a
 * sequence of the first six case-insensitive alphabetical characters. A matching function, {@link #matches(int, int)},
 * determines whether a candidate character matches any of the defined forms.
 *
 * @author  Middleware Services
 */
public class CharacterSequence
{

  /** Character forms. */
  private final String[] forms;


  /**
   * Creates a new instance from one or more sequences. The following example demonstrates a sequence that models the
   * first row of a QWERTY keyboard:
   *
   * <pre>
       new CharacterSequence("`1234567890-=", "~!@#$%^&amp;*()_+");
   * </pre>
   *
   * @param  strings  One or more characters strings, one for each form. At least one sequence MUST be defined. If
   *                  multiple sequences are defined, they MUST be of equal length.
   */
  public CharacterSequence(final String... strings)
  {
    if (strings.length < 1) {
      throw new IllegalArgumentException("At least one sequence must be defined");
    }
    for (String s : strings) {
      if (strings[0].length() != s.length()) {
        throw new IllegalArgumentException("Strings have unequal length: " + strings[0] + " != " + s);
      }
    }
    forms = strings;
  }


  /** @return  Array of strings that define character forms. */
  public String[] getForms()
  {
    return forms;
  }


  /**
   * Determines whether the character at the given index of the sequence matches the given value. Both original and
   * variant forms are considered.
   *
   * @param  index  Character sequence index.
   * @param  cp  Character code point to check for.
   *
   * @return  True if sequence contains given character, false otherwise.
   */
  public boolean matches(final int index, final int cp)
  {
    for (String s : forms) {
      if (s.codePointAt(index) == cp) {
        return true;
      }
    }
    return false;
  }


  /** @return  Length of character sequence. */
  public int length()
  {
    return PassayUtils.codePointCount(forms[0]);
  }
}

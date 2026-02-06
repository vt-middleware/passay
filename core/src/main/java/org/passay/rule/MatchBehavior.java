/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.function.BiFunction;
import org.passay.PassayUtils;
import org.passay.UnicodeString;

/**
 * Enum that defines how string matching should occur.
 *
 * @author  Middleware Services
 */
public enum MatchBehavior
{

  /** Use {@link UnicodeString#startsWith(UnicodeString)}. */
  StartsWith("starts with", UnicodeString::startsWith),

  /** Use {@link UnicodeString#endsWith(UnicodeString)}. */
  EndsWith("ends with", UnicodeString::endsWith),

  /** Use {@link UnicodeString#contains(UnicodeString)}. */
  Contains("contains", UnicodeString::contains);

  /** The description of the match behavior. **/
  private final String description;

  /** The matcher function. **/
  private final BiFunction<UnicodeString, UnicodeString, Boolean> matcher;

  /**
   * Constructs a MatchBehavior constant.
   *
   * @param desc the behavior description
   * @param matcherFunction the matcher function
   */
  MatchBehavior(final String desc, final BiFunction<UnicodeString, UnicodeString, Boolean> matcherFunction)
  {
    description = desc;
    matcher = matcherFunction;
  }


  /**
   * Returns the name of this match behavior in upper case snake casing.
   *
   * @return  upper case snake name
   */
  public String upperSnakeName()
  {
    final String name = name();
    final StringBuilder sb = new StringBuilder();
    sb.append(PassayUtils.toString(name.codePointAt(0)));
    int i = Character.charCount(name.codePointAt(0));
    while (i < name.length()) {
      final int cp = name.codePointAt(i);
      if (Character.isUpperCase(cp)) {
        sb.append("_").append(PassayUtils.toString(cp));
      } else {
        sb.append(PassayUtils.toString(Character.toUpperCase(cp)));
      }
      i += Character.charCount(cp);
    }
    return sb.toString();
  }


  /**
   * Returns whether text matches the supplied string for this match type.
   *
   * @param  text  to search
   * @param  cp  code point to find in text
   *
   * @return  whether text matches the supplied string for this match type
   */
  public boolean match(final UnicodeString text, final int cp)
  {
    return match(text, PassayUtils.toString(cp));
  }


  /**
   * Returns whether text matches the supplied string for this match type.
   *
   * @param  text  to search
   * @param  s  to find in text
   *
   * @return  whether text matches the supplied string for this match type
   */
  public boolean match(final UnicodeString text, final CharSequence s)
  {
    return match(text, new UnicodeString(s));
  }


  /**
   * Returns whether text matches the supplied string for this match type.
   *
   * @param  text  to search
   * @param  s  to find in text
   *
   * @return  whether text matches the supplied string for this match type
   */
  public boolean match(final UnicodeString text, final UnicodeString s)
  {
    return matcher.apply(text, s);
  }


  @Override
  public String toString()
  {
    return description;
  }
}

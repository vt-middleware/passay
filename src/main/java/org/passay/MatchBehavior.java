/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.function.BiFunction;

/**
 * Enum that defines how string matching should occur.
 *
 * @author  Middleware Services
 */
public enum MatchBehavior
{

  /** Use {@link String#startsWith(String)}. */
  StartsWith("starts with", String::startsWith),

  /** Use {@link String#endsWith(String)}. */
  EndsWith("ends with", String::endsWith),

  /** Use {@link String#contains(CharSequence)}. */
  Contains("contains", String::contains);

  /** The description of the match behavior. **/
  private final String description;

  /** The matcher function. **/
  private final BiFunction<String, String, Boolean> matcher;

  /**
   * Constructs a MatchBehavior constant.
   *
   * @param desc the behavior description
   * @param matcherFunction the matcher function
   */
  MatchBehavior(final String desc, final BiFunction<String, String, Boolean> matcherFunction)
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
    sb.append(CodePoints.toString(name.codePointAt(0)));
    int i = Character.charCount(name.codePointAt(0));
    while (i < name.length()) {
      final int cp = name.codePointAt(i);
      if (Character.isUpperCase(cp)) {
        sb.append("_").append(CodePoints.toString(cp));
      } else {
        sb.append(CodePoints.toString(Character.toUpperCase(cp)));
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
  public boolean match(final String text, final int cp)
  {
    return match(text, CodePoints.toString(cp));
  }


  /**
   * Returns whether text matches the supplied string for this match type.
   *
   * @param  text  to search
   * @param  s  to find in text
   *
   * @return  whether text matches the supplied string for this match type
   */
  public boolean match(final String text, final String s)
  {
    return matcher.apply(text, s);
  }


  @Override
  public String toString()
  {
    return description;
  }
}

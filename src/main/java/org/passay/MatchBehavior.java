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
    final StringBuilder sb = new StringBuilder("MATCH_BEHAVIOR_");
    sb.append(name.charAt(0));
    for (int i = 1; i < name.length(); i++) {
      final char ch = name.charAt(i);
      if (Character.isUpperCase(ch)) {
        sb.append("_").append(ch);
      } else {
        sb.append(Character.toUpperCase(ch));
      }
    }
    return sb.toString();
  }


  /**
   * Returns whether text matches the supplied string for this match type.
   *
   * @param  text  to search
   * @param  c  to find in text
   *
   * @return  whether text matches the supplied string for this match type
   */
  public boolean match(final String text, final char c)
  {
    return match(text, String.valueOf(c));
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

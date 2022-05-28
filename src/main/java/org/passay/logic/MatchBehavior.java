/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.logic;

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

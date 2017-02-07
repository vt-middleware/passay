/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Enum that defines how string matching should occur.
 *
 * @author  Middleware Services
 */
public enum MatchBehavior
{

  /** Use {@link String#startsWith(String)}. */
  StartsWith,

  /** Use {@link String#endsWith(String)}. */
  EndsWith,

  /** Use {@link String#contains(CharSequence)}. */
  Contains;


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
    final boolean match;
    switch(this) {

    case StartsWith:
      match = text.startsWith(s);
      break;

    case EndsWith:
      match = text.endsWith(s);
      break;

    case Contains:
      match = text.contains(s);
      break;

    default: throw new IllegalStateException("Unknown match type: " + this);
    }
    return match;
  }


  @Override
  public String toString()
  {
    final String s;
    switch(this) {

    case StartsWith:
      s = "starts with";
      break;

    case EndsWith:
      s = "ends with";
      break;

    case Contains:
      s = "contains";
      break;

    default: throw new IllegalStateException("Unknown match type: " + this);
    }
    return s;
  }
}

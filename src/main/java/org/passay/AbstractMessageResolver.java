/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Locale;

/**
 * Provides a baseline implementation of {@link #resolve(RuleResultDetail)} which uses {@link String#format(String,
 * Object...)} to resolve messages. When no message is found for a particular key, the key and the {@link
 * RuleResultDetail#getParameters()} are used to construct a message.
 *
 * @author  Middleware Services
 */
public abstract class AbstractMessageResolver implements MessageResolver
{

  /** Locale used to format error messages. */
  private Locale locale;


  /**
   * Returns the locale used by this message resolver.
   *
   * @return  locale
   */
  public Locale getLocale()
  {
    return locale;
  }


  /**
   * Sets the locale used by this message resolver.
   *
   * @param  l  locale
   */
  public void setLocale(final Locale l)
  {
    locale = l;
  }


  /**
   * Returns the message for the supplied key.
   *
   * @param  key  which corresponds to a message
   *
   * @return  message
   */
  protected abstract String getMessage(String key);


  @Override
  public String resolve(final RuleResultDetail detail)
  {
    String message;
    for (String key : detail.getErrorCodes()) {
      message = getMessage(key);
      if (message != null) {
        final String format;
        if (locale != null) {
          format = String.format(locale, message, resolveMatchBehavior(detail.getValues()));
        } else {
          format = String.format(message, resolveMatchBehavior(detail.getValues()));
        }
        return format;
      }
    }
    final String format;
    if (!detail.getParameters().isEmpty()) {
      if (locale != null) {
        format =  String.format(locale, "%s:%s", detail.getErrorCode(), detail.getParameters()).toString();
      } else {
        format =  String.format("%s:%s", detail.getErrorCode(), detail.getParameters()).toString();
      }
    } else {
      format = detail.getErrorCode();
    }
    return format;
  }


  /**
   * Resolves any custom messaging for {@link MatchBehavior}. The supplied array may be modified by this method.
   *
   * @param  args  containing any MatchBehavior enums
   *
   * @return  resolved args if custom match behavior messaging was found
   */
  protected Object[] resolveMatchBehavior(final Object[] args)
  {
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        final Object arg = args[i];
        if (arg instanceof MatchBehavior) {
          final String msg = getMessage(((MatchBehavior) arg).upperSnakeName());
          if (msg != null) {
            args[i] = msg;
          }
        }
      }
    }
    return args;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.resolver;

import java.util.Locale;
import org.passay.PassayUtils;
import org.passay.RuleResultDetail;

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
  private final Locale locale;


  /**
   * Creates a new abstract message resolver.
   *
   * @param  locale  locale
   */
  public AbstractMessageResolver(final Locale locale)
  {
    this.locale = locale;
  }


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
    PassayUtils.assertNotNullArg(detail, "Rule result detail cannot be null");
    String message;
    for (String key : detail.getErrorCodes()) {
      message = getMessage(key);
      if (message != null) {
        final String format;
        if (locale != null) {
          format = String.format(locale, message, detail.getValues());
        } else {
          format = String.format(message, detail.getValues());
        }
        return format;
      }
    }
    final String format;
    if (!detail.getParameters().isEmpty()) {
      if (locale != null) {
        format = String.format(locale, "%s:%s", detail.getErrorCode(), detail.getParameters());
      } else {
        format = String.format("%s:%s", detail.getErrorCode(), detail.getParameters());
      }
    } else {
      format = detail.getErrorCode();
    }
    return format;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Provides a baseline implementation of {@link #resolve(RuleResultDetail)} which uses {@link String#format(String,
 * Object...)} to resolve messages. When no message is found for a particular key, the key and the {@link
 * RuleResultDetail#getParameters()} are used to construct a message.
 *
 * @author  Middleware Services
 */
public abstract class AbstractMessageResolver implements MessageResolver
{


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
    final String key = detail.getErrorCode();
    final String message = getMessage(key);
    final String format;
    if (message != null) {
      format = String.format(message, detail.getValues());
    } else {
      if (!detail.getParameters().isEmpty()) {
        format = String.format("%s:%s", key, detail.getParameters());
      } else {
        format = String.format("%s", key);
      }
    }
    return format;
  }
}

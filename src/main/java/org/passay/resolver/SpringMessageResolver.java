/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.resolver;

import java.util.Locale;

import org.passay.rule.result.RuleResultDetail;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * Provides implementation for resolving validation message using using Spring's {@link MessageSource}.
 *
 * @author Kazuki Shimizu
 * @version 1.3.1
 */
public class SpringMessageResolver implements MessageResolver
{

  /** A accessor for Spring's {@link MessageSource} */
  private final MessageSourceAccessor messageSourceAccessor;

  /** The {@link MessageResolver} for fallback */
  private final MessageResolver fallbackMessageResolver = new PropertiesMessageResolver();

  /**
   * Create a new instance with the locale associated with the current thread.
   * @param messageSource a message source managed by spring
   */
  public SpringMessageResolver(final MessageSource messageSource)
  {
    messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }

  /**
   * Create a new instance with the specified locale.
   * @param messageSource a message source managed by spring
   * @param locale the locale to use for message access
   */
  public SpringMessageResolver(final MessageSource messageSource, final Locale locale)
  {
    messageSourceAccessor = new MessageSourceAccessor(messageSource, locale);
  }

  /**
   * Resolves the message for the supplied rule result detail using Spring's {@link MessageSource}.
   * (If the message can't retrieve from a {@link MessageSource}, return default message provided by passay)
   * @param detail rule result detail
   * @return message for the detail error code
   */
  @Override
  public String resolve(final RuleResultDetail detail)
  {
    try {
      return messageSourceAccessor.getMessage(detail.getErrorCode(), detail.getValues());
    } catch (NoSuchMessageException e) {
      return fallbackMessageResolver.resolve(detail);
    }
  }

}

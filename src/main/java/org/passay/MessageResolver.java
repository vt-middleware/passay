/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolves messages from rule result details in order to provide a facility for
 * customizing messages such as password rule validation failures.
 *
 * @author  Middleware Services
 */
public class MessageResolver
{

  /** Classpath location of default message map. */
  public static final String DEFAULT_MESSAGE_PATH = "/messages.properties";

  /** Maps message keys to message strings. */
  private final Properties messageProperties;


  /** Creates a new message resolver with the default message map. */
  public MessageResolver()
  {
    this(getDefaultProperties());
  }


  /**
   * Creates a new message resolver with the supplied message map.
   *
   * @param  properties  map of keys to messages.
   */
  public MessageResolver(final Properties properties)
  {
    if (properties == null) {
      throw new IllegalArgumentException("Properties cannot be null.");
    }
    messageProperties = properties;
  }


  /**
   * Resolves the message for the supplied rule result detail.
   *
   * @param  detail  rule result detail
   *
   * @return  message for the detail error code in properties resource or detail
   * error code if no message is found.
   */
  public String resolve(final RuleResultDetail detail)
  {
    final String key = detail.getErrorCode();
    final String message = messageProperties.getProperty(key);
    String format;
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


  /**
   * Returns the default mapping of message keys to message strings.
   *
   * @return  default message mapping.
   */
  public static Properties getDefaultProperties()
  {
    final Properties props = new Properties();
    InputStream in = null;
    try {
      in = MessageResolver.class.getResourceAsStream(DEFAULT_MESSAGE_PATH);
      props.load(in);
    } catch (Exception e) {
      throw new IllegalStateException(
        "Error loading default message properties.",
        e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return props;
  }
}

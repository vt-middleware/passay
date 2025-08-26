/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.passay.PassayUtils;

/**
 * Resolves messages from rule result details from a properties file. The default properties file is loaded from the
 * classpath, see {@link #DEFAULT_MESSAGE_PATH}.
 *
 * @author  Middleware Services
 */
public class PropertiesMessageResolver extends AbstractMessageResolver
{

  /** Classpath location of default message map. */
  public static final String DEFAULT_MESSAGE_PATH = "/passay.properties";

  /** Maps message keys to message strings. */
  private final Properties messageProperties;


  /** Creates a new message resolver with the default message properties. See {@link #getDefaultProperties()}. */
  public PropertiesMessageResolver()
  {
    this(getDefaultProperties());
  }


  /**
   * Creates a new message resolver with the supplied message properties.
   *
   * @param  properties  map of keys to messages.
   */
  public PropertiesMessageResolver(final Properties properties)
  {
    this(properties, null);
  }


  /**
   * Creates a new message resolver with the supplied message properties.
   *
   * @param  properties  map of keys to messages.
   * @param  locale  for resource
   */
  public PropertiesMessageResolver(final Properties properties, final Locale locale)
  {
    super(locale);
    messageProperties = PassayUtils.assertNotNullArg(properties, "Properties cannot be null");
  }


  @Override
  protected String getMessage(final String key)
  {
    return messageProperties.getProperty(key);
  }


  /**
   * Returns the default mapping of message keys to message strings.
   *
   * @return  default message mapping.
   */
  public static Properties getDefaultProperties()
  {
    final Properties props = new Properties();
    try (InputStream in = PropertiesMessageResolver.class.getResourceAsStream(DEFAULT_MESSAGE_PATH)) {
      try {
        props.load(in);
      } catch (Exception e) {
        throw new IllegalStateException("Error loading default message properties.", e);
      }
    } catch (IOException ignored) {}
    return props;
  }
}

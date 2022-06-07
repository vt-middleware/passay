/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resolves messages from rule result details from a resource bundle. Uses {@link ResourceBundle#getBundle(String)} to
 * load the default bundle.
 *
 * @author  Middleware Services
 */
public class ResourceBundleMessageResolver extends AbstractMessageResolver
{

  /** Maps locale specific message keys to message strings. */
  private final ResourceBundle resourceBundle;


  /** Creates a new message resolver with the default message map. */
  public ResourceBundleMessageResolver()
  {
    this(getDefaultBundle());
  }


  /**
   * Creates a new message resolver with the supplied resource bundle.
   *
   * @param  bundle  locale specific map of keys to messages.
   */
  public ResourceBundleMessageResolver(final ResourceBundle bundle)
  {
    if (bundle == null) {
      throw new IllegalArgumentException("Bundle cannot be null.");
    }
    resourceBundle = bundle;
  }


  @Override
  protected String getMessage(final String key)
  {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return null;
    }
  }


  /**
   * Returns the default resource bundle which is found in passay.properties.
   *
   * @return  default resource bundle.
   */
  public static ResourceBundle getDefaultBundle()
  {
    return ResourceBundle.getBundle("passay");
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Locale;
import java.util.ResourceBundle;

import org.passay.resolver.ResourceBundleMessageResolver;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ResourceBundleMessageResolver}.
 *
 * @author  Middleware Services
 */
public class ResourceBundleMessageResolverTest
{


  /**
   * Test bundle loading with {@link ResourceBundle#getBundle(String, Locale)}.
   */
  @Test(groups = "passtest")
  public void loadBundle()
  {
    AssertJUnit.assertNotNull(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.GERMAN)).getMessage("LOCALE_PROPERTY"));
    AssertJUnit.assertNull(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.getDefault())).getMessage("LOCALE_PROPERTY"));
    AssertJUnit.assertNotNull(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.getDefault())).getMessage("TOO_SHORT"));
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.resolver;

import java.util.Locale;
import java.util.ResourceBundle;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @Test
  public void loadBundle()
  {
    assertThat(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.GERMAN)).getMessage("LOCALE_PROPERTY")).isNotNull();
    assertThat(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.getDefault())).getMessage("LOCALE_PROPERTY")).isNull();
    assertThat(
      new ResourceBundleMessageResolver(
        ResourceBundle.getBundle("passay", Locale.getDefault())).getMessage("TOO_SHORT")).isNotNull();
  }
}

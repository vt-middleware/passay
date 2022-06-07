/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.spring;

import java.util.Locale;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Test class for {@link SpringMessageResolver}.
 *
 * @author Kazuki Shimizu
 */
public class SpringMessageResolverTest
{

  /**
   * Perform test for using locale associated current thread.
   */
  @Test(groups = "passtest")
  public void testWithLocaleAssociatedCurrentThread()
  {
    final StaticMessageSource messageSource = new StaticMessageSource();
    messageSource.addMessage("TOO_SHORT", Locale.US, "to short {0}-{1} for us");
    messageSource.addMessage("TOO_SHORT", Locale.JAPAN, "to short {0}-{1} for jp");
    messageSource.addMessage("TOO_SHORT", Locale.FRANCE, "to short {0}-{1} for fr");

    try {
      LocaleContextHolder.setLocale(Locale.FRANCE);
      final PasswordValidator validator =
        new PasswordValidator(new SpringMessageResolver(messageSource), new LengthRule(8));
      final PasswordData pd = new PasswordData("pass");

      final RuleResult result = validator.validate(pd);

      AssertJUnit.assertFalse(result.isValid());
      AssertJUnit.assertEquals("to short 8-8 for fr", validator.getMessages(result).get(0));
    } finally {
      LocaleContextHolder.resetLocaleContext();
    }
  }

  /**
   * Perform test for using specified locale.
   */
  @Test(groups = "passtest")
  public void testWithSpecifiedLocale()
  {
    final StaticMessageSource messageSource = new StaticMessageSource();
    messageSource.addMessage("TOO_SHORT", Locale.US, "to short {0}-{1} for us");
    messageSource.addMessage("TOO_SHORT", Locale.JAPAN, "to short {0}-{1} for jp");

    final PasswordValidator validator =
      new PasswordValidator(new SpringMessageResolver(messageSource, Locale.JAPAN), new LengthRule(8));
    final PasswordData pd = new PasswordData("pass");

    final RuleResult result = validator.validate(pd);

    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals("to short 8-8 for jp", validator.getMessages(result).get(0));
  }

  /**
   * Perform test for fallback message provided by passay.
   */
  @Test(groups = "passtest")
  public void testFallback()
  {
    final StaticMessageSource messageSource = new StaticMessageSource();

    final PasswordValidator validator =
      new PasswordValidator(new SpringMessageResolver(messageSource), new LengthRule(8));
    final PasswordData pd = new PasswordData("pass");

    final RuleResult result = validator.validate(pd);

    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals("Password must be 8 or more characters in length.",
      validator.getMessages(result).get(0));
  }

}

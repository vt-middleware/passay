/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.spring;

import java.util.Locale;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.rule.LengthRule;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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

      assertThat(result.isValid()).isFalse();
      assertThat(validator.getMessages(result).get(0)).isEqualTo("to short 8-8 for fr");
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

    assertThat(result.isValid()).isFalse();
    assertThat(validator.getMessages(result).get(0)).isEqualTo("to short 8-8 for jp");
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

    assertThat(result.isValid()).isFalse();
    assertThat(validator.getMessages(result).get(0)).isEqualTo("Password must be 8 or more characters in length.");
  }
}

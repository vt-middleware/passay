/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test the HaveIveBeenPwnd Rule.
 *
 * @author Wolfgang Jung (post@wolfgang-jung.net)
 */
public class HaveIveBeenPwndRuleTest
{
  /**
   * a common password, that is listed.
   */
  private static final String COMMON_PASSWORD = "trustno1";

  @Test
  public void testWithCommonPwd() throws MalformedURLException
  {
    final HaveIveBeenPwndRule pwndRule = new HaveIveBeenPwndRule("org.passay");
    pwndRule.setRemoteAddress(new URL("https://api.pwnedpasswords.com/range/"));
    pwndRule.setMandatory(true);
    pwndRule.setTimeout(Duration.ofSeconds(10));
    pwndRule.setAllowPasswordDuringTimeout(false);
    final PasswordValidator validator = new PasswordValidator(pwndRule);
    final RuleResult result = validator.validate(new PasswordData(COMMON_PASSWORD));
    Assert.assertFalse(result.valid);
    Assert.assertTrue(result.metadata.getCount(RuleResultMetadata.CountCategory.Pwnd) > 1);
    Assert.assertEquals(validator.getMessages(result).size(), 1);
    Assert.assertTrue(validator.getMessages(result).get(0).contains("previous leaks"));
  }

  @Test
  public void testWithCommonPwdAndTooShortTimeout()
  {
    final HaveIveBeenPwndRule pwndRule = new HaveIveBeenPwndRule("org.passay");
    pwndRule.setTimeout(Duration.ofMillis(1));
    final RuleResult result = pwndRule.validate(new PasswordData(COMMON_PASSWORD));
    Assert.assertTrue(result.valid);
    Assert.assertEquals(result.metadata.getCount(RuleResultMetadata.CountCategory.Pwnd), 0);
  }

  @Test
  public void testWithCommonPwdAndTooShortTimeoutNotAllowing()
  {
    final HaveIveBeenPwndRule pwndRule = new HaveIveBeenPwndRule("org.passay");
    pwndRule.setTimeout(Duration.ofMillis(1));
    pwndRule.setAllowPasswordDuringTimeout(false);
    final RuleResult result = pwndRule.validate(new PasswordData(COMMON_PASSWORD));
    Assert.assertFalse(result.valid);
  }

  @Test
  public void testWithLongRandomPassword()
  {
    final HaveIveBeenPwndRule pwndRule = new HaveIveBeenPwndRule("org.passay");
    final String s = new PasswordGenerator().generatePassword(64,
      new CharacterRule(EnglishCharacterData.Special),
      new CharacterRule(EnglishCharacterData.Alphabetical),
      new CharacterRule(EnglishCharacterData.Digit)
    );
    final RuleResult result = pwndRule.validate(new PasswordData(s));
    Assert.assertTrue(result.valid);
  }
}

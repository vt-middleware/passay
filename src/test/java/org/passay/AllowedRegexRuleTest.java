/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.regex.Pattern;

import org.passay.logic.PasswordData;
import org.passay.rule.AllowedRegexRule;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link AllowedRegexRule}.
 *
 * @author  Middleware Services
 */
public class AllowedRegexRuleTest extends AbstractRuleTest
{

  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // test invalid password
        {
          new AllowedRegexRule("\\d\\d\\d\\d"),
          new PasswordData("p4zRcv8#n65"),
          codes(AllowedRegexRule.ERROR_CODE),
        },
        // test entire password
        {
          new AllowedRegexRule("^[\\p{Alpha}]+\\d\\d\\d\\d$"),
          new PasswordData("pwUiNh0248"),
          null,
        },
        // test find password
        {
          new AllowedRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUi0248xwK"),
          null,
        },
        // test case insensitive
        {
          new AllowedRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("pwUAbbCd0248xwK"),
          codes(AllowedRegexRule.ERROR_CODE),
        },
        // test case insensitive
        {
          new AllowedRegexRule("(?i)abcd"),
          new PasswordData("pwUAbbCd0248xwK"),
          codes(AllowedRegexRule.ERROR_CODE),
        },
        // test case insensitive
        {
          new AllowedRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("pwUAbCd0248xwK"),
          null,
        },
        // test case insensitive
        {
          new AllowedRegexRule("(?i)abcd"),
          new PasswordData("pwUAbCd0248xwK"),
          null,
        },
      };
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          new AllowedRegexRule("\\d\\d\\d\\d"),
          new PasswordData("p4zRcv8#n65"),
          new String[] {String.format("Password must match pattern '%s'.", "\\d\\d\\d\\d"), },
        },
        {
          new AllowedRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("p4zRabCCdv8#n65"),
          new String[] {String.format("Password must match pattern '%s'.", "abcd"), },
        },
        {
          new AllowedRegexRule("(?i)abcd"),
          new PasswordData("p4zRabCCdv8#n65"),
          new String[] {String.format("Password must match pattern '%s'.", "(?i)abcd"), },
        },
      };
  }
}

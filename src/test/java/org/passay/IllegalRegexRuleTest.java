/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.regex.Pattern;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link IllegalRegexRule}.
 *
 * @author  Middleware Services
 */
public class IllegalRegexRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // test valid password
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // test entire password
        {
          new IllegalRegexRule("^[\\p{Alpha}]+\\d\\d\\d\\d$"),
          new PasswordData("pwUiNh0248"),
          codes(IllegalRegexRule.ERROR_CODE),
        },
        // test find password
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUi0248xwK"),
          codes(IllegalRegexRule.ERROR_CODE),
        },
        // test multiple matches
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUi0248xwK9753"),
          codes(IllegalRegexRule.ERROR_CODE, IllegalRegexRule.ERROR_CODE),
        },
        // test single match
        {
          new IllegalRegexRule("\\d\\d\\d\\d", false),
          new PasswordData("pwUi0248xwK9753"),
          codes(IllegalRegexRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUi0248xwK9753uu0248"),
          codes(IllegalRegexRule.ERROR_CODE, IllegalRegexRule.ERROR_CODE),
        },
        // test case insensitive
        {
          new IllegalRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("p4zRaBcDv8#n65"),
          codes(IllegalRegexRule.ERROR_CODE),
        },
        // test case insensitive
        {
          new IllegalRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("p4zRaBBcDv8#n65"),
          null,
        },
        // test case insensitive
        {
          new IllegalRegexRule("(?i)abcd"),
          new PasswordData("p4zRaBcDv8#n65"),
          codes(IllegalRegexRule.ERROR_CODE),
        },
        // test case insensitive
        {
          new IllegalRegexRule("(?i)abcd"),
          new PasswordData("p4zRaBBcDv8#n65"),
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
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUiNh0248"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "0248"), },
        },
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUiNh0248xwK9753"),
          new String[] {
            String.format("Password matches the illegal pattern '%s'.", "0248"),
            String.format("Password matches the illegal pattern '%s'.", "9753"), },
        },
        {
          new IllegalRegexRule("\\d\\d\\d\\d", false),
          new PasswordData("pwUiNh0248xwK9753"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "0248"), },
        },
        {
          new IllegalRegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUiNh0248xwK9753uu0248"),
          new String[] {
            String.format("Password matches the illegal pattern '%s'.", "0248"),
            String.format("Password matches the illegal pattern '%s'.", "9753"), },
        },
        {
          new IllegalRegexRule("abcd", Pattern.CASE_INSENSITIVE),
          new PasswordData("pwABCD0248"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "ABCD"), },
        },
        {
          new IllegalRegexRule("(?i)abcd"),
          new PasswordData("pwABCD0248"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "ABCD"), },
        },
      };
  }
}

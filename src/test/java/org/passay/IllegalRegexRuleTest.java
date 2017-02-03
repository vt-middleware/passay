/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

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
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
    throws Exception
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
      };
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
    throws Exception
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
      };
  }
}

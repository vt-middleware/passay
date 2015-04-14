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
      };
  }
}

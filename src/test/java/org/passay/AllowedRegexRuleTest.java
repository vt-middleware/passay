/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

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
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
    throws Exception
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
          new AllowedRegexRule("\\d\\d\\d\\d"),
          new PasswordData("p4zRcv8#n65"),
          new String[] {
            String.format("Password must match pattern '%s'.", "\\d\\d\\d\\d"),
          },
        },
      };
  }
}

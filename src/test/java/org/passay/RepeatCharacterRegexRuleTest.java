/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link RepeatCharacterRegexRule}.
 *
 * @author  Middleware Services
 */
public class RepeatCharacterRegexRuleTest extends AbstractRuleTest
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
          new RepeatCharacterRegexRule(),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // test repeating character
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65"),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },
        // test longer repeating character
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4vvvvvvv#n65"),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },

        // test valid password for long regex
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // test long regex with short repeat
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData("p4&&&&&#n65"),
          null,
        },
        // test long regex with long repeat
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData("p4vvvvvvv#n65"),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
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
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65"),
          new String[] {
            String.format(
              "Password matches the illegal pattern '%s'.", "&&&&&"),
          },
        },
      };
  }
}

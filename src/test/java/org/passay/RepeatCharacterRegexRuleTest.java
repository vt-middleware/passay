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
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
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
        // test multiple matches
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65FFFFF"),
          codes(RepeatCharacterRegexRule.ERROR_CODE, RepeatCharacterRegexRule.ERROR_CODE),
        },
        // test single match
        {
          new RepeatCharacterRegexRule(5, false),
          new PasswordData("p4&&&&&#n65FFFFF"),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65FFFFFQr1&&&&&"),
          codes(RepeatCharacterRegexRule.ERROR_CODE, RepeatCharacterRegexRule.ERROR_CODE),
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
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "&&&&&"), },
        },
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65FFFFF"),
          new String[] {
            String.format("Password matches the illegal pattern '%s'.", "&&&&&"),
            String.format("Password matches the illegal pattern '%s'.", "FFFFF"), },
        },
        {
          new RepeatCharacterRegexRule(5, false),
          new PasswordData("p4&&&&&#n65FFFFF"),
          new String[] {String.format("Password matches the illegal pattern '%s'.", "&&&&&"), },
        },
        {
          new RepeatCharacterRegexRule(),
          new PasswordData("p4&&&&&#n65FFFFFQr1&&&&&"),
          new String[] {
            String.format("Password matches the illegal pattern '%s'.", "&&&&&"),
            String.format("Password matches the illegal pattern '%s'.", "FFFFF"), },
        },
      };
  }
}

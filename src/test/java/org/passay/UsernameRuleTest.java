/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link UsernameRule}.
 *
 * @author  Middleware Services
 */
public class UsernameRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // valid password
        {
          new UsernameRule(),
          new PasswordData("p4t3stu$er#n65"),
          null,
        },
        {
          new UsernameRule(),
          new PasswordData("", "p4t3stu$er#n65"),
          null,
        },
        {
          new UsernameRule(),
          new PasswordData("testuser", "p4t3stu$er#n65"),
          null,
        },
        // match username
        {
          new UsernameRule(),
          new PasswordData("testuser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(),
          new PasswordData("TestUser", "p4TestUser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        // negative testing for backwards and case sensitive
        {
          new UsernameRule(),
          new PasswordData("testuser", "p4resutset#n65"),
          null,
        },
        {
          new UsernameRule(),
          new PasswordData("testuser", "p4TEStuSER#n65"),
          null,
        },
        {
          new UsernameRule(),
          new PasswordData("TestUser", "p4testuser#n65"),
          null,
        },
        {
          new UsernameRule(),
          new PasswordData("testuser", "p4RESUTsET#n65"),
          null,
        },
        // backwards matching
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4t3stu$er#n65"),
          null,
        },
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4resutset#n65"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4TEStuSER#n65"),
          null,
        },
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4RESUTsET#n65"),
          null,
        },
        // case insensitive matching
        {
          new UsernameRule(false, true),
          new PasswordData("testuser", "p4t3stu$er#n65"),
          null,
        },
        {
          new UsernameRule(false, true),
          new PasswordData("testuser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          new PasswordData("TestUser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          new PasswordData("testuser", "p4resutset#n65"),
          null,
        },
        {
          new UsernameRule(false, true),
          new PasswordData("testuser", "p4TEStuSER#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          new PasswordData("testuser", "p4RESUTsET#n65"),
          null,
        },
        // both backwards and case insensitive matching
        {
          new UsernameRule(true, true),
          new PasswordData("testuser", "p4t3stu$er#n65"),
          null,
        },
        {
          new UsernameRule(true, true),
          new PasswordData("testuser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          new PasswordData("TestUser", "p4testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          new PasswordData("testuser", "p4resutset#n65"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, true),
          new PasswordData("TestUser", "p4resutset#n65"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, true),
          new PasswordData("testuser", "p4TEStuSER#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          new PasswordData("testuser", "p4RESUTsET#n65"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        // test match behavior
        {
          new UsernameRule(MatchBehavior.StartsWith),
          new PasswordData("testuser", "testuser#n65"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(MatchBehavior.StartsWith),
          new PasswordData("testuser", "p4testuser#n65"),
          null,
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          new PasswordData("testuser", "p4#n65testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          new PasswordData("testuser", "p4testuser#n65"),
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
          new UsernameRule(),
          new PasswordData("testuser", "p4testuser#n65"),
          new String[] {String.format("Password contains the user id '%s'.", "testuser"), },
        },
        {
          new UsernameRule(true, false),
          new PasswordData("testuser", "p4resutset#n65"),
          new String[] {String.format("Password contains the user id '%s' in reverse.", "testuser"), },
        },
        {
          new UsernameRule(MatchBehavior.StartsWith),
          new PasswordData("testuser", "testuser#n65"),
          new String[] {String.format("Password starts with the user id '%s'.", "testuser"), },
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          new PasswordData("testuser", "p4#n65testuser"),
          new String[] {String.format("Password ends with the user id '%s'.", "testuser"), },
        },
      };
  }
}

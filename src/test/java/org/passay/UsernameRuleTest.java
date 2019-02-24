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
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
    throws Exception
  {
    return
      new Object[][] {
        // valid password
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4t3stu$er#n65", null),
          null,
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4t3stu$er#n65", ""),
          null,
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4t3stu$er#n65", "testuser"),
          null,
        },
        // match username
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4TestUser#n65", "TestUser"),
          codes(UsernameRule.ERROR_CODE),
        },
        // negative testing for backwards and case sensitive
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4resutset#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4TEStuSER#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4testuser#n65", "TestUser"),
          null,
        },
        {
          new UsernameRule(),
          TestUtils.newPasswordData("p4RESUTsET#n65", "testuser"),
          null,
        },
        // backwards matching
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4t3stu$er#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4resutset#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4TEStuSER#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4RESUTsET#n65", "testuser"),
          null,
        },
        // case insensitive matching
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4t3stu$er#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4testuser#n65", "TestUser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4resutset#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4TEStuSER#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(false, true),
          TestUtils.newPasswordData("p4RESUTsET#n65", "testuser"),
          null,
        },
        // both backwards and case insensitive matching
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4t3stu$er#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4testuser#n65", "TestUser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4resutset#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4resutset#n65", "TestUser"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4TEStuSER#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(true, true),
          TestUtils.newPasswordData("p4RESUTsET#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        // test match behavior
        {
          new UsernameRule(MatchBehavior.StartsWith),
          TestUtils.newPasswordData("testuser#n65", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(MatchBehavior.StartsWith),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          null,
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          TestUtils.newPasswordData("p4#n65testuser", "testuser"),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
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
          new UsernameRule(),
          TestUtils.newPasswordData("p4testuser#n65", "testuser"),
          new String[] {String.format("Password contains the user id '%s'.", "testuser"), },
        },
        {
          new UsernameRule(true, false),
          TestUtils.newPasswordData("p4resutset#n65", "testuser"),
          new String[] {String.format("Password contains the user id '%s' in reverse.", "testuser"), },
        },
        {
          new UsernameRule(MatchBehavior.StartsWith),
          TestUtils.newPasswordData("testuser#n65", "testuser"),
          new String[] {String.format("Password starts with the user id '%s'.", "testuser"), },
        },
        {
          new UsernameRule(MatchBehavior.EndsWith),
          TestUtils.newPasswordData("p4#n65testuser", "testuser"),
          new String[] {String.format("Password ends with the user id '%s'.", "testuser"), },
        },
      };
  }
}

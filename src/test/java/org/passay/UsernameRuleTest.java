/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link UsernameRule}.
 *
 * @author  Middleware Services
 */
public class UsernameRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "p4t3stu$er#n65";

  /** Test password. */
  private static final String USERID_PASS = "p4testuser#n65";

  /** Test password. */
  private static final String BACKWARDS_USERID_PASS = "p4resutset#n65";

  /** Test password. */
  private static final String UPPERCASE_USERID_PASS = "p4TEStuSER#n65";

  /** Test password. */
  private static final String BACKWARDS_UPPERCASE_USERID_PASS = "p4RESUTsET#n65";

  /** Test username. */
  private static final String USER = "testuser";

  /** For testing. */
  private final UsernameRule rule = new UsernameRule();

  /** For testing. */
  private final UsernameRule backwardsRule = new UsernameRule();

  /** For testing. */
  private final UsernameRule ignoreCaseRule = new UsernameRule();

  /** For testing. */
  private final UsernameRule allRule = new UsernameRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    backwardsRule.setMatchBackwards(true);
    ignoreCaseRule.setIgnoreCase(true);
    allRule.setMatchBackwards(true);
    allRule.setIgnoreCase(true);
  }


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

        {
          rule,
          TestUtils.newPasswordData(VALID_PASS, null),
          null,
        },
        {
          rule,
          TestUtils.newPasswordData(VALID_PASS, ""),
          null,
        },
        {
          rule,
          TestUtils.newPasswordData(VALID_PASS, USER),
          null,
        },
        {
          rule,
          TestUtils.newPasswordData(USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          rule,
          TestUtils.newPasswordData(BACKWARDS_USERID_PASS, USER),
          null,
        },
        {
          rule,
          TestUtils.newPasswordData(UPPERCASE_USERID_PASS, USER),
          null,
        },
        {
          rule,
          TestUtils.newPasswordData(BACKWARDS_UPPERCASE_USERID_PASS, USER),
          null,
        },

        {
          backwardsRule,
          TestUtils.newPasswordData(VALID_PASS, USER),
          null,
        },
        {
          backwardsRule,
          TestUtils.newPasswordData(USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          backwardsRule,
          TestUtils.newPasswordData(BACKWARDS_USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          backwardsRule,
          TestUtils.newPasswordData(UPPERCASE_USERID_PASS, USER),
          null,
        },
        {
          backwardsRule,
          TestUtils.newPasswordData(BACKWARDS_UPPERCASE_USERID_PASS, USER),
          null,
        },

        {
          ignoreCaseRule,
          TestUtils.newPasswordData(VALID_PASS, USER),
          null,
        },
        {
          ignoreCaseRule,
          TestUtils.newPasswordData(USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          ignoreCaseRule,
          TestUtils.newPasswordData(BACKWARDS_USERID_PASS, USER),
          null,
        },
        {
          ignoreCaseRule,
          TestUtils.newPasswordData(UPPERCASE_USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          ignoreCaseRule,
          TestUtils.newPasswordData(BACKWARDS_UPPERCASE_USERID_PASS, USER),
          null,
        },

        {
          allRule,
          TestUtils.newPasswordData(VALID_PASS, USER),
          null,
        },
        {
          allRule,
          TestUtils.newPasswordData(USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          allRule,
          TestUtils.newPasswordData(BACKWARDS_USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE_REVERSED),
        },
        {
          allRule,
          TestUtils.newPasswordData(UPPERCASE_USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE),
        },
        {
          allRule,
          TestUtils.newPasswordData(BACKWARDS_UPPERCASE_USERID_PASS, USER),
          codes(UsernameRule.ERROR_CODE_REVERSED),
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
          rule,
          TestUtils.newPasswordData(USERID_PASS, USER),
          new String[] {String.format("Password contains the user id '%s'.", USER), },
        },
        {
          backwardsRule,
          TestUtils.newPasswordData(BACKWARDS_USERID_PASS, USER),
          new String[] {String.format("Password contains the user id '%s' in reverse.", USER), },
        },
      };
  }
}

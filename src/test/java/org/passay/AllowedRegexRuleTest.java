/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final Rule rule = new AllowedRegexRule("\\d\\d\\d\\d");
    final RuleResult result = rule.validate(
      new PasswordData("p4zRcv8#n65"));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format("Password must match pattern '%s'.", "\\d\\d\\d\\d"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

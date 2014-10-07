/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link RegexRule}.
 *
 * @author  Middleware Services
 */
public class RegexRuleTest extends AbstractRuleTest
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
          new RegexRule("\\d\\d\\d\\d"),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // test entire password
        {
          new RegexRule("^[\\p{Alpha}]+\\d\\d\\d\\d$"),
          new PasswordData("pwUiNh0248"),
          codes(RegexRule.ERROR_CODE),
        },
        // test find password
        {
          new RegexRule("\\d\\d\\d\\d"),
          new PasswordData("pwUi0248xwK"),
          codes(RegexRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final Rule rule = new RegexRule("\\d\\d\\d\\d");
    final RuleResult result = rule.validate(
      new PasswordData("pwUiNh0248"));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format("Password matches the illegal sequence '%s'.", "0248"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

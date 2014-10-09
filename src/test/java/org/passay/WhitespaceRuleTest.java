/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link WhitespaceRule}.
 *
 * @author  Middleware Services
 */
public class WhitespaceRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "AycDPdsyz";

  /** Test password. */
  private static final String SPACE_PASS = "AycD" + " " + "Pdsyz";

  /** Test password. */
  private static final String TAB_PASS = "Ayc" + "\t" + "DPdsyz";

  /** Test password. */
  private static final String LINE_SEP_PASS =
    "AycDPs" + System.getProperty("line.separator") + "yz";

  /** For testing. */
  private final WhitespaceRule rule = new WhitespaceRule();


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

        {rule, new PasswordData(VALID_PASS), null, },
        {
          rule,
          new PasswordData(SPACE_PASS),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData(TAB_PASS),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData(LINE_SEP_PASS),
          codes(WhitespaceRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final RuleResult result = rule.validate(new PasswordData(SPACE_PASS));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        "Password cannot contain whitespace characters.",
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

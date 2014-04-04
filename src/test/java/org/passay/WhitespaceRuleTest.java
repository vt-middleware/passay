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
  private static final Password VALID_PASS = new Password("AycDPdsyz");

  /** Test password. */
  private static final Password SPACE_PASS = new Password(
    "AycD" + " " + "Pdsyz");

  /** Test password. */
  private static final Password TAB_PASS = new Password(
    "Ayc" + "\t" + "DPdsyz");

  /** Test password. */
  private static final Password LINE_SEP_PASS = new Password(
    "AycDPs" + System.getProperty("line.separator") + "yz");

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

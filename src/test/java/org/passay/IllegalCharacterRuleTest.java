/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link IllegalCharacterRule}.
 *
 * @author  Middleware Services
 */
public class IllegalCharacterRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final Password VALID_PASS = new Password("AycDPdsyz");

  /** Test password. */
  private static final Password INVALID_PASS = new Password("AycD@Pdsyz");

  /** For testing. */
  private final IllegalCharacterRule rule = new IllegalCharacterRule(
    new char[] {'@'});


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
          new PasswordData(INVALID_PASS),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final RuleResult result = rule.validate(new PasswordData(INVALID_PASS));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format("Password contains the illegal character '%s'.", "@"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

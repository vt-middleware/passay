/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link AllowedCharacterRule}.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final Password VALID_PASS = new Password("boepselwezz");

  /** Test password. */
  private static final Password INVALID_PASS = new Password("gbwersco4kk");

  /** For testing. */
  private final AllowedCharacterRule rule = new AllowedCharacterRule(
    new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', });


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
          codes(AllowedCharacterRule.ERROR_CODE),
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
        String.format("Password contains the illegal character '%s'.", "4"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

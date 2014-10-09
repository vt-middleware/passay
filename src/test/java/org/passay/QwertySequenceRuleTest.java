/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link QwertySequenceRule}.
 *
 * @author  Middleware Services
 */
public class QwertySequenceRuleTest extends AbstractRuleTest
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
        // Test valid password
        {
          new QwertySequenceRule(),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has qwerty sequence
        {
          new QwertySequenceRule(6, false),
          new PasswordData("pqwerty#n65"),
          codes(QwertySequenceRule.ERROR_CODE),
        },
        // Has wrapping qwerty sequence with wrap=false
        {
          new QwertySequenceRule(),
          new PasswordData("pkl;'a#n65"),
          null,
        },
        // Has wrapping qwerty sequence with wrap=true
        {
          new QwertySequenceRule(8, true),
          new PasswordData("piop{}|qw#n65"),
          codes(QwertySequenceRule.ERROR_CODE),
        },
        // Has backward qwerty sequence
        {
          new QwertySequenceRule(4, false),
          new PasswordData("p7^54#n65"),
          codes(QwertySequenceRule.ERROR_CODE, QwertySequenceRule.ERROR_CODE),
        },
        // Has backward wrapping qwerty sequence with wrap=false
        {
          new QwertySequenceRule(8, false),
          new PasswordData("phgfdsa\";#n65"),
          null,
        },
        // Has backward wrapping qwerty sequence with wrap=true
        {
          new QwertySequenceRule(6, true),
          new PasswordData("p@1`+_0#n65"),
          codes(QwertySequenceRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final Rule rule = new QwertySequenceRule();
    final RuleResult result = rule.validate(
      new PasswordData("pkl;'asd65"));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format("Password contains the illegal sequence '%s'.", ";'asd"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

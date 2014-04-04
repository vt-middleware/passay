/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link RepeatCharacterRegexRule}.
 *
 * @author  Middleware Services
 */
public class RepeatCharacterRegexRuleTest extends AbstractRuleTest
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
          new RepeatCharacterRegexRule(),
          new PasswordData(new Password("p4zRcv8#n65")),
          null,
        },
        // test repeating character
        {
          new RepeatCharacterRegexRule(),
          new PasswordData(new Password("p4&&&&&#n65")),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },
        // test longer repeating character
        {
          new RepeatCharacterRegexRule(),
          new PasswordData(new Password("p4vvvvvvv#n65")),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },

        // test valid password for long regex
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData(new Password("p4zRcv8#n65")),
          null,
        },
        // test long regex with short repeat
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData(new Password("p4&&&&&#n65")),
          null,
        },
        // test long regex with long repeat
        {
          new RepeatCharacterRegexRule(7),
          new PasswordData(new Password("p4vvvvvvv#n65")),
          codes(RepeatCharacterRegexRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final Rule rule = new RepeatCharacterRegexRule();
    final RuleResult result = rule.validate(
      new PasswordData(new Password("p4&&&&&#n65")));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format("Password matches the illegal sequence '%s'.", "&&&&&"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

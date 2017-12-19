/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link LengthRule}.
 *
 * @author  Middleware Services
 */
public class LengthRuleTest extends AbstractRuleTest
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

        {new LengthRule(8, 10), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(8, 10), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(8, 10), new PasswordData("p4T3to#6Tu"), null, },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(8), new PasswordData("p4T3#6Tu"), null, },
        {
          new LengthRule(8),
          new PasswordData("p4T3t#6Tu"),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T3to#6Tu"),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(0, 10), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T3to#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T36"), null, },
        {
          new LengthRule(0, 10),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3to#6Tu"), null, },
        {
          new LengthRule(8, Integer.MAX_VALUE),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3j76rE@#"), null, },
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
          new LengthRule(8, 10),
          new PasswordData("p4T3j76rE@#"),
          new String[] {
            String.format("Password must be no more than %s characters in length.", 10),
          },
        },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T36"),
          new String[] {String.format("Password must be %s or more characters in length.", 8), },
        },
      };
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"passtest"})
  public void checkMetadata()
    throws Exception
  {
    final LengthRule rule = new LengthRule(4, 10);
    RuleResult result = rule.validate(new PasswordData("metadata"));
    AssertJUnit.assertTrue(result.isValid());
    AssertJUnit.assertEquals(8, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Length));

    result = rule.validate(new PasswordData("md"));
    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals(2, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Length));
  }
}

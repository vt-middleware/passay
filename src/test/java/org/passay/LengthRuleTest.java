/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link LengthRule}.
 *
 * @author  Middleware Services
 */
public class LengthRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String MIN_VALID_PASS = "p4T3#6Tu";

  /** Test password. */
  private static final String MID_VALID_PASS = "p4T3t#6Tu";

  /** Test password. */
  private static final String MAX_VALID_PASS = "p4T3to#6Tu";

  /** Test password. */
  private static final String SHORT_PASS = "p4T36";

  /** Test password. */
  private static final String LONG_PASS = "p4T3j76rE@#";

  /** For testing. */
  private final LengthRule rule = new LengthRule(8, 10);

  /** For testing. */
  private final LengthRule minRule = new LengthRule(8);

  /** For testing. */
  private final LengthRule noMinRule = new LengthRule();

  /** For testing. */
  private final LengthRule noMaxRule = new LengthRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    noMinRule.setMaximumLength(10);
    noMaxRule.setMinimumLength(8);
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

        {rule, new PasswordData(MIN_VALID_PASS), null, },
        {rule, new PasswordData(MID_VALID_PASS), null, },
        {rule, new PasswordData(MAX_VALID_PASS), null, },
        {
          rule,
          new PasswordData(SHORT_PASS),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          rule,
          new PasswordData(LONG_PASS),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {minRule, new PasswordData(MIN_VALID_PASS), null, },
        {
          minRule,
          new PasswordData(MID_VALID_PASS),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          minRule,
          new PasswordData(MAX_VALID_PASS),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          minRule,
          new PasswordData(SHORT_PASS),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          minRule,
          new PasswordData(LONG_PASS),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {noMinRule, new PasswordData(MIN_VALID_PASS), null, },
        {noMinRule, new PasswordData(MID_VALID_PASS), null, },
        {noMinRule, new PasswordData(MAX_VALID_PASS), null, },
        {noMinRule, new PasswordData(SHORT_PASS), null, },
        {
          noMinRule,
          new PasswordData(LONG_PASS),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {noMaxRule, new PasswordData(MIN_VALID_PASS), null, },
        {noMaxRule, new PasswordData(MID_VALID_PASS), null, },
        {noMaxRule, new PasswordData(MAX_VALID_PASS), null, },
        {
          noMaxRule,
          new PasswordData(SHORT_PASS),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {noMaxRule, new PasswordData(LONG_PASS), null, },
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
          new PasswordData(LONG_PASS),
          new String[] {
            String.format("Password must be no more than %s characters in length.", rule.getMaximumLength()),
          },
        },
        {
          rule,
          new PasswordData(SHORT_PASS),
          new String[] {String.format("Password must be at least %s characters in length.", rule.getMinimumLength()), },
        },
      };
  }
}

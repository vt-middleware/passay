/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link CharacterCharacteristicsRule}.
 *
 * @author  Middleware Services
 */
public class CharacterCharacteristicsRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final Password VALID_PASS = new Password("r%scvEW2e93)");

  /** Test password. */
  private static final Password ALPHA_PASS = new Password("r%5#8EW2393)");

  /** Test password. */
  private static final Password DIGIT_PASS = new Password("r%scvEW2e9e)");

  /** Test password. */
  private static final Password UPPERCASE_PASS = new Password("r%scv3W2e9)");

  /** Test password. */
  private static final Password LOWERCASE_PASS = new Password("R%s4VEW239)");

  /** Test password. */
  private static final Password NONALPHA_PASS = new Password("r5scvEW2e9b");

  /** For testing. */
  private final CharacterCharacteristicsRule rule1 =
    new CharacterCharacteristicsRule();

  /** For testing. */
  private final CharacterCharacteristicsRule rule2 =
    new CharacterCharacteristicsRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    rule1.getRules().add(new AlphabeticalCharacterRule(4));
    rule1.getRules().add(new DigitCharacterRule(3));
    rule1.getRules().add(new UppercaseCharacterRule(2));
    rule1.getRules().add(new LowercaseCharacterRule(2));
    rule1.getRules().add(new NonAlphanumericCharacterRule(1));
    rule1.setNumberOfCharacteristics(5);

    rule2.getRules().add(new DigitCharacterRule(1));
    rule2.getRules().add(new NonAlphanumericCharacterRule(1));
    rule2.getRules().add(new UppercaseCharacterRule(1));
    rule2.getRules().add(new LowercaseCharacterRule(1));
    rule2.setNumberOfCharacteristics(3);
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

        {rule1, new PasswordData(VALID_PASS), null, },
        {
          rule1,
          new PasswordData(ALPHA_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            AlphabeticalCharacterRule.ERROR_CODE,
            LowercaseCharacterRule.ERROR_CODE),
        },
        {
          rule1,
          new PasswordData(DIGIT_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            DigitCharacterRule.ERROR_CODE),
        },
        {
          rule1,
          new PasswordData(UPPERCASE_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            UppercaseCharacterRule.ERROR_CODE),
        },
        {
          rule1,
          new PasswordData(LOWERCASE_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            LowercaseCharacterRule.ERROR_CODE),
        },
        {
          rule1,
          new PasswordData(NONALPHA_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            NonAlphanumericCharacterRule.ERROR_CODE),
        },
        {rule2, new PasswordData(VALID_PASS), null, },
        {rule2, new PasswordData(ALPHA_PASS), null, },
        {rule2, new PasswordData(DIGIT_PASS), null, },
        {rule2, new PasswordData(UPPERCASE_PASS), null, },
        {rule2, new PasswordData(LOWERCASE_PASS), null, },
        {rule2, new PasswordData(NONALPHA_PASS), null, },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void checkConsistency()
    throws Exception
  {
    final CharacterCharacteristicsRule ccr = new CharacterCharacteristicsRule();
    try {
      ccr.validate(new PasswordData(VALID_PASS));
      AssertJUnit.fail("Should have thrown IllegalStateException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalStateException.class, e.getClass());
    }
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    // check messages for rule1
    RuleResult result = rule1.validate(
      new PasswordData(new Password("r%scvEW2e3)")));
    for (int i = 0; i < result.getDetails().size(); i++) {
      final RuleResultDetail detail = result.getDetails().get(i);
      switch (i) {

      case 0:
        AssertJUnit.assertEquals(
          String.format(
            "Password must contain at least %s %s characters.",
            3,
            "digit"),
          DEFAULT_RESOLVER.resolve(detail));
        break;

      case 1:
        AssertJUnit.assertEquals(
          String.format(
            "Password matches %s of %s character rules, but %s are required.",
            4,
            5,
            5),
          DEFAULT_RESOLVER.resolve(detail));
        break;

      default:
        AssertJUnit.fail("Invalid index");
        break;
      }
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }

    // check messages for rule2
    result = rule2.validate(new PasswordData(new Password("rscvew2e3")));
    for (int i = 0; i < result.getDetails().size(); i++) {
      final RuleResultDetail detail = result.getDetails().get(i);
      switch (i) {

      case 0:
        AssertJUnit.assertEquals(
          String.format(
            "Password must contain at least %s %s characters.",
            1,
            "non-alphanumeric"),
          DEFAULT_RESOLVER.resolve(detail));
        break;

      case 1:
        AssertJUnit.assertEquals(
          String.format(
            "Password must contain at least %s %s characters.",
            1,
            "uppercase"),
          DEFAULT_RESOLVER.resolve(detail));
        break;

      case 2:
        AssertJUnit.assertEquals(
          String.format(
            "Password matches %s of %s character rules, but %s are required.",
            2,
            4,
            3),
          DEFAULT_RESOLVER.resolve(detail));
        break;

      default:
        AssertJUnit.fail("Invalid index");
        break;
      }
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

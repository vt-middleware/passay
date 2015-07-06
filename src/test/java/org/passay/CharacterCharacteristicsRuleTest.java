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
  private static final String VALID_PASS = "r%scvEW2e93)";

  /** Test password. */
  private static final String VALID_PASS_ALT = "r¢sCvE±2e93";

  /** Test password. */
  private static final String ALPHA_PASS = "r%5#8EW2393)";

  /** Test password. */
  private static final String DIGIT_PASS = "r%scvEW2e9e)";

  /** Test password. */
  private static final String UPPERCASE_PASS = "r%scv3W2e9)";

  /** Test password. */
  private static final String LOWERCASE_PASS = "R%s4VEW239)";

  /** Test password. */
  private static final String NONALPHA_PASS = "r5scvEW2e9b";

  /** For testing. */
  private final CharacterCharacteristicsRule rule1 = new CharacterCharacteristicsRule();

  /** For testing. */
  private final CharacterCharacteristicsRule rule2 = new CharacterCharacteristicsRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    rule1.getRules().add(new CharacterRule(EnglishCharacterData.Alphabetical, 4));
    rule1.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 3));
    rule1.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 2));
    rule1.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 2));
    rule1.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    rule1.setNumberOfCharacteristics(5);

    rule2.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
    rule2.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    rule2.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    rule2.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
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
        {rule1, new PasswordData(VALID_PASS_ALT), null, },
        {
          rule1,
          new PasswordData(ALPHA_PASS),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Alphabetical.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },
        {
          rule1,
          new PasswordData(DIGIT_PASS),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.Digit.getErrorCode()),
        },
        {
          rule1,
          new PasswordData(UPPERCASE_PASS),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.UpperCase.getErrorCode()),
        },
        {
          rule1,
          new PasswordData(LOWERCASE_PASS),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.LowerCase.getErrorCode()),
        },
        {
          rule1,
          new PasswordData(NONALPHA_PASS),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.Special.getErrorCode()),
        },
        {rule2, new PasswordData(VALID_PASS), null, },
        {rule2, new PasswordData(VALID_PASS_ALT), null, },
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
          rule1,
          new PasswordData("r%scvEW2e3)"),
          new String[] {
            String.format("Password must contain at least %s digit characters.", 3),
            String.format("Password matches %s of %s character rules, but %s are required.", 4, 5, 5),
          },
        },
        {
          rule1,
          new PasswordData("R»S7VEW2e3)"),
          new String[] {
            String.format("Password must contain at least %s lowercase characters.", 2),
            String.format("Password matches %s of %s character rules, but %s are required.", 4, 5, 5),
          },
        },
        {
          rule2,
          new PasswordData("rscvew2e3"),
          new String[] {
            String.format("Password must contain at least %s special characters.", 1),
            String.format("Password must contain at least %s uppercase characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 2, 4, 3),
          },
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void customResolver()
    throws Exception
  {
    final CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule();
    rule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 3));
    rule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 2));
    rule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 2));
    rule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    rule.setNumberOfCharacteristics(2);
    rule.setReportRuleFailures(false);

    final TestMessageResolver resolver = new TestMessageResolver(
      "INSUFFICIENT_CHARACTERISTICS",
      "Passwords must contain at least %2$s of the following: " +
      "three digits, two uppercase characters, two lowercase characters, " +
      "and one special character");
    final RuleResult result = rule.validate(new PasswordData("rscvE2e3"));
    AssertJUnit.assertEquals(1, result.getDetails().size());

    final RuleResultDetail detail = result.getDetails().get(0);
    AssertJUnit.assertEquals(
      String.format(
        "Passwords must contain at least %s of the following: " +
        "three digits, two uppercase characters, two lowercase characters, " +
        "and one special character",
        2),
      resolver.resolve(detail));
  }
}

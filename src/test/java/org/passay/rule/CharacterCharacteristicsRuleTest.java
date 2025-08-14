/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.data.EnglishCharacterData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link CharacterCharacteristicsRule}.
 *
 * @author  Middleware Services
 */
public class CharacterCharacteristicsRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private CharacterCharacteristicsRule rule1;

  /** For testing. */
  private CharacterCharacteristicsRule rule2;


  /** Initialize rules for this test. */
  @BeforeClass(groups = "passtest")
  public void createRules()
  {
    rule1 = new CharacterCharacteristicsRule(
      5,
      new CharacterRule(EnglishCharacterData.Alphabetical, 4),
      new CharacterRule(EnglishCharacterData.Digit, 3),
      new CharacterRule(EnglishCharacterData.UpperCase, 2),
      new CharacterRule(EnglishCharacterData.LowerCase, 2),
      new CharacterRule(EnglishCharacterData.Special, 1));

    rule2 = new CharacterCharacteristicsRule(
      false,
      true,
      3,
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1));
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // valid ascii password
        {rule1, new PasswordData("r%scvEW2e93)"), null, },
        // valid non-ascii password
        {rule1, new PasswordData("r¢sCvE±2e93"), null, },
        // valid non-ascii password
        {rule1, new PasswordData("r\u16C8sCvE±2e93"), null, },
        // valid non-ascii password
        {rule1, new PasswordData("r\uD808\uDF34sCvE±2e93"), null, },
        // valid non-ascii password
        {rule1, new PasswordData("r\uD83C\uDDEE\uD83C\uDDF8sCvE±2e93"), null, },
        // issue #32
        {rule1, new PasswordData("r~scvEW2e93b"), null, },
        // missing lowercase
        {
          rule1,
          new PasswordData("r%5#8EW2393)"),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Alphabetical.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },
        // missing 3 digits
        {
          rule1,
          new PasswordData("r%scvEW2e9e)"),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.Digit.getErrorCode()),
        },
        // missing 2 uppercase
        {
          rule1,
          new PasswordData("r%scv3W2e9)"),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.UpperCase.getErrorCode()),
        },
        // missing 2 lowercase
        {
          rule1,
          new PasswordData("R%s4VEW239)"),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.LowerCase.getErrorCode()),
        },
        // missing 1 special
        {
          rule1,
          new PasswordData("r5scvEW2e9b"),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.Special.getErrorCode()),
        },
        // missing 1 special with unicode
        {
          rule1,
          new PasswordData("r\uD808\uDF345scvEW2e9b"),
          codes(CharacterCharacteristicsRule.ERROR_CODE, EnglishCharacterData.Special.getErrorCode()),
        },
        // previous passwords all valid under different rule set
        {rule2, new PasswordData("r%scvEW2e93)"), null, },
        {rule2, new PasswordData("r¢sCvE±2e93"), null, },
        {rule2, new PasswordData("r%5#8EW2393)"), null, },
        {rule2, new PasswordData("r%scvEW2e9e)"), null, },
        {rule2, new PasswordData("r%scv3W2e9)"), null, },
        {rule2, new PasswordData("R%s4VEW239)"), null, },
        {rule2, new PasswordData("r5scvEW2e9b"), null, },
      };
  }


  /**
   * Test consistency.
   */
  @Test(groups = "passtest")
  public void checkConsistency()
  {
    final CharacterCharacteristicsRule ccr = new CharacterCharacteristicsRule();
    try {
      ccr.validate(new PasswordData("r%scvEW2e93)"));
      fail("Should have thrown IllegalStateException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalStateException.class);
    }
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          rule1,
          new PasswordData("r%scvEW2e3)"),
          new String[] {
            String.format("Password must contain %s or more digit characters.", 3),
            String.format("Password matches %s of %s character rules, but %s are required.", 4, 5, 5),
          },
        },
        {
          rule1,
          new PasswordData("R»S7VEW2e3)"),
          new String[] {
            String.format("Password must contain %s or more lowercase characters.", 2),
            String.format("Password matches %s of %s character rules, but %s are required.", 4, 5, 5),
          },
        },
        {
          rule2,
          new PasswordData("rscvew2e3"),
          new String[] {
            String.format("Password must contain %s or more special characters.", 1),
            String.format("Password must contain %s or more uppercase characters.", 1),
          },
        },
      };
  }


  /**
   * Test custom resolver.
   */
  @Test(groups = "passtest")
  public void customResolver()
  {
    final CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule(
      true,
      false,
      2,
      new CharacterRule(EnglishCharacterData.Digit, 3),
      new CharacterRule(EnglishCharacterData.UpperCase, 2),
      new CharacterRule(EnglishCharacterData.LowerCase, 2),
      new CharacterRule(EnglishCharacterData.Special, 1));

    final TestMessageResolver resolver = new TestMessageResolver(
      "INSUFFICIENT_CHARACTERISTICS",
      "Passwords must contain at least %2$s of the following: " +
      "three digits, two uppercase characters, two lowercase characters, " +
      "and one special character");
    final RuleResult result = rule.validate(new PasswordData("rscvE2e3"));
    assertThat(result.getDetails().size()).isEqualTo(1);

    final RuleResultDetail detail = result.getDetails().get(0);
    assertThat(resolver.resolve(detail))
      .isEqualTo(
        String.format(
          "Passwords must contain at least %s of the following: " +
          "three digits, two uppercase characters, two lowercase characters, " +
          "and one special character",
          2));
  }

  /**
   * Test Metadata.
   */
  @Test(groups = "passtest")
  public void checkMetadata()
  {
    final CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule(
      2, new CharacterRule(EnglishCharacterData.Digit, 1), new CharacterRule(EnglishCharacterData.LowerCase, 1));
    RuleResult result = rule.validate(new PasswordData("meTAdata01"));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Digit)).isEqualTo(2);
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.LowerCase)).isEqualTo(6);

    result = rule.validate(new PasswordData("meTAdataOne"));
    assertThat(result.isValid()).isFalse();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Digit)).isEqualTo(0);
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.LowerCase)).isEqualTo(8);
  }
}

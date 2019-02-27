/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PasswordGenerator}.
 *
 * @author  Sean C. Sullivan
 * @author  Middleware Services
 */
public class PasswordGeneratorTest
{

  /** To generate passwords with. */
  private final PasswordGenerator generator = new PasswordGenerator();

  /** Rule to generate passwords with. */
  private final CharacterCharacteristicsRule genCharRule = new CharacterCharacteristicsRule();

  /** Rule to verify passwords with. */
  private final CharacterCharacteristicsRule verifyCharRule = new CharacterCharacteristicsRule();

  /** Rule to verify passwords with that should fail. */
  private final CharacterCharacteristicsRule failCharRule = new CharacterCharacteristicsRule();

  /** Rules to test. */
  private final List<CharacterRule> rules = new ArrayList<>();

  /** Rules to test. */
  private final List<CharacterRule> failRules = new ArrayList<>();

  /**
   * Setup test resources.
   */
  @BeforeClass(groups = {"passgentest"})
  public void initializeRules()
  {
    rules.add(new CharacterRule(EnglishCharacterData.Digit, 2));
    rules.add(new CharacterRule(EnglishCharacterData.Special, 2));
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));

    genCharRule.getRules().addAll(rules);
    genCharRule.setNumberOfCharacteristics(3);

    verifyCharRule.getRules().addAll(rules);
    verifyCharRule.setNumberOfCharacteristics(3);

    failRules.add(new CharacterRule(EnglishCharacterData.Digit, 3));
    failRules.add(new CharacterRule(EnglishCharacterData.Special, 3));
    failRules.add(new CharacterRule(EnglishCharacterData.UpperCase, 3));
    failRules.add(new CharacterRule(EnglishCharacterData.LowerCase, 3));

    failCharRule.getRules().addAll(failRules);
    failCharRule.setNumberOfCharacteristics(4);
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "randomPasswords")
  public Object[][] randomPasswords()
  {
    final Object[][] passwords = new Object[100][1];
    final int length = 10;
    for (int i = 0; i < 100; i++) {
      final String password = generator.generatePassword(length, rules);
      AssertJUnit.assertNotNull(password);
      AssertJUnit.assertTrue(password.length() >= length);
      passwords[i] = new Object[] {password};
    }
    return passwords;
  }


  /**
   * @param  pass  to verify
   */
  @Test(groups = {"passgentest"}, dataProvider = "randomPasswords")
  public void testGenerator(final String pass)
  {
    AssertJUnit.assertFalse(failCharRule.validate(new PasswordData(pass)).isValid());
    AssertJUnit.assertTrue(verifyCharRule.validate(new PasswordData(pass)).isValid());
  }
}

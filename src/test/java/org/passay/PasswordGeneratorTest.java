/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
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

  /** Rule to verify passwords with. */
  private final CharacterCharacteristicsRule verifyCharRule = new CharacterCharacteristicsRule();

  /** Rule to verify passwords with that should fail. */
  private final CharacterCharacteristicsRule failCharRule = new CharacterCharacteristicsRule();

  /** Rules to test. */
  private final List<Rule> rules = new ArrayList<>();

  /** Rules to test. */
  private final List<CharacterRule> failRules = new ArrayList<>();

  /**
   * Setup test resources.
   */
  @BeforeClass(groups = "passgentest")
  public void initializeRules()
  {
    final CharacterRule[] characterRules = new CharacterRule[] {
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 20),
      new CharacterRule(EnglishCharacterData.Digit, 1),
    };

    rules.addAll(Arrays.asList(characterRules));
    rules.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical));

    verifyCharRule.getRules().addAll(Arrays.asList(characterRules));
    verifyCharRule.setNumberOfCharacteristics(3);

    for (CharacterRule cr : characterRules) {
      failRules.add(new CharacterRule(cr.getCharacterData(), cr.getNumberOfCharacters() + 1));
    }
    failRules.addAll(Arrays.asList(characterRules));
    // Add a constraint not met by generated passwords
    failRules.add(new CharacterRule(EnglishCharacterData.Special, 1));

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
    final int length = 22;
    for (int i = 0; i < passwords.length; i++) {
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
  @Test(groups = "passgentest", dataProvider = "randomPasswords")
  public void testGenerator(final String pass)
  {
    AssertJUnit.assertFalse(failCharRule.validate(new PasswordData(pass)).isValid());
    AssertJUnit.assertTrue(verifyCharRule.validate(new PasswordData(pass)).isValid());
  }

  /**
   * Performs an iterative test of the generator with enough rounds to likely generate an illegal sequence,
   * which should trigger the retry mechanism.
   */
  @Test(groups = "passgentest")
  public void testGeneratorWithRetry()
  {
    for (int i = 0; i < 100000; i++) {
      try {
        generator.generatePassword(22, rules);
      } catch (IllegalStateException e) {
        if (!e.getMessage().equals("Exceeded maximum number of password generation retries")) {
          throw e;
        }
      }
    }
    AssertJUnit.assertTrue(generator.getRetryCount() > 0);
  }


  /**
   */
  @Test(groups = "passgentest")
  public void testBufferOverflow()
  {
    try {
      new PasswordGenerator().generatePassword(5, new CharacterRule(EnglishCharacterData.LowerCase, 10));
      AssertJUnit.fail("Should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      if (!e.getMessage().equals("Exceeded maximum number of password generation retries")) {
        AssertJUnit.fail("Unexpected error message:" + e.getMessage());
      }
    }
    new PasswordGenerator().generatePassword(10, new CharacterRule(EnglishCharacterData.LowerCase, 5));
    new PasswordGenerator().generatePassword(10, new CharacterRule(EnglishCharacterData.LowerCase, 10));
  }
}

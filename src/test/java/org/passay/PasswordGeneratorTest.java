/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.IllegalSequenceRule;
import org.passay.rule.RepeatCharactersRule;
import org.passay.rule.Rule;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link PasswordGenerator}.
 *
 * @author  Sean C. Sullivan
 * @author  Middleware Services
 */
public class PasswordGeneratorTest
{
  /**
   * @return Test parameters consisting of an array of rules.
   */
  @DataProvider(name = "ruleSets")
  public Object[][] ruleSets()
  {
    return new Object[][] {
      new Object[] {
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        new CharacterRule(EnglishCharacterData.LowerCase, 20),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
      },
      // Test case for https://github.com/vt-middleware/passay/issues/158
      new Object[] {
        new CharacterRule(EnglishCharacterData.Digit, 3),
        new CharacterRule(EnglishCharacterData.UpperCase, 3),
        new CharacterRule(EnglishCharacterData.LowerCase, 3),
        new RepeatCharactersRule(3, 1),
      },
    };
  }

  /**
   * Performs an iterative test of the generator with enough rounds to likely generate an illegal sequence,
   * which should trigger the retry mechanism.
   *
   * @param rules Password validation rules. Should NOT include characteristics as these are managed by the test.
   */
  @Test(groups = "passgentest", dataProvider = "ruleSets")
  public void testGenerateWithRetry(final Rule... rules)
  {
    final List<Rule> ruleSet = Arrays.asList(rules);
    final List<Rule> passRules = addCharacteristics(ruleSet);
    final PasswordValidator passValidator = new PasswordValidator(passRules);
    // Add a constraint not met by generated passwords
    final List<Rule> extendedSet = new ArrayList<>(ruleSet);
    extendedSet.add(new CharacterRule(EnglishCharacterData.SpecialUnicode, 1));
    final List<Rule> failRules = addCharacteristics(extendedSet);
    final PasswordValidator failValidator = new PasswordValidator(failRules);
    final PasswordGenerator generator = new PasswordGenerator();
    // Perform a number of rounds likely to produce illegal sequences by random chance
    try {
      for (int i = 0; i < 100000; i++) {
        final String password = generator.generatePassword(22, ruleSet);
        final PasswordData pd = new PasswordData(password);
        assertThat(passValidator.validate(pd).isValid()).isTrue();
        assertThat(failValidator.validate(pd).isValid()).isFalse();
      }
    } catch (IllegalStateException e) {
      if (e.getMessage().equals("Exceeded maximum number of password generation retries")) {
        fail(e.getMessage());
      } else {
        throw e;
      }
    }
    assertThat(generator.getRetryCount()).isGreaterThan(0);
  }

  @Test(groups = "passgentest")
  public void testBufferOverflow()
  {
    try {
      new PasswordGenerator().generatePassword(5, new CharacterRule(EnglishCharacterData.LowerCase, 10));
      fail("Should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      if (!e.getMessage().equals("Exceeded maximum number of password generation retries")) {
        fail("Unexpected error message: %s", e.getMessage());
      }
    }
    new PasswordGenerator().generatePassword(10, new CharacterRule(EnglishCharacterData.LowerCase, 5));
    new PasswordGenerator().generatePassword(10, new CharacterRule(EnglishCharacterData.LowerCase, 10));
    try {
      // cannot generate password with odd number of characters using unicode input (every char has length of 2)
      new PasswordGenerator().generatePassword(9, new CharacterRule(EnglishCharacterData.SpecialLatin, 10));
      fail("Should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      if (!e.getMessage().equals("Exceeded maximum number of password generation retries")) {
        fail("Unexpected error message: %s", e.getMessage());
      }
    }
  }


  private List<Rule> addCharacteristics(final List<Rule> rules)
  {
    final List<Rule> compositeRules = new ArrayList<>(rules);
    final List<CharacterRule> characterRules = rules.stream()
            .filter(r -> r instanceof CharacterRule)
            .map(r -> (CharacterRule) r)
            .collect(Collectors.toList());
    final CharacterCharacteristicsRule characteristics = new CharacterCharacteristicsRule(
      characterRules.size(),
      characterRules);
    compositeRules.add(characteristics);
    return compositeRules;
  }
}

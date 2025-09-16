/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.IllegalCharacterRule;
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
      new Object[] {
        new CharacterRule(EnglishCharacterData.Digit, 3),
        new AllowedCharacterRule(
          new UnicodeString(
            '0', '1', '2', '3', '4', '5',
            'a', 'b', 'c', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'x', 'y', 'z')),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
      },
      new Object[] {
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterCharacteristicsRule(
          new CharacterRule(EnglishCharacterData.UpperCase, 2),
          new CharacterRule(EnglishCharacterData.LowerCase, 3)),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
      },
      new Object[] {
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterCharacteristicsRule(
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1),
          new CharacterRule(EnglishCharacterData.SpecialAscii, 1)),
        new AllowedCharacterRule(new UnicodeString(
          '0', '1', '2', '3', '4', '5',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',  'M', 'N', 'O', 'P',
          'k',  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '!', '@', '#', '^', '&', '*')),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
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
    final DefaultPasswordValidator passValidator = new DefaultPasswordValidator(passRules);
    // Add a constraint not met by generated passwords
    final List<Rule> extendedSet = new ArrayList<>(ruleSet);
    extendedSet.add(new CharacterRule(EnglishCharacterData.SpecialUnicode, 1));
    final List<Rule> failRules = addCharacteristics(extendedSet);
    final DefaultPasswordValidator failValidator = new DefaultPasswordValidator(failRules);
    final PasswordGenerator generator = new PasswordGenerator(22, ruleSet);
    // Perform a number of rounds likely to produce illegal sequences by random chance
    try {
      for (int i = 0; i < 500000; i++) {
        final UnicodeString password = generator.generate();
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
  public void testInvalidRuleCombination()
  {
    try {
      new PasswordGenerator(
        5,
        new AllowedCharacterRule(new UnicodeString('a', 'b', 'c')),
        new IllegalCharacterRule(new UnicodeString('a', 'b', 'c')));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
      assertThat(e.getMessage()).isEqualTo("Rules did not produce any combination of valid characters");
    }

    try {
      new PasswordGenerator(
        5,
        new AllowedCharacterRule(new UnicodeString('a', 'b', 'c')),
        new AllowedCharacterRule(new UnicodeString('x', 'y', 'z')));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
      assertThat(e.getMessage()).isEqualTo("Rules did not produce any combination of valid characters");
    }

    try {
      new PasswordGenerator(
        5,
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        new AllowedCharacterRule(new UnicodeString('a', 'b', 'c', 'd', 'e', 'f')));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
      assertThat(e.getMessage()).isEqualTo("Rules did not produce any combination of valid characters");
    }

    try {
      new PasswordGenerator(
        5,
        new CharacterCharacteristicsRule(
          1,
          new CharacterRule(EnglishCharacterData.Digit, 1),
          new CharacterRule(EnglishCharacterData.UpperCase, 1)),
        new AllowedCharacterRule(new UnicodeString('a', 'b', 'c', 'd', 'e', 'f')));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
      assertThat(e.getMessage()).isEqualTo("Rules did not produce any combination of valid characters");
    }

    try {
      new PasswordGenerator(
        5,
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new IllegalCharacterRule(new UnicodeString('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
      assertThat(e.getMessage()).isEqualTo("Rules did not produce any combination of valid characters");
    }
  }


  @Test(groups = "passgentest")
  public void testBufferOverflow()
  {
    try {
      new PasswordGenerator(5, new CharacterRule(EnglishCharacterData.LowerCase, 10)).generate();
      fail("Should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      if (!e.getMessage().equals("Exceeded maximum number of password generation retries")) {
        fail("Unexpected error message: %s", e.getMessage());
      }
    }
    new PasswordGenerator(10, new CharacterRule(EnglishCharacterData.LowerCase, 5)).generate();
    new PasswordGenerator(10, new CharacterRule(EnglishCharacterData.LowerCase, 10)).generate();
    try {
      // cannot generate password with odd number of characters using unicode input (every char has length of 2)
      new PasswordGenerator(9, new CharacterRule(EnglishCharacterData.SpecialLatin, 10)).generate();
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

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import org.passay.PasswordData;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link LengthComplexityRule}.
 *
 * @author  Middleware Services
 */
public class LengthComplexityRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private LengthComplexityRule rule1;

  /** For testing. */
  private LengthComplexityRule rule2;


  /** Initialize rules for this test. */
  @BeforeClass(groups = "passtest")
  public void createRules()
  {
    rule1 = new LengthComplexityRule(
      new LengthComplexityRule.Entry(
        "[0,12)",
        new LengthRule(8, 64),
        new CharacterCharacteristicsRule(
          4,
          new CharacterRule(EnglishCharacterData.Digit, 1),
          new CharacterRule(EnglishCharacterData.Special, 1),
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1)),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()),
      new LengthComplexityRule.Entry(
        "[12,16)",
        new LengthRule(8, 64),
        new CharacterCharacteristicsRule(
          3,
          new CharacterRule(EnglishCharacterData.Digit, 1),
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1)),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()),
      new LengthComplexityRule.Entry(
        "[16,20)",
        new LengthRule(8, 64),
        new CharacterCharacteristicsRule(
          2,
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1)),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()),
      new LengthComplexityRule.Entry(
        "[20,128]",
        new LengthRule(8, 64),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()));

    rule2 = new LengthComplexityRule(
      false,
      true,
      new LengthComplexityRule.Entry(
        "[0,20)",
        new LengthRule(8, 64),
        new CharacterCharacteristicsRule(
          4,
          new CharacterRule(EnglishCharacterData.Digit, 1),
          new CharacterRule(EnglishCharacterData.Special, 1),
          new CharacterRule(EnglishCharacterData.UpperCase, 1),
          new CharacterRule(EnglishCharacterData.LowerCase, 1)),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()),
      new LengthComplexityRule.Entry(
        "[20,*]",
        new LengthRule(8, 64),
        new UsernameRule(true, true),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new IllegalSequenceRule(EnglishSequenceData.USQwerty),
        new RepeatCharacterRegexRule()));
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // valid passwords in each length range
        {rule1, new PasswordData("alfred", "r%scvEW2e"), null, },
        {rule1, new PasswordData("alfred", "rkscvEW2e93C"), null, },
        {rule1, new PasswordData("alfred", "rkscvEWbePwCOUovqt"), null, },
        {rule1, new PasswordData("alfred", "horse staple battery"), null, },
        {rule1, new PasswordData("alfred", "it was the best of times"), null, },
        {
          rule1,
          new PasswordData("alfred", "r%vE2"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            LengthRule.ERROR_CODE_MIN),
        },
        {
          rule1,
          new PasswordData("alfred", "It was the best of times, it was the worst of times, it was the age of wisdom,"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            LengthRule.ERROR_CODE_MAX),
        },
        {
          rule1,
          new PasswordData(
            "alfred",
            "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of " +
              "foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light,"),
          codes(
            LengthComplexityRule.ERROR_CODE_RULES),
        },
        {
          rule1,
          new PasswordData("alfred", "rPscvEW2e"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Special.getErrorCode()),
        },
        {
          rule1,
          new PasswordData("alfred", "r%scvEWte"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode()),
        },
        {
          rule1,
          new PasswordData("alfred", "r%scvew2e"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode()),
        },
        {
          rule1,
          new PasswordData("alfred", "R%SCVEW2E"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.LowerCase.getErrorCode()),
        },
        {
          rule1,
          new PasswordData("alfred", "rALfredTe"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            UsernameRule.ERROR_CODE,
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Special.getErrorCode(),
            EnglishCharacterData.Digit.getErrorCode()),
        },
        {
          rule1,
          new PasswordData("alfred", "It was the best of eeeee, it was the worst of 87654"),
          codes(
            LengthComplexityRule.ERROR_CODE,
            RepeatCharacterRegexRule.ERROR_CODE,
            EnglishSequenceData.USQwerty.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode()),
        },
        {
          rule2,
          new PasswordData("alfred", "It was the best of eeeee, it was the worst of 87654"),
          codes(
            RepeatCharacterRegexRule.ERROR_CODE,
            EnglishSequenceData.USQwerty.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode()),
        },
      };
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "intervals")
  public Object[][] intervals()
  {
    return
      new Object[][] {

        {"(7,33)", true},
        {"[8,32]", true},
        {"[8,33)", true},
        {"(7,32]", true},
        {"", false},
        {"()", false},
        {"(7,32", false},
        {"7,32)", false},
        {"[,32]", false},
        {"[32]", false},
        {"[8, 32]", false},
        {"(6,6)", false},
        {"[6,6)", false},
        {"(6,6]", false},
        {"[6,6]", true},
        {"(6,5)", false},
        {"[6,5)", false},
        {"(6,5]", false},
        {"[6,5]", false},
        {"[-10,10]", false},
        {",*]", false},
        {"[,*]", false},
        {"[*]", false},
        {"[64,*", false},
        {"[64,128*]", false},
        {"[64,*]", true},
        {"[64,*)", true},
      };
  }


  /**
   * @param  interval  to check
   * @param  valid  whether the supplied interval is valid
   */
  @Test(groups = "passtest", dataProvider = "intervals")
  public void checkInterval(final String interval, final boolean valid)
  {
    try {
      new LengthComplexityRule(new LengthComplexityRule.Entry(interval, new RepeatCharacterRegexRule()));
      if (!valid) {
        fail("Should have thrown IllegalArgumentException");
      }
    } catch (Exception e) {
      if (valid) {
        fail("Should not have thrown exception: %s", e.getMessage());
      }
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
  }


  /**
   * Test consistency.
   */
  @Test(groups = "passtest")
  public void checkConsistency()
  {
    try {
      // intersecting rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("(5,11)", new WhitespaceRule()));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    try {
      // intersecting rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("(5,15]", new WhitespaceRule()));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    try {
      // intersecting rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("[15,25)", new WhitespaceRule()));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    try {
      // intersecting rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("(19,25)", new WhitespaceRule()));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    try {
      // null rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("(0,1)", (Rule[]) null));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }
    try {
      // empty rules
      new LengthComplexityRule(
        new LengthComplexityRule.Entry("[10,20]", new RepeatCharacterRegexRule()),
        new LengthComplexityRule.Entry("(0,1)", new ArrayList<>()));
      fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
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
          new PasswordData("bwayne", "r%vE2"),
          new String[] {
            String.format("Password must be %s or more characters in length.", 8),
            String.format("Password meets %s complexity rules, but %s are required.", 6, 7),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", "It was the best of times, it was the worst of times, it was the age of wisdom,"),
          new String[] {
            String.format("Password must be no more than %s characters in length.", 64),
            String.format("Password meets %s complexity rules, but %s are required.", 5, 6),
          },
        },
        {
          rule1,
          new PasswordData(
            "bwayne",
            "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of " +
              "foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light,"),
          new String[] {
            String.format("No rules have been configured for a password of length %s.", 198),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", "rpscvEW2e"),
          new String[] {
            String.format("Password must contain %s or more special characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 3, 4, 4),
            String.format("Password meets %s complexity rules, but %s are required.", 6, 7),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", "rkscvEWteNTC"),
          new String[] {
            String.format("Password must contain %s or more digit characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 2, 3, 3),
            String.format("Password meets %s complexity rules, but %s are required.", 6, 7),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", "rkscvewbepwcouovqt"),
          new String[] {
            String.format("Password must contain %s or more uppercase characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 1, 2, 2),
            String.format("Password meets %s complexity rules, but %s are required.", 6, 7),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", "horse defghj battery"),
          new String[] {
            String.format("Password contains the illegal alphabetical sequence '%s'.", "defgh"),
            String.format("Password meets %s complexity rules, but %s are required.", 5, 6),
          },
        },
        {
          rule2,
          new PasswordData("bwayne", "horse defghj battery"),
          new String[] {
            String.format("Password contains the illegal alphabetical sequence '%s'.", "defgh"),
          },
        },
      };
  }
}

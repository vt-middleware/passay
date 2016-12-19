/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link LengthComplexityRule}.
 *
 * @author  Middleware Services
 */
public class LengthComplexityRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String INVALID_PASS_5 = "r%vE2";

  /** Test password. */
  private static final String VALID_PASS_9 = "r%scvEW2e";

  /** Test password. */
  private static final String VALID_PASS_12 = "rkscvEW2e93C";

  /** Test password. */
  private static final String VALID_PASS_18 = "rkscvEWbePwCOUovqt";

  /** Test password. */
  private static final String VALID_PASS_20 = "horse staple battery";

  /** Test password. */
  private static final String VALID_PASS_24 = "it was the best of times";

  /** Test password. */
  private static final String INVALID_PASS_78 =
    "It was the best of times, it was the worst of times, it was the age of wisdom,";

  /** Test password. */
  private static final String INVALID_PASS_198 =
    "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, " +
    "it was the epoch of belief, it was the epoch of incredulity, it was the season of Light,";

  /** For testing. */
  private final LengthComplexityRule rule1 = new LengthComplexityRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    rule1.addRules(
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
      new RepeatCharacterRegexRule());

    rule1.addRules(
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
      new RepeatCharacterRegexRule());

    rule1.addRules(
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
      new RepeatCharacterRegexRule());

    rule1.addRules(
      "[20,128]",
      new LengthRule(8, 64),
      new UsernameRule(true, true),
      new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
      new IllegalSequenceRule(EnglishSequenceData.Numerical),
      new IllegalSequenceRule(EnglishSequenceData.USQwerty),
      new RepeatCharacterRegexRule());
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

        {rule1, new PasswordData("alfred", VALID_PASS_9), null, },
        {rule1, new PasswordData("alfred", VALID_PASS_12), null, },
        {rule1, new PasswordData("alfred", VALID_PASS_18), null, },
        {rule1, new PasswordData("alfred", VALID_PASS_20), null, },
        {rule1, new PasswordData("alfred", VALID_PASS_24), null, },
        {
          rule1,
          new PasswordData("alfred", INVALID_PASS_5),
          codes(
            LengthComplexityRule.ERROR_CODE,
            LengthRule.ERROR_CODE_MIN),
        },
        {
          rule1,
          new PasswordData("alfred", INVALID_PASS_78),
          codes(
            LengthComplexityRule.ERROR_CODE,
            LengthRule.ERROR_CODE_MAX),
        },
        {
          rule1,
          new PasswordData("alfred", INVALID_PASS_198),
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
      };
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "intervals")
  public Object[][] intervals()
    throws Exception
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
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"passtest"}, dataProvider = "intervals")
  public void checkInterval(final String interval, final boolean valid)
    throws Exception
  {
    final LengthComplexityRule lcr = new LengthComplexityRule();
    try {
      lcr.addRules(interval, new RepeatCharacterRegexRule());
      if (!valid) {
        AssertJUnit.fail("Should have thrown IllegalArgumentException");
      }
    } catch (Exception e) {
      if (valid) {
        AssertJUnit.fail("Should not have thrown exception: " + e);
      }
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void checkConsistency()
    throws Exception
  {
    // no rules configured
    final LengthComplexityRule lcr = new LengthComplexityRule();
    lcr.addRules("[10,20]", new RepeatCharacterRegexRule());
    try {
      // intersecting rules
      lcr.addRules("(5,11)", new WhitespaceRule());
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
    try {
      // intersecting rules
      lcr.addRules("(5,15]", new WhitespaceRule());
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
    try {
      // intersecting rules
      lcr.addRules("[15,25)", new WhitespaceRule());
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
    try {
      // intersecting rules
      lcr.addRules("(19,25)", new WhitespaceRule());
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
    try {
      // null rules
      lcr.addRules("(0,1)", (Rule[]) null);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }
    try {
      // empty rules
      lcr.addRules("(0,1)", new ArrayList<>());
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Exception e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
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
          new PasswordData("bwayne", INVALID_PASS_5),
          new String[] {
            String.format("Password must be %s or more characters in length.", 8),
            String.format("Password meets %s complexity rules, but %s are required.", 6, 7),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", INVALID_PASS_78),
          new String[] {
            String.format("Password must be no more than %s characters in length.", 64),
            String.format("Password meets %s complexity rules, but %s are required.", 5, 6),
          },
        },
        {
          rule1,
          new PasswordData("bwayne", INVALID_PASS_198),
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
      };
  }
}

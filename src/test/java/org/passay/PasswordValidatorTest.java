/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PasswordValidator}.
 *
 * @author  Middleware Services
 */
public class PasswordValidatorTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "aBcD3FgH1Jk";

  /** Test password. */
  private static final String INVALID_PASS = "aBcDeFgHiJk";

  /** For testing. */
  private static final String USER = "testuser";

  /** For testing. */
  private final List<PasswordData.Reference> references = new ArrayList<>();

  /** Test checker. */
  private final List<Rule> rules = new ArrayList<>();

  /** Word list. */
  private Dictionary dict;

  /** For testing. */
  private PasswordValidator validator;


  /**
   * @param  dictFile  to load.
   *
   * @throws  Exception  On test failure.
   */
  @Parameters("dictionaryFile")
  @BeforeClass(groups = {"passtest"})
  public void createDictionary(final String dictFile)
    throws Exception
  {
    final ArrayWordList awl = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      false,
      new ArraysSort());
    dict = new WordListDictionary(awl);
    validator = new PasswordValidator(rules);
  }


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"passtest"}, dependsOnMethods = {"createDictionary"})
  public void createChecker()
    throws Exception
  {
    final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
    charRule.setNumberOfCharacteristics(3);

    final WhitespaceRule whitespaceRule = new WhitespaceRule();

    final LengthRule lengthRule = new LengthRule(8, 16);

    final DictionarySubstringRule dictRule = new DictionarySubstringRule(dict);
    dictRule.setMatchBackwards(true);

    final IllegalSequenceRule qwertySeqRule = new IllegalSequenceRule(EnglishSequenceData.US_QWERTY);

    final IllegalSequenceRule alphaSeqRule = new IllegalSequenceRule(EnglishSequenceData.Alphabetical);

    final IllegalSequenceRule numSeqRule = new IllegalSequenceRule(EnglishSequenceData.Numerical);

    final RepeatCharacterRegexRule dupSeqRule = new RepeatCharacterRegexRule();

    final UsernameRule userIDRule = new UsernameRule();
    userIDRule.setIgnoreCase(true);
    userIDRule.setMatchBackwards(true);

    final EncodingHashBean sha1Bean = new EncodingHashBean();
    sha1Bean.setDigestSpec(new DigestSpec("SHA1"));
    sha1Bean.setCodecSpec(new CodecSpec("Base64"));

    final DigestHistoryRule historyRule = new DigestHistoryRule(sha1Bean);
    references.add(new PasswordData.HistoricalReference("history", "safx/LW8+SsSy/o3PmCNy4VEm5s="));
    references.add(new PasswordData.HistoricalReference("history", "zurb9DyQ5nooY1la8h86Bh0n1iw="));
    references.add(new PasswordData.HistoricalReference("history", "bhqabXwE3S8E6xNJfX/d76MFOCs="));

    final DigestSourceRule sourceRule = new DigestSourceRule(sha1Bean);
    references.add(new PasswordData.SourceReference("source", "CJGTDMQRP+rmHApkcijC80aDV0o="));

    rules.add(charRule);
    rules.add(whitespaceRule);
    rules.add(lengthRule);
    rules.add(dictRule);
    rules.add(qwertySeqRule);
    rules.add(alphaSeqRule);
    rules.add(numSeqRule);
    rules.add(dupSeqRule);
    rules.add(userIDRule);
    rules.add(historyRule);
    rules.add(sourceRule);
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void validate()
    throws Exception
  {
    final List<Rule> l = new ArrayList<>();
    final PasswordValidator pv = new PasswordValidator(l);

    l.add(new LengthRule(8, 16));

    final CharacterCharacteristicsRule ccRule = new CharacterCharacteristicsRule();
    ccRule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
    ccRule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    ccRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    ccRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
    ccRule.setNumberOfCharacteristics(3);
    l.add(ccRule);

    l.add(new IllegalSequenceRule(EnglishSequenceData.US_QWERTY));
    l.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical));
    l.add(new IllegalSequenceRule(EnglishSequenceData.Numerical));
    l.add(new RepeatCharacterRegexRule());

    final RuleResult resultPass = pv.validate(new PasswordData(VALID_PASS));
    AssertJUnit.assertTrue(resultPass.isValid());
    AssertJUnit.assertTrue(pv.getMessages(resultPass).size() == 0);

    final RuleResult resultFail = pv.validate(new PasswordData(INVALID_PASS));
    AssertJUnit.assertFalse(resultFail.isValid());
    AssertJUnit.assertTrue(pv.getMessages(resultFail).size() > 0);

    l.add(new UsernameRule(true, true));

    try {
      pv.validate(new PasswordData(VALID_PASS));
      AssertJUnit.fail("Should have thrown NullPointerException");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(e.getClass(), NullPointerException.class);
    } catch (Exception e) {
      AssertJUnit.fail("Should have thrown NullPointerException, threw " + e.getMessage());
    }

    final PasswordData valid = new PasswordData(VALID_PASS);
    valid.setUsername(USER);
    AssertJUnit.assertTrue(pv.validate(valid).isValid());

    final PasswordData invalid = new PasswordData(INVALID_PASS);
    invalid.setUsername(USER);
    AssertJUnit.assertFalse(pv.validate(invalid).isValid());
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  // CheckStyle:MethodLengthCheck OFF
  @DataProvider(name = "passwords")
  public Object[][] passwords()
    throws Exception
  {
    return
      new Object[][] {

        /** invalid character rule passwords. */

        /** all digits */
        {
          validator,
          PasswordData.newInstance("4326789032", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            EnglishSequenceData.US_QWERTY.getErrorCode()),
        },

        /** all non-alphanumeric */
        {
          validator,
          PasswordData.newInstance("$&!$#@*{{>", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /** all lowercase */
        {
          validator,
          PasswordData.newInstance("aycdopezss", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            DictionarySubstringRule.ERROR_CODE),
        },

        /** all uppercase */
        {
          validator,
          PasswordData.newInstance("AYCDOPEZSS", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            DictionarySubstringRule.ERROR_CODE),
        },

        /** digits and non-alphanumeric */
        {
          validator,
          PasswordData.newInstance("@&3*(%5{}^", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /** digits and lowercase */
        {
          validator,
          PasswordData.newInstance("ay3dop5zss", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /** digits and uppercase */
        {
          validator,
          PasswordData.newInstance("AY3DOP5ZSS", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /** non-alphanumeric and lowercase */
        {
          validator,
          PasswordData.newInstance("a&c*o%ea}s", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode()),
        },

        /** non-alphanumeric and uppercase */
        {
          validator,
          PasswordData.newInstance("A&C*O%EA}S", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /** uppercase and lowercase */
        {
          validator,
          PasswordData.newInstance("AycDOPdsyz", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /** invalid whitespace rule passwords. */

        /** contains a space */
        {
          validator,
          PasswordData.newInstance("AycD Pdsyz", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            WhitespaceRule.ERROR_CODE),
        },

        /** contains a tab */
        {
          validator,
          PasswordData.newInstance("AycD    Psyz", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            WhitespaceRule.ERROR_CODE),
        },

        /** invalid length rule passwords. */

        /** too short */
        {
          validator,
          PasswordData.newInstance("p4T3t#", USER, references),
          codes(LengthRule.ERROR_CODE_MIN),
        },

        /** too long */
        {
          validator,
          PasswordData.newInstance("p4t3t#n6574632vbad#@!8", USER, references),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        /** invalid dictionary rule passwords. */

        /** matches dictionary word 'none' */
        {
          validator,
          PasswordData.newInstance("p4t3t#none", USER, references),
          codes(DictionaryRule.ERROR_CODE),
        },

        /** matches dictionary word 'none' backwards */
        {
          validator,
          PasswordData.newInstance("p4t3t#enon", USER, references),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },

        /** invalid sequence rule passwords. */

        /** matches sequence 'zxcvb' */
        {
          validator,
          PasswordData.newInstance("p4zxcvb#n65", USER, references),
          codes(EnglishSequenceData.US_QWERTY.getErrorCode()),
        },

        /**
         * matches sequence 'werty' backwards
         * 'wert' is a dictionary word
         */
        {
          validator,
          PasswordData.newInstance("p4ytrew#n65", USER, references),
          codes(EnglishSequenceData.US_QWERTY.getErrorCode(), DictionaryRule.ERROR_CODE_REVERSED),
        },

        /** matches sequence 'iop[]' ignore case */
        {
          validator,
          PasswordData.newInstance("p4iOP[]#n65", USER, references),
          codes(EnglishSequenceData.US_QWERTY.getErrorCode()),
        },

        /** invalid userid rule passwords. */

        /**
         * contains userid 'testuser'
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          PasswordData.newInstance("p4testuser#n65", USER, references),
          codes(UsernameRule.ERROR_CODE, DictionaryRule.ERROR_CODE),
        },

        /**
         * contains userid 'testuser' backwards
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          PasswordData.newInstance("p4resutset#n65", USER, references),
          codes(UsernameRule.ERROR_CODE_REVERSED, DictionaryRule.ERROR_CODE_REVERSED),
        },

        /**
         * contains userid 'testuser' ignore case
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          PasswordData.newInstance("p4TeStusEr#n65", USER, references),
          codes(UsernameRule.ERROR_CODE, DictionaryRule.ERROR_CODE),
        },

        /** invalid history rule passwords. */

        /** contains history password */
        {
          validator,
          PasswordData.newInstance("t3stUs3r02", USER, references),
          codes(HistoryRule.ERROR_CODE),
        },

        /** contains history password */
        {
          validator,
          PasswordData.newInstance("t3stUs3r03", USER, references),
          codes(HistoryRule.ERROR_CODE),
        },

        /** contains source password */
        {
          validator,
          PasswordData.newInstance("t3stUs3r04", USER, references),
          codes(SourceRule.ERROR_CODE),
        },

        /** valid passwords. */

        /** digits, non-alphanumeric, lowercase, uppercase */
        {
          validator,
          PasswordData.newInstance("p4T3t#N65", USER, references),
          null,
        },

        /** digits, non-alphanumeric, lowercase */
        {
          validator,
          PasswordData.newInstance("p4t3t#n65", USER, references),
          null,
        },

        /** digits, non-alphanumeric, uppercase */
        {
          validator,
          PasswordData.newInstance("P4T3T#N65", USER, references),
          null,
        },

        /** digits, uppercase, lowercase */
        {
          validator,
          PasswordData.newInstance("p4t3tCn65", USER, references),
          null,
        },

        /** non-alphanumeric, lowercase, uppercase */
        {
          validator,
          PasswordData.newInstance("pxT%t#Nwq", USER, references),
          null,
        },

        // Issue 135
        {
          validator,
          PasswordData.newInstance("1234567", USER, references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Special.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            LengthRule.ERROR_CODE_MIN),
        },
      };
  }
  // CheckStyle:MethodLengthCheck ON


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
          validator,
          PasswordData.newInstance("ay3dop5zss", USER, references),
          new String[] {
            String.format("Password must contain at least %s special characters.", 1),
            String.format("Password must contain at least %s uppercase characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 2, 4, 3),
          },
        },
      };
  }
}

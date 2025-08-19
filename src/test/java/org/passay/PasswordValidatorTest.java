/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.PasswordData.HistoricalReference;
import org.passay.PasswordData.SourceReference;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.passay.entropy.RandomPasswordEntropy;
import org.passay.resolver.MessageResolver;
import org.passay.resolver.PropertiesMessageResolver;
import org.passay.resolver.ResourceBundleMessageResolver;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.DictionaryRule;
import org.passay.rule.DictionarySubstringRule;
import org.passay.rule.DigestHistoryRule;
import org.passay.rule.DigestSourceRule;
import org.passay.rule.HistoryRule;
import org.passay.rule.IllegalSequenceRule;
import org.passay.rule.LengthRule;
import org.passay.rule.RepeatCharacterRegexRule;
import org.passay.rule.Rule;
import org.passay.rule.SourceRule;
import org.passay.rule.UsernameRule;
import org.passay.rule.WhitespaceRule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link PasswordValidator}.
 *
 * @author  Middleware Services
 */
public class PasswordValidatorTest
{

  /** test message resolver. */
  protected static final MessageResolver TEST_RESOLVER = new ResourceBundleMessageResolver(
    ResourceBundle.getBundle("passay-test"));

  /** empty message resolver. */
  protected static final MessageResolver EMPTY_RESOLVER = new PropertiesMessageResolver(new Properties());

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
  @BeforeClass(groups = "passtest")
  public void createDictionary(final String dictFile) throws Exception
  {
    final ArrayWordList awl = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      false,
      new ArraysSort());
    dict = new WordListDictionary(awl);
  }

  /**
   * Setup test resources.
   */
  @BeforeClass(groups = "passtest", dependsOnMethods = "createDictionary")
  public void createChecker()
  {
    final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule(
      3,
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1));

    final WhitespaceRule whitespaceRule = new WhitespaceRule();

    final LengthRule lengthRule = new LengthRule(8, 16);

    final DictionarySubstringRule dictRule = new DictionarySubstringRule(dict, true);

    final IllegalSequenceRule qwertySeqRule = new IllegalSequenceRule(EnglishSequenceData.USQwerty);

    final IllegalSequenceRule alphaSeqRule = new IllegalSequenceRule(EnglishSequenceData.Alphabetical);

    final IllegalSequenceRule numSeqRule = new IllegalSequenceRule(EnglishSequenceData.Numerical);

    final RepeatCharacterRegexRule dupSeqRule = new RepeatCharacterRegexRule();

    final UsernameRule userIDRule = new UsernameRule(true, true);

    final EncodingHashBean sha1Bean = new EncodingHashBean();
    sha1Bean.setDigestSpec(new DigestSpec("SHA1"));
    sha1Bean.setCodecSpec(new CodecSpec("Base64"));

    final DigestHistoryRule historyRule = new DigestHistoryRule(sha1Bean);
    references.add(new HistoricalReference("history", "safx/LW8+SsSy/o3PmCNy4VEm5s="));
    references.add(new HistoricalReference("history", "zurb9DyQ5nooY1la8h86Bh0n1iw="));
    references.add(new HistoricalReference("history", "bhqabXwE3S8E6xNJfX/d76MFOCs="));

    final DigestSourceRule sourceRule = new DigestSourceRule(sha1Bean);
    references.add(new SourceReference("source", "CJGTDMQRP+rmHApkcijC80aDV0o="));

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
    validator = new DefaultPasswordValidator(DefaultPasswordValidator.DEFAULT_ENTROPY_PROVIDER, rules);
  }

  /**
   * Test estimate entropy.
   */
  @Test(groups = "passtest")
  public void estimateEntropy()
  {
    /*
     * NIST: Table A1 from http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf
     * ___________________________
     * Len: Plain  Dict  Dict+Comp
     * 1  : 4.0    4.0    4.0
     * 2  : 6.0    6.0    6.0
     * 3  : 8.0    8.0    8.0
     * 4  : 10.0   14.0   16.0
     * 5  : 12.0   17.0   20.0
     * 6  : 14.0   20.0   23.0
     * 7  : 16.0   22.0   27.0
     * 8  : 18.0   24.0   30.0
     * 9  : 19.5   24.5   30.5
     * 10 : 21.0   26.0   32.0
     * 11 : 22.5   26.5   32.5
     * 12 : 24.0   28.0   34.0
     * 13 : 25.5   28.5   34.5
     * 14 : 27.0   30.0   36.0
     * 15 : 28.5   30.5   36.5
     * 16 : 30.0   32.0   38.0
     * 17 : 31.5   32.5   38.5
     * 18 : 33.0   34.0   40.0
     * 19 : 34.5   34.5   40.5
     * 20 : 36.0   36.0   42.0
     * 21 : 37.0   37.0   43.0
     * 22 : 38.0   38.0   44.0
     */

    final PasswordData length5AllLowercasePassword = new PasswordData("hello");
    PasswordData length5CompositionPassword = new PasswordData("h3L!o");

    final PasswordData length10AllLowercasePassword = new PasswordData("hellohello");
    PasswordData length10CompositionPassword = new PasswordData("h3!Lohell0");

    final PasswordData length22AllLowercasePassword = new PasswordData("hellohellohellohellooo");
    PasswordData length22CompositionPassword = new PasswordData("he1loHellohellohello!!");

    //Test for no character based rules.
    final List<Rule> l = new ArrayList<>();
    final DefaultPasswordValidator pv = new DefaultPasswordValidator(
      DefaultPasswordValidator.DEFAULT_ENTROPY_PROVIDER, l);

    try {
      final ValidationResult result = pv.validate(new PasswordData("heLlo", PasswordData.Origin.Generated));
      result.getEntropy();
      fail("Should have thrown IllegalArgumentException");
    } catch (Throwable e) {
      assertThat(e).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    //User Password Origin Tests:

    //Length 5
    assertThat(pv.validate(length5AllLowercasePassword).getEntropy()).isEqualTo(12.0);
    //Length 5 + Composition
    assertThat(pv.validate(length5CompositionPassword).getEntropy()).isEqualTo(15.0);
    //Length 10
    assertThat(pv.validate(length10AllLowercasePassword).getEntropy()).isEqualTo(21.0);
    //Length 10 + Composition
    assertThat(pv.validate(length10CompositionPassword).getEntropy()).isEqualTo(27.0);
    //Length 22
    assertThat(pv.validate(length22AllLowercasePassword).getEntropy()).isEqualTo(38.0);
    //Length 22 + Composition
    assertThat(pv.validate(length22CompositionPassword).getEntropy()).isEqualTo(44.0);

    //Empty dictionary check
    l.add(new DictionarySubstringRule(new WordListDictionary(new ArrayWordList(new String[]{}))));

    //Test Length 10 + Composition + Empty Dictionary
    assertThat(pv.validate(length10CompositionPassword).getEntropy()).isEqualTo(27.0);
    //Test Length 10 + Empty Dictionary
    assertThat(pv.validate(length10AllLowercasePassword).getEntropy()).isEqualTo(21.0);

    //Test Length 5 + Composition + Dictionary
    assertThat(validator.validate(length5CompositionPassword).getEntropy()).isEqualTo(20.0);
    //Test Length 10 + Composition + Dictionary
    assertThat(validator.validate(length10CompositionPassword).getEntropy()).isEqualTo(32.0);
    //Test Length 22 + Composition + Dictionary
    assertThat(validator.validate(length22CompositionPassword).getEntropy()).isEqualTo(44.0);

    //Generated Password Origin Tests:

    //182 total unique characters from given CharacterRules in validator
    length5CompositionPassword = new PasswordData("h3L!o", PasswordData.Origin.Generated);
    length10CompositionPassword = new PasswordData("h3!Lohell0", PasswordData.Origin.Generated);
    length22CompositionPassword = new PasswordData("he1loHellohellohello!!", PasswordData.Origin.Generated);

    //Random generated password test log2(b^l):
    assertThat(validator.validate(length5CompositionPassword).getEntropy())
      .isEqualTo(new RandomPasswordEntropy(182, 5).estimate());
    assertThat(validator.validate(length10CompositionPassword).getEntropy())
      .isEqualTo(new RandomPasswordEntropy(182, 10).estimate());
    assertThat(validator.validate(length22CompositionPassword).getEntropy())
      .isEqualTo(new RandomPasswordEntropy(182, 22).estimate());

    //Random generated password test with AllowedCharacterRule
    final List<Rule> al = new ArrayList<>();
    final AllowedCharacterRule allowedRule = new AllowedCharacterRule(new UnicodeString(
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'L', '0', '!'));
    al.add(allowedRule);
    final DefaultPasswordValidator pvAl = new DefaultPasswordValidator(
      DefaultPasswordValidator.DEFAULT_ENTROPY_PROVIDER, al);
    assertThat(pvAl.validate(length5CompositionPassword).getEntropy())
      .isEqualTo(
        new RandomPasswordEntropy(
          allowedRule.getAllowedCharacters().length(), length5CompositionPassword.getCharacterCount()).estimate());
  }

  /**
   * Test validation.
   */
  @Test(groups = "passtest")
  public void validate()
  {
    final List<Rule> l = new ArrayList<>();
    l.add(new LengthRule(8, 16));

    final CharacterCharacteristicsRule ccRule = new CharacterCharacteristicsRule(
      3,
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1),
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1));
    l.add(ccRule);

    l.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty));
    l.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical));
    l.add(new IllegalSequenceRule(EnglishSequenceData.Numerical));
    l.add(new RepeatCharacterRegexRule());

    final DefaultPasswordValidator pv = new DefaultPasswordValidator(new PropertiesMessageResolver(), l);
    final ValidationResult resultPass = pv.validate(new PasswordData(VALID_PASS));
    assertThat(resultPass.isValid()).isTrue();
    assertThat(resultPass.getMessages().isEmpty()).isTrue();

    final ValidationResult resultFail = pv.validate(new PasswordData(INVALID_PASS));
    assertThat(resultFail.isValid()).isFalse();
    assertThat(resultFail.getMessages().size()).isGreaterThan(0);

    l.add(new UsernameRule(true, true));

    assertThat(pv.validate(new PasswordData(VALID_PASS)).isValid()).isTrue();
    assertThat(pv.validate(new PasswordData("", VALID_PASS)).isValid()).isTrue();

    final PasswordData valid = new PasswordData(USER, VALID_PASS);
    assertThat(pv.validate(valid).isValid()).isTrue();

    final PasswordData invalid = new PasswordData(USER, INVALID_PASS);
    assertThat(pv.validate(invalid).isValid()).isFalse();
  }


  /**
   * Test object construction.
   */
  @Test(groups = "passtest")
  public void constructor()
  {
    new DefaultPasswordValidator();
  }


  /**
   * @return  Test data.
   */
  // CheckStyle:MethodLengthCheck OFF
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        /* invalid character rule passwords. */

        /* all digits */
        {
          validator,
          new PasswordData(USER, "4326789032", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            EnglishSequenceData.USQwerty.getErrorCode()),
        },

        /* all non-alphanumeric */
        {
          validator,
          new PasswordData(USER, "$&!$#@*{{>", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /* all lowercase */
        {
          validator,
          new PasswordData(USER, "aycdopezss", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            DictionarySubstringRule.ERROR_CODE),
        },

        /* all uppercase */
        {
          validator,
          new PasswordData(USER, "AYCDOPEZSS", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            DictionarySubstringRule.ERROR_CODE),
        },

        /* digits and non-alphanumeric */
        {
          validator,
          new PasswordData(USER, "@&3*(%5{}^", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /* digits and lowercase */
        {
          validator,
          new PasswordData(USER, "ay3dop5zss", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /* digits and uppercase */
        {
          validator,
          new PasswordData(USER, "AY3DOP5ZSS", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /* non-alphanumeric and lowercase */
        {
          validator,
          new PasswordData(USER, "a&c*o%ea}s", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode()),
        },

        /* non-alphanumeric and uppercase */
        {
          validator,
          new PasswordData(USER, "A&C*O%EA}S", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode()),
        },

        /* uppercase and lowercase */
        {
          validator,
          new PasswordData(USER, "AycDOPdsyz", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode()),
        },

        /* invalid whitespace rule passwords. */

        /* contains a space */
        {
          validator,
          new PasswordData(USER, "AycD Pdsyz", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            WhitespaceRule.ERROR_CODE),
        },

        /* contains a tab */
        {
          validator,
          new PasswordData(USER, "AycD    Psyz", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Digit.getErrorCode(),
            EnglishCharacterData.Special.getErrorCode(),
            WhitespaceRule.ERROR_CODE),
        },

        /* invalid length rule passwords. */

        /* too short */
        {
          validator,
          new PasswordData(USER, "p4T3t#", references),
          codes(LengthRule.ERROR_CODE_MIN),
        },

        /* too long */
        {
          validator,
          new PasswordData(USER, "p4t3t#n6574632vbad#@!8", references),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        /* invalid dictionary rule passwords. */

        /* matches dictionary word 'none' */
        {
          validator,
          new PasswordData(USER, "p4t3t#none", references),
          codes(DictionaryRule.ERROR_CODE),
        },

        /* matches dictionary word 'none' backwards */
        {
          validator,
          new PasswordData(USER, "p4t3t#enon", references),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },

        /* invalid sequence rule passwords. */

        /* matches sequence 'zxcvb' */
        {
          validator,
          new PasswordData(USER, "p4zxcvb#n65", references),
          codes(EnglishSequenceData.USQwerty.getErrorCode()),
        },

        /*
         * matches sequence 'werty' backwards
         * 'wert' is a dictionary word
         */
        {
          validator,
          new PasswordData(USER, "p4ytrew#n65", references),
          codes(EnglishSequenceData.USQwerty.getErrorCode(), DictionaryRule.ERROR_CODE_REVERSED),
        },

        /* matches sequence 'iop[]' ignore case */
        {
          validator,
          new PasswordData(USER, "p4iOP[]#n65", references),
          codes(EnglishSequenceData.USQwerty.getErrorCode()),
        },

        /* invalid userid rule passwords. */

        /*
         * contains userid 'testuser'
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          new PasswordData(USER, "p4testuser#n65", references),
          codes(UsernameRule.ERROR_CODE, DictionaryRule.ERROR_CODE),
        },

        /*
         * contains userid 'testuser' backwards
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          new PasswordData(USER, "p4resutset#n65", references),
          codes(UsernameRule.ERROR_CODE_REVERSED, DictionaryRule.ERROR_CODE_REVERSED),
        },

        /*
         * contains userid 'testuser' ignore case
         * 'test' and 'user' are dictionary words
         */
        {
          validator,
          new PasswordData(USER, "p4TeStusEr#n65", references),
          codes(UsernameRule.ERROR_CODE, DictionaryRule.ERROR_CODE),
        },

        /* invalid history rule passwords. */

        /* contains history password */
        {
          validator,
          new PasswordData(USER, "t3stUs3r02", references),
          codes(HistoryRule.ERROR_CODE),
        },

        /* contains history password */
        {
          validator,
          new PasswordData(USER, "t3stUs3r03", references),
          codes(HistoryRule.ERROR_CODE),
        },

        /* contains source password */
        {
          validator,
          new PasswordData(USER, "t3stUs3r04", references),
          codes(SourceRule.ERROR_CODE),
        },

        /* valid passwords. */

        /* digits, non-alphanumeric, lowercase, uppercase */
        {
          validator,
          new PasswordData(USER, "p4T3t#N65", references),
          null,
        },

        /* digits, non-alphanumeric, lowercase */
        {
          validator,
          new PasswordData(USER, "p4t3t#n65", references),
          null,
        },

        /* digits, non-alphanumeric, uppercase */
        {
          validator,
          new PasswordData(USER, "P4T3T#N65", references),
          null,
        },

        /* digits, uppercase, lowercase */
        {
          validator,
          new PasswordData(USER, "p4t3tCn65", references),
          null,
        },

        /* non-alphanumeric, lowercase, uppercase */
        {
          validator,
          new PasswordData(USER, "pxT%t#Nwq", references),
          null,
        },

        // Issue 135
        {
          validator,
          new PasswordData(USER, "1234567", references),
          codes(
            CharacterCharacteristicsRule.ERROR_CODE,
            EnglishCharacterData.Special.getErrorCode(),
            EnglishCharacterData.LowerCase.getErrorCode(),
            EnglishCharacterData.UpperCase.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            LengthRule.ERROR_CODE_MIN),
        },
      };
  }
  // CheckStyle:MethodLengthCheck ON


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          validator,
          new PasswordData(USER, "ay3dop5zss", references),
          new String[] {
            String.format("Password must contain %s or more special characters.", 1),
            String.format("Password must contain %s or more uppercase characters.", 1),
            String.format("Password matches %s of %s character rules, but %s are required.", 2, 4, 3),
          },
        },
      };
  }


  /**
   * @param  passwordValidator  to check password with
   * @param  passwordData  to check
   * @param  errorCodes  Array of error codes to be produced on a failed password validation attempt. A null value
   *                     indicates that password validation should succeed.
   */
  @Test(groups = "passtest", dataProvider = "passwords")
  public void checkPassword(
    final PasswordValidator passwordValidator, final PasswordData passwordData, final String[] errorCodes)
  {
    final ValidationResult result = passwordValidator.validate(passwordData);
    if (errorCodes != null) {
      assertThat(result.isValid()).isFalse();
      assertThat(result.getDetails().size()).isEqualTo(errorCodes.length);
      for (String code : errorCodes) {
        assertThat(hasErrorCode(code, result)).isTrue();
      }
    } else {
      assertThat(result.isValid()).isTrue();
    }
  }


  /**
   * @param  passwordValidator  to check password with
   * @param  passwordData  to check
   * @param  messages  Array of messages to be produced on a failed password validation attempt
   */
  @Test(groups = "passtest", dataProvider = "messages")
  public void checkMessage(
    final PasswordValidator passwordValidator, final PasswordData passwordData, final String[] messages)
  {
    final ValidationResult result = passwordValidator.validate(passwordData);
    assertThat(result.isValid()).isFalse();
    assertThat(result.getDetails().size()).isEqualTo(messages.length);
    for (int i = 0; i < result.getDetails().size(); i++) {
      final RuleResultDetail detail = result.getDetails().get(i);
      assertThat(TEST_RESOLVER.resolve(detail)).isEqualTo(messages[i]);
      assertThat(EMPTY_RESOLVER.resolve(detail)).isNotNull();
    }
  }


  /**
   * Test producer extends.
   */
  @Test(groups = "passtest")
  public void producerExtends()
  {
    // test that password validator will accept any list of rules that extends Rule
    final List<CharacterRule> l = new ArrayList<>();
    l.add(new CharacterRule(EnglishCharacterData.LowerCase));
    l.add(new CharacterRule(EnglishCharacterData.UpperCase));
    new DefaultPasswordValidator(l);
    new DefaultPasswordValidator(
      new CharacterRule(EnglishCharacterData.LowerCase), new CharacterRule(EnglishCharacterData.UpperCase));
  }


  /**
   * Converts one or more error codes to a string array.
   *
   * @param  codes  One or more error codes.
   *
   * @return  Array of error codes.
   */
  protected static String[] codes(final String... codes)
  {
    return codes;
  }


  /**
   * Determines whether the given error code is found among the details of the give rule validation result.
   *
   * @param  code  to search for in result details.
   * @param  result  to search for given code.
   *
   * @return  True if code is found among result details, false otherwise.
   */
  protected static boolean hasErrorCode(final String code, final ValidationResult result)
  {
    for (RuleResultDetail detail : result.getDetails()) {
      if (code.equals(detail.getErrorCode())) {
        return true;
      }
    }
    return false;
  }
}

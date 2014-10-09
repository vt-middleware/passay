/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DictionaryRule}.
 *
 * @author  Middleware Services
 */
public class DictionaryRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "Pullm@n1z3";

  /** Test password. */
  private static final String DICT_PASS = "Pullmanize";

  /** Test password. */
  private static final String BACKWARDS_DICT_PASS =  "ezinamlluP";

  /** Test password. */
  private static final String UPPERCASE_DICT_PASS = "PuLLmanIZE";

  /** Test password. */
  private static final String BACKWARDS_UPPERCASE_DICT_PASS =  "EZInamLLuP";

  /** Test password. */
  private static final String SINGLE_LETTER_DICT_PASS = "a";

  /** For testing. */
  private final DictionaryRule rule = new DictionaryRule();

  /** For testing. */
  private final DictionaryRule backwardsRule = new DictionaryRule();

  /** For testing. */
  private final DictionaryRule ignoreCaseRule = new DictionaryRule();

  /** For testing. */
  private final DictionaryRule allRule = new DictionaryRule();


  /**
   * Initialize rules for this test.
   *
   * @param  dictFile  dictionary file to read
   *
   * @throws  Exception  if dictionary files cannot be read
   */
  @Parameters("dictionaryFile")
  @BeforeClass(groups = {"passtest"})
  public void createRules(final String dictFile)
    throws Exception
  {
    final ArrayWordList caseSensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      true,
      new ArraysSort());
    final WordListDictionary caseSensitiveDict = new WordListDictionary(
      caseSensitiveWordList);

    final ArrayWordList caseInsensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      false,
      new ArraysSort());
    final WordListDictionary caseInsensitiveDict = new WordListDictionary(
      caseInsensitiveWordList);

    rule.setDictionary(caseSensitiveDict);

    backwardsRule.setDictionary(caseSensitiveDict);
    backwardsRule.setMatchBackwards(true);

    ignoreCaseRule.setDictionary(caseInsensitiveDict);

    allRule.setDictionary(caseInsensitiveDict);
    allRule.setMatchBackwards(true);
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

        {rule, new PasswordData(VALID_PASS), null, },
        {
          rule,
          new PasswordData(DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {rule, new PasswordData(BACKWARDS_DICT_PASS), null, },
        {rule, new PasswordData(UPPERCASE_DICT_PASS), null, },
        {rule, new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS), null, },

        {backwardsRule, new PasswordData(VALID_PASS), null, },
        {
          backwardsRule,
          new PasswordData(DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {
          backwardsRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },
        {backwardsRule, new PasswordData(UPPERCASE_DICT_PASS), null, },
        {
          backwardsRule,
          new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS),
          null,
        },

        {ignoreCaseRule, new PasswordData(VALID_PASS), null, },
        {
          ignoreCaseRule,
          new PasswordData(DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {ignoreCaseRule, new PasswordData(BACKWARDS_DICT_PASS), null, },
        {
          ignoreCaseRule,
          new PasswordData(UPPERCASE_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {
          ignoreCaseRule,
          new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS),
          null,
        },

        {allRule, new PasswordData(VALID_PASS), null, },
        {
          allRule,
          new PasswordData(DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {
          allRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },
        {
          allRule,
          new PasswordData(UPPERCASE_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
        {
          allRule,
          new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },
        {
          allRule,
          new PasswordData(SINGLE_LETTER_DICT_PASS),
          codes(DictionaryRule.ERROR_CODE),
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    RuleResult result = rule.validate(new PasswordData(DICT_PASS));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format(
          "Password contains the dictionary word '%s'.",
          "Pullmanize"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }

    result = rule.validate(new PasswordData(BACKWARDS_DICT_PASS));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format(
          "Password contains the reversed dictionary word '%s'.",
          "Pullmanize"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

/**
 * Unit test for {@link DictionarySubstringRule}.
 *
 * @author  Middleware Services
 */
public class DictionarySubstringRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "p4t3t#7wd5gew";

  /** Test password. */
  private static final String DICT_PASS = "p4tlancely5gew";

  /** Test password. */
  private static final String BACKWARDS_DICT_PASS = "p4tylecnal5gew";

  /** Test password. */
  private static final String UPPERCASE_DICT_PASS = "p4tlAnCeLy5gew";

  /** Test password. */
  private static final String BACKWARDS_UPPERCASE_DICT_PASS = "p4tyLeCnAl5gew";

  /** For testing. */
  private final DictionarySubstringRule rule = new DictionarySubstringRule();

  /** For testing. */
  private final DictionarySubstringRule backwardsRule =
    new DictionarySubstringRule();

  /** For testing. */
  private final DictionarySubstringRule ignoreCaseRule =
    new DictionarySubstringRule();

  /** For testing. */
  private final DictionarySubstringRule allRule = new DictionarySubstringRule();


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
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        {rule, new PasswordData(BACKWARDS_DICT_PASS), null, },
        {rule, new PasswordData(UPPERCASE_DICT_PASS), null, },
        {rule, new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS), null, },

        {backwardsRule, new PasswordData(VALID_PASS), null, },
        {
          backwardsRule,
          new PasswordData(DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        {
          backwardsRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
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
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        {ignoreCaseRule, new PasswordData(BACKWARDS_DICT_PASS), null, },
        {
          ignoreCaseRule,
          new PasswordData(UPPERCASE_DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE),
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
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        {
          allRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
        },
        {
          allRule,
          new PasswordData(UPPERCASE_DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        {
          allRule,
          new PasswordData(BACKWARDS_UPPERCASE_DICT_PASS),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
        },
      };
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
          rule,
          new PasswordData(DICT_PASS),
          new String[] {
            String.format(
              "Password contains the dictionary word '%s'.",
              "lance"),
          },
        },
        {
          backwardsRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          new String[] {
            String.format(
              "Password contains the reversed dictionary word '%s'.",
              "lance"),
          },
        },
      };
  }
}

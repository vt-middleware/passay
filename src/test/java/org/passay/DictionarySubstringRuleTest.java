/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.FileReader;

import org.passay.logic.PasswordData;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.passay.rule.DictionarySubstringRule;
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

  /** For testing. */
  private final DictionarySubstringRule rule = new DictionarySubstringRule();

  /** For testing. */
  private final DictionarySubstringRule backwardsRule = new DictionarySubstringRule();

  /** For testing. */
  private final DictionarySubstringRule ignoreCaseRule = new DictionarySubstringRule();

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
  @BeforeClass(groups = "passtest")
  public void createRules(final String dictFile) throws Exception
  {
    final ArrayWordList caseSensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      true,
      new ArraysSort());
    final WordListDictionary caseSensitiveDict = new WordListDictionary(caseSensitiveWordList);

    final ArrayWordList caseInsensitiveWordList = WordLists.createFromReader(
      new FileReader[] {new FileReader(dictFile)},
      false,
      new ArraysSort());
    final WordListDictionary caseInsensitiveDict = new WordListDictionary(caseInsensitiveWordList);

    rule.setDictionary(caseSensitiveDict);

    backwardsRule.setDictionary(caseSensitiveDict);
    backwardsRule.setMatchBackwards(true);

    ignoreCaseRule.setDictionary(caseInsensitiveDict);

    allRule.setDictionary(caseInsensitiveDict);
    allRule.setMatchBackwards(true);
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // valid password
        {rule, new PasswordData("p4t3t#7wd5gew"), null, },
        // dictionary word
        {
          rule,
          new PasswordData("p4tlancely5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards dictionary word
        {rule, new PasswordData("p4tylecnal5gew"), null, },
        // mixed case dictionary word
        {rule, new PasswordData("p4tlAnCeLy5gew"), null, },
        // backwards mixed case dictionary word
        {rule, new PasswordData("p4tyLeCnAl5gew"), null, },

        // valid password
        {backwardsRule, new PasswordData("p4t3t#7wd5gew"), null, },
        // dictionary word
        {
          backwardsRule,
          new PasswordData("p4tlancely5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards dictionary word
        {
          backwardsRule,
          new PasswordData("p4tylecnal5gew"),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
        },
        // mixed case dictionary word
        {backwardsRule, new PasswordData("p4tlAnCeLy5gew"), null, },
        // backwards mixed case dictionary word
        {backwardsRule, new PasswordData("p4tyLeCnAl5gew"), null, },

        // valid password
        {ignoreCaseRule, new PasswordData("p4t3t#7wd5gew"), null, },
        // dictionary word
        {
          ignoreCaseRule,
          new PasswordData("p4tlancely5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards dictionary word
        {ignoreCaseRule, new PasswordData("p4tylecnal5gew"), null, },
        // mixed case dictionary word
        {
          ignoreCaseRule,
          new PasswordData("p4tlAnCeLy5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards mixed case dictionary word
        {ignoreCaseRule, new PasswordData("p4tyLeCnAl5gew"), null, },

        // valid password
        {allRule, new PasswordData("p4t3t#7wd5gew"), null, },
        // dictionary word
        {
          allRule,
          new PasswordData("p4tlancely5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards dictionary word
        {
          allRule,
          new PasswordData("p4tylecnal5gew"),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
        },
        // mixed case dictionary word
        {
          allRule,
          new PasswordData("p4tlAnCeLy5gew"),
          codes(DictionarySubstringRule.ERROR_CODE),
        },
        // backwards mixed case dictionary word
        {
          allRule,
          new PasswordData("p4tyLeCnAl5gew"),
          codes(DictionarySubstringRule.ERROR_CODE_REVERSED),
        },
      };
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
          rule,
          new PasswordData("p4tlancely5gew"),
          new String[] {String.format("Password contains the dictionary word '%s'.", "lance"), },
        },
        {
          backwardsRule,
          new PasswordData("p4tylecnal5gew"),
          new String[] {String.format("Password contains the reversed dictionary word '%s'.", "lance"), },
        },
      };
  }
}

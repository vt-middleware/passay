/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.passay.PasswordData;
import org.passay.dictionary.Dictionaries;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link DictionaryRule}.
 *
 * @author  Middleware Services
 */
public class DictionaryRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private DictionaryRule rule;

  /** For testing. */
  private DictionaryRule backwardsRule;

  /** For testing. */
  private DictionaryRule ignoreCaseRule;

  /** For testing. */
  private DictionaryRule allRule;


  /**
   * Initialize rules for this test.
   */
  @BeforeClass
  public void createRules() throws IOException
  {
    final Dictionary caseSensitiveDict = new WordListDictionary(
      WordLists.createFromReader(new Reader[] {new FileReader(Dictionaries.WEB_FILE_GT3)}, true, new ArraysSort()));
    final Dictionary caseInsensitiveDict = new WordListDictionary(
      WordLists.createFromReader(new Reader[] {new FileReader(Dictionaries.WEB_FILE_GT3)}, false, new ArraysSort()));
    rule = new DictionaryRule(caseSensitiveDict);
    backwardsRule = new DictionaryRule(caseSensitiveDict, true);
    ignoreCaseRule = new DictionaryRule(caseInsensitiveDict);
    allRule = new DictionaryRule(caseInsensitiveDict, true);
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
        {rule, new PasswordData("Pullm@n1z3"), null, },
        // dictionary word
        {
          rule,
          new PasswordData("Pullmanize"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backwards dictionary word
        {rule, new PasswordData("ezinamlluP"), null, },
        // mixed case dictionary word
        {rule, new PasswordData("PuLLmanIZE"), null, },
        // backwards mixed case dictionary word
        {rule, new PasswordData("EZInamLLuP"), null, },

        // valid password
        {backwardsRule, new PasswordData("Pullm@n1z3"), null, },
        // dictionary word
        {
          backwardsRule,
          new PasswordData("Pullmanize"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backward dictionary word
        {
          backwardsRule,
          new PasswordData("ezinamlluP"),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },
        // mixed case dictionary word
        {backwardsRule, new PasswordData("PuLLmanIZE"), null, },
        // backwards mixed case dictionary word
        {backwardsRule, new PasswordData("EZInamLLuP"), null, },

        // valid password
        {ignoreCaseRule, new PasswordData("Pullm@n1z3"), null, },
        // dictionary word
        {
          ignoreCaseRule,
          new PasswordData("Pullmanize"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backwards dictionary word
        {ignoreCaseRule, new PasswordData("ezinamlluP"), null, },
        // mixed case dictionary word
        {
          ignoreCaseRule,
          new PasswordData("PuLLmanIZE"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backwards mixed case dictionary word
        {ignoreCaseRule, new PasswordData("EZInamLLuP"), null, },

        // valid password
        {allRule, new PasswordData("Pullm@n1z3"), null, },
        // dictionary word
        {
          allRule,
          new PasswordData("Pullmanize"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backwards dictionary rule
        {
          allRule,
          new PasswordData("ezinamlluP"),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
        },
        // mixed case dictionary word
        {
          allRule,
          new PasswordData("PuLLmanIZE"),
          codes(DictionaryRule.ERROR_CODE),
        },
        // backwards mixed case dictionary word
        {
          allRule,
          new PasswordData("EZInamLLuP"),
          codes(DictionaryRule.ERROR_CODE_REVERSED),
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
          new PasswordData("Pullmanize"),
          new String[] {String.format("Password contains the dictionary word '%s'.", "Pullmanize"), },
        },
        {
          backwardsRule,
          new PasswordData("ezinamlluP"),
          new String[] {String.format("Password contains the reversed dictionary word '%s'.", "Pullmanize"), },
        },
      };
  }
}

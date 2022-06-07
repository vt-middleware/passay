/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

/**
 * Unit test for {@link DictionaryRule}.
 *
 * @author  Middleware Services
 */
public class DictionaryRuleTest extends AbstractRuleTest
{

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
   */
  @Parameters("dictionaryFile")
  @BeforeClass(groups = "passtest")
  public void createRules(final String dictFile)
  {
    final Dictionary caseSensitiveDict = new DictionaryBuilder().addFile(dictFile).setCaseSensitive(true).build();
    final Dictionary caseInsensitiveDict = new DictionaryBuilder().addFile(dictFile).build();

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

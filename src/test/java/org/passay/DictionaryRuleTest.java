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

  /** Test password. */
  private static final String VALID_PASS = "Pullm@n1z3";

  /** Test password. */
  private static final String DICT_PASS = "Pullmanize";

  /** Test password. */
  private static final String BACKWARDS_DICT_PASS = "ezinamlluP";

  /** Test password. */
  private static final String UPPERCASE_DICT_PASS = "PuLLmanIZE";

  /** Test password. */
  private static final String BACKWARDS_UPPERCASE_DICT_PASS = "EZInamLLuP";

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
    final Dictionary caseSensitiveDict = new DictionaryBuilder().addFile(
      dictFile).setCaseSensitive(true).build();
    final Dictionary caseInsensitiveDict = new DictionaryBuilder().addFile(
      dictFile).build();

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
              "Pullmanize"),
          },
        },
        {
          backwardsRule,
          new PasswordData(BACKWARDS_DICT_PASS),
          new String[] {
            String.format(
              "Password contains the reversed dictionary word '%s'.",
              "Pullmanize"),
          },
        },
      };
  }
}

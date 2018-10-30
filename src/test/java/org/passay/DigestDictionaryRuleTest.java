/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

/**
 * Unit test for {@link DigestDictionaryRule}.
 *
 * @author  Middleware Services
 */
public class DigestDictionaryRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private final DigestDictionaryRule rule = new DigestDictionaryRule(
    new EncodingHashBean(new CodecSpec("Hex-Upper"), new DigestSpec("SHA1")));

  /** For testing. */
  private final DigestDictionaryRule backwardsRule = new DigestDictionaryRule(
    new EncodingHashBean(new CodecSpec("Hex-Upper"), new DigestSpec("SHA1")));


  /**
   * Initialize rules for this test.
   *
   * @param  dictFile  dictionary file to read
   *
   * @throws  Exception  if dictionary files cannot be read
   */
  @Parameters("digestDictionaryFile")
  @BeforeClass(groups = {"passtest"})
  public void createRules(final String dictFile)
    throws Exception
  {
    final Dictionary caseSensitiveDict = new DictionaryBuilder().addFile(dictFile).setCaseSensitive(true).build();

    rule.setDictionary(caseSensitiveDict);

    backwardsRule.setDictionary(caseSensitiveDict);
    backwardsRule.setMatchBackwards(true);
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
        // valid password
        {rule, new PasswordData("JM@Ntr3xS801!!"), null, },
        // dictionary word
        {
          rule,
          new PasswordData("JMANtrex5801!!"),
          codes(DigestDictionaryRule.ERROR_CODE),
        },
        // backwards dictionary word
        {rule, new PasswordData("!!1085xertNAMJ"), null, },
        // mixed case dictionary word
        {rule, new PasswordData("jmanTREX5801!!"), null, },
        // backwards mixed case dictionary word
        {rule, new PasswordData("!!1085XERTnamj"), null, },

        // valid password
        {backwardsRule, new PasswordData("JM@Ntr3xS801!!"), null, },
        // dictionary word
        {
          backwardsRule,
          new PasswordData("JMANtrex5801!!"),
          codes(DigestDictionaryRule.ERROR_CODE),
        },
        // backward dictionary word
        {
          backwardsRule,
          new PasswordData("!!1085xertNAMJ"),
          codes(DigestDictionaryRule.ERROR_CODE_REVERSED),
        },
        // mixed case dictionary word
        {backwardsRule, new PasswordData("jmanTREX5801!!"), null, },
        // backwards mixed case dictionary word
        {backwardsRule, new PasswordData("!!1085XERTnamj"), null, },
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
          new PasswordData("JMANtrex5801!!"),
          new String[] {"Password contains a dictionary word.", },
        },
        {
          backwardsRule,
          new PasswordData("!!1085xertNAMJ"),
          new String[] {"Password contains a reversed dictionary word.", },
        },
      };
  }
}

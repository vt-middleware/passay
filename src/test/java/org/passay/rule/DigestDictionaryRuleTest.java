/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.PasswordData;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
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
  private DigestDictionaryRule rule;

  /** For testing. */
  private DigestDictionaryRule backwardsRule;


  /**
   * Initialize rules for this test.
   *
   * @param  dictFile  dictionary file to read
   */
  @Parameters("digestDictionaryFile")
  @BeforeClass
  public void createRules(final String dictFile) throws IOException
  {
    final Dictionary caseSensitiveDict = new WordListDictionary(
      WordLists.createFromReader(new Reader[] {new FileReader(dictFile)}, true, new ArraysSort()));
    rule = new DigestDictionaryRule(
      new EncodingHashBean(new CodecSpec("Hex-Upper"), new DigestSpec("SHA1")), caseSensitiveDict);
    backwardsRule = new DigestDictionaryRule(
      new EncodingHashBean(new CodecSpec("Hex-Upper"), new DigestSpec("SHA1")), caseSensitiveDict, true);
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
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
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

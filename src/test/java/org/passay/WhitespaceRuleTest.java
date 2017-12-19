/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link WhitespaceRule}.
 *
 * @author  Middleware Services
 */
public class WhitespaceRuleTest extends AbstractRuleTest
{


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

        {new WhitespaceRule(), new PasswordData("AycDPdsyz"), null, },
        {
          new WhitespaceRule(),
          new PasswordData("AycD Pdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(MatchBehavior.Contains),
          new PasswordData("AycD Pds\tyz"),
          codes(WhitespaceRule.ERROR_CODE, WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("Ayc\tDPdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycDPs\nyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycDPs\ryz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycDPs\n\ryz"),
          codes(WhitespaceRule.ERROR_CODE, WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(MatchBehavior.Contains, false),
          new PasswordData("AycDPs\n\ryz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(MatchBehavior.StartsWith),
          new PasswordData(" AycDPdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(MatchBehavior.StartsWith),
          new PasswordData("AycD Pdsyz"),
          null,
        },
        {
          new WhitespaceRule(MatchBehavior.EndsWith),
          new PasswordData("AycDPdsyz "),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(MatchBehavior.EndsWith),
          new PasswordData("AycD Pdsyz"),
          null,
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
          new WhitespaceRule(MatchBehavior.StartsWith),
          new PasswordData("\tAycDPdsyz"),
          new String[] {"Password starts with a whitespace character.", },
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycD Pdsyz"),
          new String[] {"Password contains a whitespace character.", },
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycD Pds\tyz"),
          new String[] {"Password contains a whitespace character.", "Password contains a whitespace character.", },
        },
        {
          new WhitespaceRule(MatchBehavior.Contains, false),
          new PasswordData("AycD Pds\tyz"),
          new String[] {"Password contains a whitespace character.",  },
        },
        {
          new WhitespaceRule(MatchBehavior.EndsWith),
          new PasswordData("AycDPdsyz\n"),
          new String[] {"Password ends with a whitespace character.", },
        },
      };
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"passtest"})
  public void checkMetadata()
    throws Exception
  {
    final WhitespaceRule rule = new WhitespaceRule();
    RuleResult result = rule.validate(new PasswordData("metadata"));
    AssertJUnit.assertTrue(result.isValid());
    AssertJUnit.assertEquals(0, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Whitespace));

    result = rule.validate(new PasswordData("meta data"));
    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals(1, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Whitespace));
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"passtest"}, expectedExceptions = IllegalArgumentException.class)
  public void checkValidCharacters()
    throws Exception
  {
    new WhitespaceRule(new char[] {' ', 'a'});
  }
}

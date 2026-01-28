/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultMetadata;
import org.passay.UnicodeString;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link WhitespaceRule}.
 *
 * @author  Middleware Services
 */
public class WhitespaceRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
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
   * Test Metadata.
   */
  @Test
  public void checkMetadata()
  {
    final WhitespaceRule rule = new WhitespaceRule();
    RuleResult result = rule.validate(new PasswordData("metadata"));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Whitespace)).isEqualTo(0);

    result = rule.validate(new PasswordData("meta data"));
    assertThat(result.isValid()).isFalse();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Whitespace)).isEqualTo(1);
  }


  /**
   * Test valid characters.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void checkValidCharacters()
  {
    new WhitespaceRule(new UnicodeString(' ', 'a'));
  }
}

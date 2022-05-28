/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.passay.logic.PasswordData;
import org.passay.rule.CharacterOccurrencesRule;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link CharacterOccurrencesRule}.
 *
 * @author  Amichai Rothman
 */
public class CharacterOccurrencesRuleTest extends AbstractRuleTest
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
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("p4zRcv101#n6F"),
          null,
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("aaaa#n65"),
          null,
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("a1a2a3a4#n65bbbb"),
          null,
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("aaaaa"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("aaaaa#n65"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("111aaaaa"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("aaaaabbb"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("a1a2a3a4a"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("1aa2aa3a"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("babababab"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("ababababa"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(5),
          new PasswordData("1aa2aa3aa4bbb5bb6bbb"),
          codes(CharacterOccurrencesRule.ERROR_CODE, CharacterOccurrencesRule.ERROR_CODE),
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
          new CharacterOccurrencesRule(4),
          new PasswordData("a1a2a3a4a5a"),
          new String[] {"Password contains 6 occurrences of the character 'a', but at most 4 are allowed.", },
        },
      };
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
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
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
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
          new PasswordData("\u16C81\u16C82\u16C83\u16C84#n65bbbb"),
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
          new CharacterOccurrencesRule(4),
          new PasswordData("\u16C8b\u16C8b\u16C8b\u16C8b\u16C8"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("\uD808\uDF34b\uD808\uDF34b\uD808\uDF34b\uD808\uDF34b\uD808\uDF34"),
          codes(CharacterOccurrencesRule.ERROR_CODE),
        },
        // unicode character with multiple codepoints; each codepoint is considered 1 character
        {
          new CharacterOccurrencesRule(4),
          new PasswordData(
            "\uD83C\uDDEE\uD83C\uDDF8" + "b" +
            "\uD83C\uDDEE\uD83C\uDDF8" + "b" +
            "\uD83C\uDDEE\uD83C\uDDF8" + "b" +
            "\uD83C\uDDEE\uD83C\uDDF8" + "b" +
            "\uD83C\uDDEE\uD83C\uDDF8"),
          codes(CharacterOccurrencesRule.ERROR_CODE, CharacterOccurrencesRule.ERROR_CODE),
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
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          new CharacterOccurrencesRule(4),
          new PasswordData("a1a2a3a4a5a"),
          new String[] {"Password contains 6 occurrences of the character 'a', but at most 4 are allowed.", },
        },
        {
          new CharacterOccurrencesRule(3),
          new PasswordData("\uD83D\uDF581a2\uD83D\uDF583a4\uD83D\uDF585\uD83D\uDF58"),
          new String[] {
            "Password contains 4 occurrences of the character '\uD83D\uDF58', but at most 3 are allowed.",
          },
        },
      };
  }
}

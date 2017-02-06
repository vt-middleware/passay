/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

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
          new PasswordData("AycD" + " " + "Pdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("Ayc" + "\t" + "DPdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycDPs" + System.getProperty("line.separator") + "yz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(StringMatch.StartsWith),
          new PasswordData(" AycDPdsyz"),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          new WhitespaceRule(StringMatch.EndsWith),
          new PasswordData("AycDPdsyz "),
          codes(WhitespaceRule.ERROR_CODE),
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
          new WhitespaceRule(StringMatch.StartsWith),
          new PasswordData("\tAycDPdsyz"),
          new String[] {"Password starts with a whitespace character.", },
        },
        {
          new WhitespaceRule(),
          new PasswordData("AycD Pdsyz"),
          new String[] {"Password contains a whitespace character.", },
        },
        {
          new WhitespaceRule(StringMatch.EndsWith),
          new PasswordData("AycDPdsyz\n"),
          new String[] {"Password ends with a whitespace character.", },
        },
      };
  }
}

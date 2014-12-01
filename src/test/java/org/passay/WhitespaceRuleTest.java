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

  /** Test password. */
  private static final String VALID_PASS = "AycDPdsyz";

  /** Test password. */
  private static final String SPACE_PASS = "AycD" + " " + "Pdsyz";

  /** Test password. */
  private static final String TAB_PASS = "Ayc" + "\t" + "DPdsyz";

  /** Test password. */
  private static final String LINE_SEP_PASS = "AycDPs" +
    System.getProperty("line.separator") + "yz";

  /** For testing. */
  private final WhitespaceRule rule = new WhitespaceRule();


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
          new PasswordData(SPACE_PASS),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData(TAB_PASS),
          codes(WhitespaceRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData(LINE_SEP_PASS),
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
          rule,
          new PasswordData(SPACE_PASS),
          new String[] {"Password cannot contain whitespace characters.", },
        },
      };
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link IllegalCharacterRule}.
 *
 * @author  Middleware Services
 */
public class IllegalCharacterRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "AycDPdsyz";

  /** Test password. */
  private static final String INVALID_PASS = "AycD@Pdsyz";

  /** For testing. */
  private final IllegalCharacterRule rule = new IllegalCharacterRule(
    new char[] {'@'});


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
          new PasswordData(INVALID_PASS),
          codes(IllegalCharacterRule.ERROR_CODE),
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
          new PasswordData(INVALID_PASS),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "@"),
          },
        },
      };
  }
}

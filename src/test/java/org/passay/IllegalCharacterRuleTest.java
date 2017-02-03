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

        // test valid password
        {new IllegalCharacterRule(new char[] {'@', '$'}), new PasswordData("AycDPdsyz"), null, },
        // test invalid password
        {
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pdsyz"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        // test multiple matches
        {
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pd$yz"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        // test single match
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, false),
          new PasswordData("AycD@Pd$yz"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pd$yz@"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
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
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pdsyz"),
          new String[] {String.format("Password contains the illegal character '%s'.", "@"), },
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pd$yz"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "@"),
            String.format("Password contains the illegal character '%s'.", "$"), },
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, false),
          new PasswordData("AycD@Pd$yz"),
          new String[] {String.format("Password contains the illegal character '%s'.", "@")},
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}),
          new PasswordData("AycD@Pd$yz@"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "@"),
            String.format("Password contains the illegal character '%s'.", "$"), },
        },
      };
  }
}

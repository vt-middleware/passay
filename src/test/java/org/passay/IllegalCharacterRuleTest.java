/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link IllegalCharacterRule}.
 *
 * @author  Middleware Services
 */
public class IllegalCharacterRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
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
        // test match behavior
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.StartsWith),
          new PasswordData("@ycDAPdSyz&"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.StartsWith),
          new PasswordData("AycD@Pdsyz"),
          null,
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.EndsWith),
          new PasswordData("AycDAPdSyz@"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.EndsWith),
          new PasswordData("AycD@Pdsyz"),
          null,
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
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.StartsWith),
          new PasswordData("@ycDAPdsyz"),
          new String[] {String.format("Password starts with the illegal character '%s'.", "@"), },
        },
        {
          new IllegalCharacterRule(new char[] {'@', '$'}, MatchBehavior.EndsWith),
          new PasswordData("AycDAPdsyz$"),
          new String[] {String.format("Password ends with the illegal character '%s'.", "$"), },
        },
      };
  }


  /**
   * Test Metadata.
   */
  @Test(groups = {"passtest"})
  public void checkMetadata()
  {
    final IllegalCharacterRule rule = new IllegalCharacterRule(new char[] {'@', '$'});
    RuleResult result = rule.validate(new PasswordData("metadata"));
    AssertJUnit.assertTrue(result.isValid());
    AssertJUnit.assertEquals(0, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Illegal));

    result = rule.validate(new PasswordData("met@data$"));
    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals(2, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Illegal));
  }
}

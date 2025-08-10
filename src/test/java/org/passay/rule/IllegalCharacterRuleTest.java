/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultMetadata;
import org.passay.UnicodeString;
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
        {new IllegalCharacterRule(new UnicodeString('@', '$')), new PasswordData("AycDPdsyz"), null, },
        // test invalid password
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pdsyz"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        // test multiple matches
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pd$yz"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        // test single match
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), false),
          new PasswordData("AycD@Pd$yz"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pd$yz@"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        // test match behavior
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.StartsWith),
          new PasswordData("@ycDAPdSyz&"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.StartsWith),
          new PasswordData("AycD@Pdsyz"),
          null,
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.EndsWith),
          new PasswordData("AycDAPdSyz@"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.EndsWith),
          new PasswordData("AycD@Pdsyz"),
          null,
        },
        // test unicode
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pdsčz"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$', 'č')),
          new PasswordData("AycD@Pdsčz"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pds\u16C8z"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$', '\u16C8')),
          new PasswordData("AycD@Pds\u16C8z"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pds\uD808\uDF34z"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$', Character.toCodePoint('\uD808', '\uDF34'))),
          new PasswordData("AycD@Pds\uD808\uDF34z"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("A\uD808\uDF34cD@Pds\uD808\uDF34z"),
          codes(IllegalCharacterRule.ERROR_CODE),
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$', Character.toCodePoint('\uD808', '\uDF34'))),
          new PasswordData("A\uD808\uDF34cD@Pds\uD808\uDF34z"),
          codes(IllegalCharacterRule.ERROR_CODE, IllegalCharacterRule.ERROR_CODE),
        },
        // single unicode character but represented by 2 code points
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pds\uD83C\uDDEE\uD83C\uDDF8z"),
          codes(IllegalCharacterRule.ERROR_CODE),
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
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pdsyz"),
          new String[] {String.format("Password contains the illegal character '%s'.", "@"), },
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pd$yz"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "@"),
            String.format("Password contains the illegal character '%s'.", "$"), },
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), false),
          new PasswordData("AycD@Pd$yz"),
          new String[] {String.format("Password contains the illegal character '%s'.", "@")},
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$')),
          new PasswordData("AycD@Pd$yz@"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "@"),
            String.format("Password contains the illegal character '%s'.", "$"), },
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$', ' '), MatchBehavior.Contains, true),
          new PasswordData("AycD Pdsyz"),
          new String[] {String.format("Whitespace is not allowed."), },
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.StartsWith),
          new PasswordData("@ycDAPdsyz"),
          new String[] {String.format("Password begins with the illegal character '%s'.", "@"), },
        },
        {
          new IllegalCharacterRule(new UnicodeString('@', '$'), MatchBehavior.EndsWith),
          new PasswordData("AycDAPdsyz$"),
          new String[] {String.format("Password ends with the illegal character '%s'.", "$"), },
        },
      };
  }


  /**
   * Test Metadata.
   */
  @Test(groups = "passtest")
  public void checkMetadata()
  {
    final IllegalCharacterRule rule = new IllegalCharacterRule(new UnicodeString('@', '$'));
    RuleResult result = rule.validate(new PasswordData("metadata"));
    AssertJUnit.assertTrue(result.isValid());
    AssertJUnit.assertEquals(0, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Illegal));

    result = rule.validate(new PasswordData("met@data$"));
    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals(2, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Illegal));
  }
}

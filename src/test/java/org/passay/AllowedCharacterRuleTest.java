/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link AllowedCharacterRule}.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRuleTest extends AbstractRuleTest
{

  /** Allowed characters for testing. */
  private static final char[] ALLOWED_CHARS = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', };


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        // test valid password
        {new AllowedCharacterRule(ALLOWED_CHARS), new PasswordData("boepselwezz"), null, },
        // test invalid password
        {
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        // test multiple matches
        {
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE, AllowedCharacterRule.ERROR_CODE),
        },
        // test single match
        {
          new AllowedCharacterRule(ALLOWED_CHARS, false),
          new PasswordData("gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk5kk4"),
          codes(AllowedCharacterRule.ERROR_CODE, AllowedCharacterRule.ERROR_CODE),
        },
        // test match behavior
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.StartsWith),
          new PasswordData("4gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.StartsWith),
          new PasswordData("gbwersco4kk"),
          null,
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk5kk4"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk"),
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
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk"),
          new String[] {String.format("Password contains the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk5kk"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "4"),
            String.format("Password contains the illegal character '%s'.", "5"), },
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, false),
          new PasswordData("gbwersco4kk5kk"),
          new String[] {String.format("Password contains the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS),
          new PasswordData("gbwersco4kk5kk4"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "4"),
            String.format("Password contains the illegal character '%s'.", "5"), },
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.StartsWith),
          new PasswordData("4bwersco4kk"),
          new String[] {String.format("Password starts with the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(ALLOWED_CHARS, MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk4"),
          new String[] {String.format("Password ends with the illegal character '%s'.", "4"), },
        },
      };
  }

  /**
   * Test Metadata.
   */
  @Test(groups = {"passtest"})
  public void checkMetadata()
  {
    final AllowedCharacterRule rule = new AllowedCharacterRule(ALLOWED_CHARS);
    RuleResult result = rule.validate(new PasswordData("metadata"));
    AssertJUnit.assertTrue(result.isValid());
    AssertJUnit.assertEquals(8, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Allowed));

    result = rule.validate(new PasswordData("metaDATA"));
    AssertJUnit.assertFalse(result.isValid());
    AssertJUnit.assertEquals(4, result.getMetadata().getCount(RuleResultMetadata.CountCategory.Allowed));
  }
}

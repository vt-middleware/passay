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
        {new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)), new PasswordData("boepselwezz"), null, },
        // test invalid password
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        // test multiple matches
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE, AllowedCharacterRule.ERROR_CODE),
        },
        // test single match
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), false),
          new PasswordData("gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        // test duplicate matches
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk5kk4"),
          codes(AllowedCharacterRule.ERROR_CODE, AllowedCharacterRule.ERROR_CODE),
        },
        // test match behavior
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.StartsWith),
          new PasswordData("4gbwersco4kk5kk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.StartsWith),
          new PasswordData("gbwersco4kk"),
          null,
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk5kk4"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk"),
          null,
        },
        // test unicode
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersčokk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(new UnicodeString('g', 'b', 'w', 'e', 'r', 's', 'č', 'o', 'k')),
          new PasswordData("gbwersčokk"),
          null,
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwers\u16C8okk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(new UnicodeString('g', 'b', 'w', 'e', 'r', 's', '\u16C8', 'o', 'k')),
          new PasswordData("gbwers\u16C8okk"),
          null,
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwers\uD808\uDF34okk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        {
          new AllowedCharacterRule(new UnicodeString("gbwers\uD808\uDF34ok")),
          new PasswordData("gbwers\uD808\uDF34okk"),
          null,
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gb\uD808\uDF34ers\uD808\uDF34okk"),
          codes(AllowedCharacterRule.ERROR_CODE),
        },
        // single unicode character but represented by 2 code points
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwers\uD83C\uDDEE\uD83C\uDDF8okk"),
          codes(AllowedCharacterRule.ERROR_CODE, AllowedCharacterRule.ERROR_CODE),
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
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk"),
          new String[] {String.format("Password contains the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk5kk"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "4"),
            String.format("Password contains the illegal character '%s'.", "5"), },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), false),
          new PasswordData("gbwersco4kk5kk"),
          new String[] {String.format("Password contains the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS)),
          new PasswordData("gbwersco4kk5kk4"),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "4"),
            String.format("Password contains the illegal character '%s'.", "5"), },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.Contains, true),
          new PasswordData("gbwer scokkk"),
          new String[] {"Whitespace not allowed.", },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.StartsWith),
          new PasswordData("4bwersco4kk"),
          new String[] {String.format("Password starts with the illegal character '%s'.", "4"), },
        },
        {
          new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS), MatchBehavior.EndsWith),
          new PasswordData("gbwersco4kk4"),
          new String[] {String.format("Password ends with the illegal character '%s'.", "4"), },
        },
      };
  }

  /**
   * Test Metadata.
   */
  @Test
  public void checkMetadata()
  {
    final AllowedCharacterRule rule = new AllowedCharacterRule(new UnicodeString(ALLOWED_CHARS));
    RuleResult result = rule.validate(new PasswordData("metadata"));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Allowed)).isEqualTo(8);

    result = rule.validate(new PasswordData("metaDATA"));
    assertThat(result.isValid()).isFalse();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Allowed)).isEqualTo(4);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultMetadata;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link LengthRule}.
 *
 * @author  Middleware Services
 */
public class LengthRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        {new LengthRule(8, 10), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(8, 10), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(8, 10), new PasswordData("p4T3to#6Tu"), null, },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(8), new PasswordData("p4T3#6Tu"), null, },
        {
          new LengthRule(8),
          new PasswordData("p4T3t#6Tu"),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T3to#6Tu"),
          codes(LengthRule.ERROR_CODE_MAX),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {
          new LengthRule(8),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(0, 10), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T3to#6Tu"), null, },
        {new LengthRule(0, 10), new PasswordData("p4T36"), null, },
        {
          new LengthRule(0, 10),
          new PasswordData("p4T3j76rE@#"),
          codes(LengthRule.ERROR_CODE_MAX),
        },

        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3#6Tu"), null, },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3t#6Tu"), null, },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3to#6Tu"), null, },
        {
          new LengthRule(8, Integer.MAX_VALUE),
          new PasswordData("p4T36"),
          codes(LengthRule.ERROR_CODE_MIN),
        },
        {new LengthRule(8, Integer.MAX_VALUE), new PasswordData("p4T3j76rE@#"), null, },
        {new LengthRule(8, 10), new PasswordData("p4T3#6čT"), null, },
        // 2 byte character ᛈ
        {new LengthRule(8, 8), new PasswordData("p4T3#6\u16C8T"), null, },
        // 4 byte character 𒌴
        {new LengthRule(8, 8), new PasswordData("p4T3#6\uD808\uDF34T"), null, },
        // 8 byte character 🇮🇸, passes but the character is represented as 2 code points
        {new LengthRule(8, 9), new PasswordData("p4T3#6\uD83C\uDDEE\uD83C\uDDF8T"), null, },
        // 8 byte character 🇮🇸, fails with max length of 8
        {
          new LengthRule(8, 8),
          new PasswordData("p4T3#6\uD83C\uDDEE\uD83C\uDDF8T"),
          codes(LengthRule.ERROR_CODE_MAX),
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
          new LengthRule(8, 10),
          new PasswordData("p4T3j76rE@#"),
          new String[] {
            String.format("Password must be no more than %s characters in length.", 10),
          },
        },
        {
          new LengthRule(8, 10),
          new PasswordData("p4T36"),
          new String[] {String.format("Password must be %s or more characters in length.", 8), },
        },
      };
  }

  /**
   * Test Metadata.
   */
  @Test(groups = "passtest")
  public void checkMetadata()
  {
    final LengthRule rule = new LengthRule(4, 10);
    RuleResult result = rule.validate(new PasswordData("metadata"));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Length)).isEqualTo(8);

    result = rule.validate(new PasswordData("md"));
    assertThat(result.isValid()).isFalse();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Length)).isEqualTo(2);
  }
}

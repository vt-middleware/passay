/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;
// CheckStyle:AvoidStaticImport OFF
import static org.passay.CharacterSequences.EN_ALPHABETICAL;
import static org.passay.CharacterSequences.EN_NUMERICAL;
import static org.passay.CharacterSequences.EN_QWERTY;
// CheckStyle:AvoidStaticImport ON

/**
 * Unit test for {@link SequenceRule}.
 *
 * @author  Middleware Services
 */
public class SequenceRuleTest extends AbstractRuleTest
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
        /* QWERTY SEQUENCE */
        // Test valid password
        {
          new SequenceRule(EN_QWERTY),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has qwerty sequence
        {
          new SequenceRule(6, false, EN_QWERTY),
          new PasswordData("pqwerty#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has wrapping qwerty sequence with wrap=false
        {
          new SequenceRule(EN_QWERTY),
          new PasswordData("pkl;'a#n65"),
          null,
        },
        // Has wrapping qwerty sequence with wrap=true
        {
          new SequenceRule(8, true, EN_QWERTY),
          new PasswordData("piop{}|qw#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward qwerty sequence
        {
          new SequenceRule(4, false, EN_QWERTY),
          new PasswordData("p7^54#n65"),
          codes(SequenceRule.ERROR_CODE, SequenceRule.ERROR_CODE),
        },
        // Has backward wrapping qwerty sequence with wrap=false
        {
          new SequenceRule(8, false, EN_QWERTY),
          new PasswordData("phgfdsa\";#n65"),
          null,
        },
        // Has backward wrapping qwerty sequence with wrap=true
        {
          new SequenceRule(6, true, EN_QWERTY),
          new PasswordData("p@1`+_0#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // report single error
        {
          new SequenceRule(6, false, false, EN_QWERTY),
          new PasswordData("pqwertyui#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        /* ALPHABETICAL SEQUENCE */
        // Test valid password
        {
          new SequenceRule(EN_ALPHABETICAL),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has alphabetical sequence
        {
          new SequenceRule(7, false, EN_ALPHABETICAL),
          new PasswordData("phijklmn#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has wrapping alphabetical sequence with wrap=false
        {
          new SequenceRule(4, false, EN_ALPHABETICAL),
          new PasswordData("pXyza#n65"),
          null,
        },
        // Has wrapping alphabetical sequence with wrap=true
        {
          new SequenceRule(4, true, EN_ALPHABETICAL),
          new PasswordData("pxyzA#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence
        {
          new SequenceRule(EN_ALPHABETICAL),
          new PasswordData("ptSrqp#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward wrapping alphabetical sequence with wrap=false
        {
          new SequenceRule(8, false, EN_ALPHABETICAL),
          new PasswordData("pcBazyXwv#n65"),
          null,
        },
        // Has backward wrapping alphabetical sequence with wrap=true
        {
          new SequenceRule(8, true, EN_ALPHABETICAL),
          new PasswordData("pcbazyxwv#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'y'
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("wxy"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'z'
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("xyz"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=false
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("yza"),
          null,
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=true
        {
          new SequenceRule(3, true, EN_ALPHABETICAL),
          new PasswordData("yza"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'b'
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("dcb"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'a'
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("cba"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=false
        {
          new SequenceRule(3, false, EN_ALPHABETICAL),
          new PasswordData("baz"),
          null,
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=true
        {
          new SequenceRule(3, true, EN_ALPHABETICAL),
          new PasswordData("baz"),
          codes(SequenceRule.ERROR_CODE),
        },
        // report single error
        {
          new SequenceRule(5, false, false, EN_ALPHABETICAL),
          new PasswordData("phijklmn#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        /* NUMERICAL SEQUENCE */
        // Test valid password
        {
          new SequenceRule(EN_NUMERICAL),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has numerical sequence
        {
          new SequenceRule(4, false, EN_NUMERICAL),
          new PasswordData("p3456#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has wrapping numerical sequence with wrap=false
        {
          new SequenceRule(7, false, EN_NUMERICAL),
          new PasswordData("p4zRcv2#n8901234"),
          null,
        },
        // Has wrapping numerical sequence with wrap=true
        {
          new SequenceRule(7, true, EN_NUMERICAL),
          new PasswordData("p4zRcv2#n8901234"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward numerical sequence
        {
          new SequenceRule(EN_NUMERICAL),
          new PasswordData("p54321#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Has backward wrapping numerical sequence with wrap=false
        {
          new SequenceRule(5, false, EN_NUMERICAL),
          new PasswordData("p987#n32109"),
          null,
        },
        // Has backward wrapping numerical sequence with wrap=true
        {
          new SequenceRule(8, true, EN_NUMERICAL),
          new PasswordData("p54321098#n65"),
          codes(SequenceRule.ERROR_CODE),
        },
        // Issue 135
        {
          new SequenceRule(5, true, EN_NUMERICAL),
          new PasswordData("1234567"),
          codes(
            SequenceRule.ERROR_CODE,
            SequenceRule.ERROR_CODE,
            SequenceRule.ERROR_CODE),
        },
        // report single error
        {
          new SequenceRule(5, true, false, EN_NUMERICAL),
          new PasswordData("1234567"),
          codes(SequenceRule.ERROR_CODE),
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
          new SequenceRule(EN_QWERTY),
          new PasswordData("pkwerty#n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "werty"),
          },
        },
        {
          new SequenceRule(5, true, false, EN_QWERTY),
          new PasswordData("pkl;'asd65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "kl;'a"),
          },
        },
        {
          new SequenceRule(EN_ALPHABETICAL),
          new PasswordData("phijkl#n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "hijkl"),
          },
        },
        {
          new SequenceRule(5, true, false, EN_ALPHABETICAL),
          new PasswordData("phijklmno#n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "hijkl"),
          },
        },
        {
          new SequenceRule(EN_NUMERICAL),
          new PasswordData("p34567n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "34567"),
          },
        },
        {
          new SequenceRule(5, false, false, EN_NUMERICAL),
          new PasswordData("p3456789n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "34567"),
          },
        },
      };
  }
}

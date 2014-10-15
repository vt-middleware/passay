/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

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
          new SequenceRule(SequenceData.QWERTY),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has qwerty sequence
        {
          new SequenceRule(SequenceData.QWERTY, 6, false),
          new PasswordData("pqwerty#n65"),
          codes(SequenceData.QWERTY.getErrorCode()),
        },
        // Has wrapping qwerty sequence with wrap=false
        {
          new SequenceRule(SequenceData.QWERTY),
          new PasswordData("pkl;'a#n65"),
          null,
        },
        // Has wrapping qwerty sequence with wrap=true
        {
          new SequenceRule(SequenceData.QWERTY, 8, true),
          new PasswordData("piop{}|qw#n65"),
          codes(SequenceData.QWERTY.getErrorCode()),
        },
        // Has backward qwerty sequence
        {
          new SequenceRule(SequenceData.QWERTY, 4, false),
          new PasswordData("p7^54#n65"),
          codes(
            SequenceData.QWERTY.getErrorCode(),
            SequenceData.QWERTY.getErrorCode()),
        },
        // Has backward wrapping qwerty sequence with wrap=false
        {
          new SequenceRule(SequenceData.QWERTY, 8, false),
          new PasswordData("phgfdsa\";#n65"),
          null,
        },
        // Has backward wrapping qwerty sequence with wrap=true
        {
          new SequenceRule(SequenceData.QWERTY, 6, true),
          new PasswordData("p@1`+_0#n65"),
          codes(SequenceData.QWERTY.getErrorCode()),
        },
        // report single error
        {
          new SequenceRule(SequenceData.QWERTY, 6, false, false),
          new PasswordData("pqwertyui#n65"),
          codes(SequenceData.QWERTY.getErrorCode()),
        },
        /* ALPHABETICAL SEQUENCE */
        // Test valid password
        {
          new SequenceRule(SequenceData.ALPHABETICAL),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has alphabetical sequence
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 7, false),
          new PasswordData("phijklmn#n65"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has wrapping alphabetical sequence with wrap=false
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 4, false),
          new PasswordData("pXyza#n65"),
          null,
        },
        // Has wrapping alphabetical sequence with wrap=true
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 4, true),
          new PasswordData("pxyzA#n65"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has backward alphabetical sequence
        {
          new SequenceRule(SequenceData.ALPHABETICAL),
          new PasswordData("ptSrqp#n65"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has backward wrapping alphabetical sequence with wrap=false
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 8, false),
          new PasswordData("pcBazyXwv#n65"),
          null,
        },
        // Has backward wrapping alphabetical sequence with wrap=true
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 8, true),
          new PasswordData("pcbazyxwv#n65"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'y'
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("wxy"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'z'
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("xyz"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=false
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("yza"),
          null,
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=true
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, true),
          new PasswordData("yza"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'b'
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("dcb"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'a'
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("cba"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=false
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, false),
          new PasswordData("baz"),
          null,
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=true
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 3, true),
          new PasswordData("baz"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        // report single error
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 5, false, false),
          new PasswordData("phijklmn#n65"),
          codes(SequenceData.ALPHABETICAL.getErrorCode()),
        },
        /* NUMERICAL SEQUENCE */
        // Test valid password
        {
          new SequenceRule(SequenceData.NUMERICAL),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has numerical sequence
        {
          new SequenceRule(SequenceData.NUMERICAL, 4, false),
          new PasswordData("p3456#n65"),
          codes(SequenceData.NUMERICAL.getErrorCode()),
        },
        // Has wrapping numerical sequence with wrap=false
        {
          new SequenceRule(SequenceData.NUMERICAL, 7, false),
          new PasswordData("p4zRcv2#n8901234"),
          null,
        },
        // Has wrapping numerical sequence with wrap=true
        {
          new SequenceRule(SequenceData.NUMERICAL, 7, true),
          new PasswordData("p4zRcv2#n8901234"),
          codes(SequenceData.NUMERICAL.getErrorCode()),
        },
        // Has backward numerical sequence
        {
          new SequenceRule(SequenceData.NUMERICAL),
          new PasswordData("p54321#n65"),
          codes(SequenceData.NUMERICAL.getErrorCode()),
        },
        // Has backward wrapping numerical sequence with wrap=false
        {
          new SequenceRule(SequenceData.NUMERICAL, 5, false),
          new PasswordData("p987#n32109"),
          null,
        },
        // Has backward wrapping numerical sequence with wrap=true
        {
          new SequenceRule(SequenceData.NUMERICAL, 8, true),
          new PasswordData("p54321098#n65"),
          codes(SequenceData.NUMERICAL.getErrorCode()),
        },
        // Issue 135
        {
          new SequenceRule(SequenceData.NUMERICAL, 5, true),
          new PasswordData("1234567"),
          codes(
            SequenceData.NUMERICAL.getErrorCode(),
            SequenceData.NUMERICAL.getErrorCode(),
            SequenceData.NUMERICAL.getErrorCode()),
        },
        // report single error
        {
          new SequenceRule(SequenceData.NUMERICAL, 5, true, false),
          new PasswordData("1234567"),
          codes(SequenceData.NUMERICAL.getErrorCode()),
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
          new SequenceRule(SequenceData.QWERTY),
          new PasswordData("pkwerty#n65"),
          new String[] {
            String.format(
              "Password contains the illegal QWERTY sequence '%s'.", "werty"),
          },
        },
        {
          new SequenceRule(SequenceData.QWERTY, 5, true, false),
          new PasswordData("pkl;'asd65"),
          new String[] {
            String.format(
              "Password contains the illegal QWERTY sequence '%s'.", "kl;'a"),
          },
        },
        {
          new SequenceRule(SequenceData.ALPHABETICAL),
          new PasswordData("phijkl#n65"),
          new String[] {
            String.format(
              "Password contains the illegal alphabetical sequence '%s'.",
              "hijkl"),
          },
        },
        {
          new SequenceRule(SequenceData.ALPHABETICAL, 5, true, false),
          new PasswordData("phijklmno#n65"),
          new String[] {
            String.format(
              "Password contains the illegal alphabetical sequence '%s'.",
              "hijkl"),
          },
        },
        {
          new SequenceRule(SequenceData.NUMERICAL),
          new PasswordData("p34567n65"),
          new String[] {
            String.format(
              "Password contains the illegal numerical sequence '%s'.",
              "34567"),
          },
        },
        {
          new SequenceRule(SequenceData.NUMERICAL, 5, false, false),
          new PasswordData("p3456789n65"),
          new String[] {
            String.format(
              "Password contains the illegal numerical sequence '%s'.",
              "34567"),
          },
        },
      };
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link IllegalSequenceRule}.
 *
 * @author  Middleware Services
 */
public class IllegalSequenceRuleTest extends AbstractRuleTest
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
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has qwerty sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 6, false),
          new PasswordData("pqwerty#n65"),
          codes(EnIllegalSequenceData.Qwerty.getErrorCode()),
        },
        // Has wrapping qwerty sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty),
          new PasswordData("pkl;'a#n65"),
          null,
        },
        // Has wrapping qwerty sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 8, true),
          new PasswordData("piop{}|qw#n65"),
          codes(EnIllegalSequenceData.Qwerty.getErrorCode()),
        },
        // Has backward qwerty sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 4, false),
          new PasswordData("p7^54#n65"),
          codes(
            EnIllegalSequenceData.Qwerty.getErrorCode(),
            EnIllegalSequenceData.Qwerty.getErrorCode()),
        },
        // Has backward wrapping qwerty sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 8, false),
          new PasswordData("phgfdsa\";#n65"),
          null,
        },
        // Has backward wrapping qwerty sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 6, true),
          new PasswordData("p@1`+_0#n65"),
          codes(EnIllegalSequenceData.Qwerty.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(
            EnIllegalSequenceData.Qwerty, 6, false, false),
          new PasswordData("pqwertyui#n65"),
          codes(EnIllegalSequenceData.Qwerty.getErrorCode()),
        },
        /* ALPHABETICAL SEQUENCE */
        // Test valid password
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has alphabetical sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 7, false),
          new PasswordData("phijklmn#n65"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has wrapping alphabetical sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 4, false),
          new PasswordData("pXyza#n65"),
          null,
        },
        // Has wrapping alphabetical sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 4, true),
          new PasswordData("pxyzA#n65"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical),
          new PasswordData("ptSrqp#n65"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward wrapping alphabetical sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 8, false),
          new PasswordData("pcBazyXwv#n65"),
          null,
        },
        // Has backward wrapping alphabetical sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 8, true),
          new PasswordData("pcbazyxwv#n65"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'y'
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("wxy"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'z'
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("xyz"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("yza"),
          null,
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, true),
          new PasswordData("yza"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'b'
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("dcb"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'a'
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("cba"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, false),
          new PasswordData("baz"),
          null,
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical, 3, true),
          new PasswordData("baz"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(
            EnIllegalSequenceData.Alphabetical, 5, false, false),
          new PasswordData("phijklmn#n65"),
          codes(EnIllegalSequenceData.Alphabetical.getErrorCode()),
        },
        /* NUMERICAL SEQUENCE */
        // Test valid password
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has numerical sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 4, false),
          new PasswordData("p3456#n65"),
          codes(EnIllegalSequenceData.Numerical.getErrorCode()),
        },
        // Has wrapping numerical sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 7, false),
          new PasswordData("p4zRcv2#n8901234"),
          null,
        },
        // Has wrapping numerical sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 7, true),
          new PasswordData("p4zRcv2#n8901234"),
          codes(EnIllegalSequenceData.Numerical.getErrorCode()),
        },
        // Has backward numerical sequence
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical),
          new PasswordData("p54321#n65"),
          codes(EnIllegalSequenceData.Numerical.getErrorCode()),
        },
        // Has backward wrapping numerical sequence with wrap=false
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 5, false),
          new PasswordData("p987#n32109"),
          null,
        },
        // Has backward wrapping numerical sequence with wrap=true
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 8, true),
          new PasswordData("p54321098#n65"),
          codes(EnIllegalSequenceData.Numerical.getErrorCode()),
        },
        // Issue 135
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical, 5, true),
          new PasswordData("1234567"),
          codes(
            EnIllegalSequenceData.Numerical.getErrorCode(),
            EnIllegalSequenceData.Numerical.getErrorCode(),
            EnIllegalSequenceData.Numerical.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(
            EnIllegalSequenceData.Numerical, 5, true, false),
          new PasswordData("1234567"),
          codes(EnIllegalSequenceData.Numerical.getErrorCode()),
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
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty),
          new PasswordData("pkwerty#n65"),
          new String[] {
            String.format(
              "Password contains the illegal QWERTY sequence '%s'.", "werty"),
          },
        },
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Qwerty, 5, true, false),
          new PasswordData("pkl;'asd65"),
          new String[] {
            String.format(
              "Password contains the illegal QWERTY sequence '%s'.", "kl;'a"),
          },
        },
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Alphabetical),
          new PasswordData("phijkl#n65"),
          new String[] {
            String.format(
              "Password contains the illegal alphabetical sequence '%s'.",
              "hijkl"),
          },
        },
        {
          new IllegalSequenceRule(
            EnIllegalSequenceData.Alphabetical, 5, true, false),
          new PasswordData("phijklmno#n65"),
          new String[] {
            String.format(
              "Password contains the illegal alphabetical sequence '%s'.",
              "hijkl"),
          },
        },
        {
          new IllegalSequenceRule(EnIllegalSequenceData.Numerical),
          new PasswordData("p34567n65"),
          new String[] {
            String.format(
              "Password contains the illegal numerical sequence '%s'.",
              "34567"),
          },
        },
        {
          new IllegalSequenceRule(
            EnIllegalSequenceData.Numerical, 5, false, false),
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

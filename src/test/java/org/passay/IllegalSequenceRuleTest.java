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
          new IllegalSequenceRule(EnglishSequenceData.Qwerty),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has qwerty sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 6, false),
          new PasswordData("pqwerty#n65"),
          codes(EnglishSequenceData.Qwerty.getErrorCode()),
        },
        // Has wrapping qwerty sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty),
          new PasswordData("pkl;'a#n65"),
          null,
        },
        // Has wrapping qwerty sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 8, true),
          new PasswordData("piop{}|qw#n65"),
          codes(EnglishSequenceData.Qwerty.getErrorCode()),
        },
        // Has backward qwerty sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 4, false),
          new PasswordData("p7^54#n65"),
          codes(EnglishSequenceData.Qwerty.getErrorCode(), EnglishSequenceData.Qwerty.getErrorCode()),
        },
        // Has backward wrapping qwerty sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 8, false),
          new PasswordData("phgfdsa\";#n65"),
          null,
        },
        // Has backward wrapping qwerty sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 6, true),
          new PasswordData("p@1`+_0#n65"),
          codes(EnglishSequenceData.Qwerty.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 6, false, false),
          new PasswordData("pqwertyui#n65"),
          codes(EnglishSequenceData.Qwerty.getErrorCode()),
        },
        /* ALPHABETICAL SEQUENCE */
        // Test valid password
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has alphabetical sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 7, false),
          new PasswordData("phijklmn#n65"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has wrapping alphabetical sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false),
          new PasswordData("pXyza#n65"),
          null,
        },
        // Has wrapping alphabetical sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, true),
          new PasswordData("pxyzA#n65"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
          new PasswordData("ptSrqp#n65"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward wrapping alphabetical sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 8, false),
          new PasswordData("pcBazyXwv#n65"),
          null,
        },
        // Has backward wrapping alphabetical sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 8, true),
          new PasswordData("pcbazyxwv#n65"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'y'
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("wxy"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'z'
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("xyz"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("yza"),
          null,
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, true),
          new PasswordData("yza"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'b'
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("dcb"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'a'
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("cba"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
          new PasswordData("baz"),
          null,
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, true),
          new PasswordData("baz"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false, false),
          new PasswordData("phijklmn#n65"),
          codes(EnglishSequenceData.Alphabetical.getErrorCode()),
        },
        /* NUMERICAL SEQUENCE */
        // Test valid password
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has numerical sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false),
          new PasswordData("p3456#n65"),
          codes(EnglishSequenceData.Numerical.getErrorCode()),
        },
        // Has wrapping numerical sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 7, false),
          new PasswordData("p4zRcv2#n8901234"),
          null,
        },
        // Has wrapping numerical sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 7, true),
          new PasswordData("p4zRcv2#n8901234"),
          codes(EnglishSequenceData.Numerical.getErrorCode()),
        },
        // Has backward numerical sequence
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical),
          new PasswordData("p54321#n65"),
          codes(EnglishSequenceData.Numerical.getErrorCode()),
        },
        // Has backward wrapping numerical sequence with wrap=false
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
          new PasswordData("p987#n32109"),
          null,
        },
        // Has backward wrapping numerical sequence with wrap=true
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 8, true),
          new PasswordData("p54321098#n65"),
          codes(EnglishSequenceData.Numerical.getErrorCode()),
        },
        // Issue 135
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, true),
          new PasswordData("1234567"),
          codes(
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode(),
            EnglishSequenceData.Numerical.getErrorCode()),
        },
        // report single error
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, true, false),
          new PasswordData("1234567"),
          codes(EnglishSequenceData.Numerical.getErrorCode()),
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
          new IllegalSequenceRule(EnglishSequenceData.Qwerty),
          new PasswordData("pkwerty#n65"),
          new String[] {String.format("Password contains the illegal QWERTY sequence '%s'.", "werty"), },
        },
        {
          new IllegalSequenceRule(EnglishSequenceData.Qwerty, 5, true, false),
          new PasswordData("pkl;'asd65"),
          new String[] {String.format("Password contains the illegal QWERTY sequence '%s'.", "kl;'a"), },
        },
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
          new PasswordData("phijkl#n65"),
          new String[] {String.format("Password contains the illegal alphabetical sequence '%s'.", "hijkl"), },
        },
        {
          new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, true, false),
          new PasswordData("phijklmno#n65"),
          new String[] {String.format("Password contains the illegal alphabetical sequence '%s'.", "hijkl"), },
        },
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical),
          new PasswordData("p34567n65"),
          new String[] {String.format("Password contains the illegal numerical sequence '%s'.", "34567"), },
        },
        {
          new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false, false),
          new PasswordData("p3456789n65"),
          new String[] {String.format("Password contains the illegal numerical sequence '%s'.", "34567"), },
        },
      };
  }
}

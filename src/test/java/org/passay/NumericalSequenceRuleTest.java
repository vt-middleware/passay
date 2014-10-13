/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link NumericalSequenceRule}.
 *
 * @author  Middleware Services
 */
public class NumericalSequenceRuleTest extends AbstractRuleTest
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
        // Test valid password
        {
          new NumericalSequenceRule(),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has numerical sequence
        {
          new NumericalSequenceRule(4, false),
          new PasswordData("p3456#n65"),
          codes(NumericalSequenceRule.ERROR_CODE),
        },
        // Has wrapping numerical sequence with wrap=false
        {
          new NumericalSequenceRule(7, false),
          new PasswordData("p4zRcv2#n8901234"),
          null,
        },
        // Has wrapping numerical sequence with wrap=true
        {
          new NumericalSequenceRule(7, true),
          new PasswordData("p4zRcv2#n8901234"),
          codes(NumericalSequenceRule.ERROR_CODE),
        },
        // Has backward numerical sequence
        {
          new NumericalSequenceRule(),
          new PasswordData("p54321#n65"),
          codes(NumericalSequenceRule.ERROR_CODE),
        },
        // Has backward wrapping numerical sequence with wrap=false
        {
          new NumericalSequenceRule(5, false),
          new PasswordData("p987#n32109"),
          null,
        },
        // Has backward wrapping numerical sequence with wrap=true
        {
          new NumericalSequenceRule(8, true),
          new PasswordData("p54321098#n65"),
          codes(NumericalSequenceRule.ERROR_CODE),
        },
        // Issue 135
        {
          new NumericalSequenceRule(5, true),
          new PasswordData("1234567"),
          codes(
            NumericalSequenceRule.ERROR_CODE,
            NumericalSequenceRule.ERROR_CODE,
            NumericalSequenceRule.ERROR_CODE),
        },
        // report single error
        {
          new NumericalSequenceRule(5, true, false),
          new PasswordData("1234567"),
          codes(NumericalSequenceRule.ERROR_CODE),
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
          new NumericalSequenceRule(),
          new PasswordData("p34567n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "34567"),
          },
        },
        {
          new NumericalSequenceRule(5, false, false),
          new PasswordData("p3456789n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "34567"),
          },
        },
      };
  }
}

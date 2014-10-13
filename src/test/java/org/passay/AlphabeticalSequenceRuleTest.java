/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link AlphabeticalSequenceRule}.
 *
 * @author  Middleware Services
 */
public class AlphabeticalSequenceRuleTest extends AbstractRuleTest
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
          new AlphabeticalSequenceRule(),
          new PasswordData("p4zRcv8#n65"),
          null,
        },
        // Has alphabetical sequence
        {
          new AlphabeticalSequenceRule(7, false),
          new PasswordData("phijklmn#n65"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has wrapping alphabetical sequence with wrap=false
        {
          new AlphabeticalSequenceRule(4, false),
          new PasswordData("pXyza#n65"),
          null,
        },
        // Has wrapping alphabetical sequence with wrap=true
        {
          new AlphabeticalSequenceRule(4, true),
          new PasswordData("pxyzA#n65"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence
        {
          new AlphabeticalSequenceRule(),
          new PasswordData("ptSrqp#n65"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has backward wrapping alphabetical sequence with wrap=false
        {
          new AlphabeticalSequenceRule(8, false),
          new PasswordData("pcBazyXwv#n65"),
          null,
        },
        // Has backward wrapping alphabetical sequence with wrap=true
        {
          new AlphabeticalSequenceRule(8, true),
          new PasswordData("pcbazyxwv#n65"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'y'
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("wxy"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'z'
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("xyz"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=false
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("yza"),
          null,
        },
        // Has forward alphabetical sequence that ends with 'a' with wrap=true
        {
          new AlphabeticalSequenceRule(3, true),
          new PasswordData("yza"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'b'
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("dcb"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'a'
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("cba"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=false
        {
          new AlphabeticalSequenceRule(3, false),
          new PasswordData("baz"),
          null,
        },
        // Has backward alphabetical sequence that ends with 'z' with wrap=true
        {
          new AlphabeticalSequenceRule(3, true),
          new PasswordData("baz"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
        },
        // report single error
        {
          new AlphabeticalSequenceRule(5, false, false),
          new PasswordData("phijklmn#n65"),
          codes(AlphabeticalSequenceRule.ERROR_CODE),
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
          new AlphabeticalSequenceRule(),
          new PasswordData("phijkl#n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "hijkl"),
          },
        },
        {
          new AlphabeticalSequenceRule(5, true, false),
          new PasswordData("phijklmno#n65"),
          new String[] {
            String.format(
              "Password contains the illegal sequence '%s'.", "hijkl"),
          },
        },
      };
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link NumberRangeRule}.
 *
 * @author  Middleware Services
 */
public class NumberRangeRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        {new NumberRangeRule(101, 199), new PasswordData("p4zRcv8#n65"), null, },
        {
          new NumberRangeRule(101, 199),
          new PasswordData("p4zRcv101#n65"),
          codes(NumberRangeRule.ERROR_CODE),
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.StartsWith),
          new PasswordData("150Rcv8#n65"),
          codes(NumberRangeRule.ERROR_CODE),
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.StartsWith),
          new PasswordData("p4zRcv101#n6F"),
          null,
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.EndsWith),
          new PasswordData("p4zRcv8#n198"),
          codes(NumberRangeRule.ERROR_CODE),
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.EndsWith),
          new PasswordData("p4zRcv101#n6F"),
          null,
        },
        {new NumberRangeRule(101, 199), new PasswordData("p4zRcv99#n65"), null, },
        {new NumberRangeRule(101, 199), new PasswordData("p4zRcv100#n65"), null, },
        {new NumberRangeRule(101, 199), new PasswordData("p4zRcv199#n65"), null, },
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
          new NumberRangeRule(101, 199, MatchBehavior.StartsWith),
          new PasswordData("133Rcv8#n65"),
          new String[] {"Password starts with the number '133'.", },
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.Contains),
          new PasswordData("p4zRcv168#n65"),
          new String[] {"Password contains the number '168'.", },
        },
        {
          new NumberRangeRule(101, 199, MatchBehavior.EndsWith),
          new PasswordData("p4zRcv8#n188"),
          new String[] {"Password ends with the number '188'.", },
        },
      };
  }
}

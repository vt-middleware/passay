/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.List;
import org.passay.PasswordData;
import org.passay.support.HistoricalReference;
import org.passay.support.Reference;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link HistoryRule}.
 *
 * @author  Middleware Services
 */
public class HistoryRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private final List<Reference> history = new ArrayList<>();

  /** For testing. */
  private final HistoryRule rule = new HistoryRule();

  /** For testing. */
  private final HistoryRule ruleReportFirst = new HistoryRule(false);

  /** For testing. */
  private final HistoryRule emptyRule = new HistoryRule();


  /** Initialize rules for this test. */
  @BeforeClass
  public void createRules()
  {
    history.add(new HistoricalReference("history", "t3stUs3r01"));
    history.add(new HistoricalReference("history", "t3stUs3r02"));
    history.add(new HistoricalReference("history", "t3stUs3r03"));
    history.add(new HistoricalReference("history", "t3stUs3r02"));
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        {rule, new PasswordData("testuser", "t3stUs3r00", history), null, },
        {
          rule,
          new PasswordData("testuser", "t3stUs3r01", history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData("testuser", "t3stUs3r02", history),
          codes(HistoryRule.ERROR_CODE, HistoryRule.ERROR_CODE),
        },
        {
          rule,
          new PasswordData("testuser", "t3stUs3r03", history),
          codes(HistoryRule.ERROR_CODE),
        },

        {ruleReportFirst, new PasswordData("testuser", "t3stUs3r00", history), null, },
        {
          ruleReportFirst,
          new PasswordData("testuser", "t3stUs3r01", history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          ruleReportFirst,
          new PasswordData("testuser", "t3stUs3r02", history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          ruleReportFirst,
          new PasswordData("testuser", "t3stUs3r03", history),
          codes(HistoryRule.ERROR_CODE),
        },

        {emptyRule, new PasswordData("testuser", "t3stUs3r00"), null, },
        {emptyRule, new PasswordData("testuser", "t3stUs3r01"), null, },
        {emptyRule, new PasswordData("testuser", "t3stUs3r02"), null, },
        {emptyRule, new PasswordData("testuser", "t3stUs3r03"), null, },
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
          rule,
          new PasswordData("testuser", "t3stUs3r01", history),
          new String[] {String.format("Password matches one of %s previous passwords.", history.size()), },
        },
        {
          rule,
          new PasswordData("testuser", "t3stUs3r02", history),
          new String[] {
            String.format("Password matches one of %s previous passwords.", history.size()),
            String.format("Password matches one of %s previous passwords.", history.size()),
          },
        },
        {
          ruleReportFirst,
          new PasswordData("testuser", "t3stUs3r02", history),
          new String[] {String.format("Password matches one of %s previous passwords.", history.size()), },
        },
      };
  }
}

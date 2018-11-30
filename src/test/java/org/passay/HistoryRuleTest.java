/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
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
  private final List<PasswordData.Reference> history = new ArrayList<>();

  /** For testing. */
  private final HistoryRule rule = new HistoryRule();

  /** For testing. */
  private final HistoryRule ruleReportFirst = new HistoryRule(false);

  /** For testing. */
  private final HistoryRule ruleSizeToReport = new HistoryRule();

    /** For testing. */
  private final HistoryRule emptyRule = new HistoryRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    history.add(new PasswordData.HistoricalReference("history", "t3stUs3r01"));
    history.add(new PasswordData.HistoricalReference("history", "t3stUs3r02"));
    history.add(new PasswordData.HistoricalReference("history", "t3stUs3r03"));
    history.add(new PasswordData.HistoricalReference("history", "t3stUs3r02"));
    ruleSizeToReport.setSizeToReport(100);
  }


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

        {rule, TestUtils.newPasswordData("t3stUs3r00", "testuser", null, history), null, },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r01", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r02", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE, HistoryRule.ERROR_CODE),
        },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r03", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },

        {ruleReportFirst, TestUtils.newPasswordData("t3stUs3r00", "testuser", null, history), null, },
        {
          ruleReportFirst,
          TestUtils.newPasswordData("t3stUs3r01", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          ruleReportFirst,
          TestUtils.newPasswordData("t3stUs3r02", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          ruleReportFirst,
          TestUtils.newPasswordData("t3stUs3r03", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },

        {
          ruleSizeToReport,
          TestUtils.newPasswordData("t3stUs3r03", "testuser", null, history),
          codes(HistoryRule.ERROR_CODE),
        },

        {emptyRule, TestUtils.newPasswordData("t3stUs3r00", "testuser"), null, },
        {emptyRule, TestUtils.newPasswordData("t3stUs3r01", "testuser"), null, },
        {emptyRule, TestUtils.newPasswordData("t3stUs3r02", "testuser"), null, },
        {emptyRule, TestUtils.newPasswordData("t3stUs3r03", "testuser"), null, },
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
          rule,
          TestUtils.newPasswordData("t3stUs3r01", "testuser", null, history),
          new String[] {String.format("Password matches one of %s previous passwords.", history.size()), },
        },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r02", "testuser", null, history),
          new String[] {
            String.format("Password matches one of %s previous passwords.", history.size()),
            String.format("Password matches one of %s previous passwords.", history.size()),
          },
        },
        {
          ruleReportFirst,
          TestUtils.newPasswordData("t3stUs3r02", "testuser", null, history),
          new String[] {String.format("Password matches one of %s previous passwords.", history.size()), },
        },
        {
          ruleSizeToReport,
          TestUtils.newPasswordData("t3stUs3r01", "testuser", null, history),
          new String[] {String.format("Password matches one of 100 previous passwords."), },
        },
      };
  }
}

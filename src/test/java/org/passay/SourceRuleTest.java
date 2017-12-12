/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link SourceRule}.
 *
 * @author  Middleware Services
 */
public class SourceRuleTest extends AbstractRuleTest
{

  /** For testing. */
  private final List<PasswordData.Reference> sources = new ArrayList<>();

  /** For testing. */
  private final SourceRule rule = new SourceRule();

  /** For testing. */
  private final SourceRule ruleReportAll = new SourceRule(true);

  /** For testing. */
  private final SourceRule emptyRule = new SourceRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    sources.add(new PasswordData.SourceReference("System A", "t3stUs3r04"));
    sources.add(new PasswordData.SourceReference("System A", "t3stUs3r05"));
    sources.add(new PasswordData.SourceReference("System A", "t3stUs3r05"));
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

        {rule, TestUtils.newPasswordData("t3stUs3r01", "testuser", null, sources), null, },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r04", "testuser", null, sources),
          codes(SourceRule.ERROR_CODE),
        },
        {
          rule,
          TestUtils.newPasswordData("t3stUs3r05", "testuser", null, sources),
          codes(SourceRule.ERROR_CODE),
        },

        {ruleReportAll, TestUtils.newPasswordData("t3stUs3r01", "testuser", null, sources), null, },
        {
          ruleReportAll,
          TestUtils.newPasswordData("t3stUs3r04", "testuser", null, sources),
          codes(SourceRule.ERROR_CODE),
        },
        {
          ruleReportAll,
          TestUtils.newPasswordData("t3stUs3r05", "testuser", null, sources),
          codes(SourceRule.ERROR_CODE, SourceRule.ERROR_CODE),
        },

        {emptyRule, TestUtils.newPasswordData("t3stUs3r01", "testuser"), null, },
        {emptyRule, TestUtils.newPasswordData("t3stUs3r04", "testuser"), null, },
        {emptyRule, TestUtils.newPasswordData("t3stUs3r05", "testuser"), null, },
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
          TestUtils.newPasswordData("t3stUs3r04", "testuser", null, sources),
          new String[] {String.format("Password cannot be the same as your %s password.", "System A"), },
        },
        {
          ruleReportAll,
          TestUtils.newPasswordData("t3stUs3r05", "testuser", null, sources),
          new String[] {
            String.format("Password cannot be the same as your %s password.", "System A"),
            String.format("Password cannot be the same as your %s password.", "System A"),
          },
        },
      };
  }
}

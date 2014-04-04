/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link HistoryRule}.
 *
 * @author  Middleware Services
 */
public class HistoryRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final Password VALID_PASS = new Password("t3stUs3r00");

  /** Test password. */
  private static final Password HISTORY_PASS1 = new Password("t3stUs3r01");

  /** Test password. */
  private static final Password HISTORY_PASS2 = new Password("t3stUs3r02");

  /** Test password. */
  private static final Password HISTORY_PASS3 = new Password("t3stUs3r03");

  /** Test username. */
  private static final String USER = "testuser";

  /** For testing. */
  private final List<PasswordData.Reference> history = new ArrayList<>();

  /** For testing. */
  private final HistoryRule rule = new HistoryRule();

  /** For testing. */
  private final HistoryRule emptyRule = new HistoryRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    history.add(new PasswordData.HistoricalReference(
      "history", HISTORY_PASS1.getText()));
    history.add(new PasswordData.HistoricalReference(
      "history", HISTORY_PASS2.getText()));
    history.add(new PasswordData.HistoricalReference(
      "history", HISTORY_PASS3.getText()));
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

        {
          rule,
          PasswordData.newInstance(VALID_PASS, USER, history),
          null,
        },
        {
          rule,
          PasswordData.newInstance(HISTORY_PASS1, USER, history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          rule,
          PasswordData.newInstance(HISTORY_PASS2, USER, history),
          codes(HistoryRule.ERROR_CODE),
        },
        {
          rule,
          PasswordData.newInstance(HISTORY_PASS3, USER, history),
          codes(HistoryRule.ERROR_CODE),
        },

        {
          emptyRule,
          PasswordData.newInstance(VALID_PASS, USER, null),
          null,
        },
        {
          emptyRule,
          PasswordData.newInstance(HISTORY_PASS1, USER, null),
          null,
        },
        {
          emptyRule,
          PasswordData.newInstance(HISTORY_PASS2, USER, null),
          null,
        },
        {
          emptyRule,
          PasswordData.newInstance(HISTORY_PASS3, USER, null),
          null,
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final RuleResult result = rule.validate(
      PasswordData.newInstance(HISTORY_PASS1, USER, history));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format(
          "Password matches one of %s previous passwords.",
          history.size()),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

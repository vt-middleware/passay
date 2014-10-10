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

  /** Test password. */
  private static final String VALID_PASS = "t3stUs3r00";

  /** Test password. */
  private static final String HISTORY_PASS1 = "t3stUs3r01";

  /** Test password. */
  private static final String HISTORY_PASS2 = "t3stUs3r02";

  /** Test password. */
  private static final String HISTORY_PASS3 = "t3stUs3r03";

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
    history.add(new PasswordData.HistoricalReference("history", HISTORY_PASS1));
    history.add(new PasswordData.HistoricalReference("history", HISTORY_PASS2));
    history.add(new PasswordData.HistoricalReference("history", HISTORY_PASS3));
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
          PasswordData.newInstance(HISTORY_PASS1, USER, history),
          new String[] {
            String.format(
              "Password matches one of %s previous passwords.",
              history.size()),
          },
        },
      };
  }
}

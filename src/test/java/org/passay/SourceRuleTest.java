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

  /** Test password. */
  private static final String VALID_PASS = "t3stUs3r01";

  /** Test password. */
  private static final String SOURCE_PASS = "t3stUs3r04";

  /** Test username. */
  private static final String USER = "testuser";

  /** For testing. */
  private final List<PasswordData.Reference> sources = new ArrayList<>();

  /** For testing. */
  private final SourceRule rule = new SourceRule();

  /** For testing. */
  private final SourceRule emptyRule = new SourceRule();


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    sources.add(new PasswordData.SourceReference("System A", "t3stUs3r04"));
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
          PasswordData.newInstance(VALID_PASS, USER, null, sources),
          null,
        },
        {
          rule,
          PasswordData.newInstance(SOURCE_PASS, USER, null, sources),
          codes(SourceRule.ERROR_CODE),
        },

        {
          emptyRule,
          PasswordData.newInstance(VALID_PASS, USER),
          null,
        },
        {
          emptyRule,
          PasswordData.newInstance(SOURCE_PASS, USER),
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
          PasswordData.newInstance(SOURCE_PASS, USER, null, sources),
          new String[] {String.format("Password cannot be the same as your %s password.", "System A"), },
        },
      };
  }
}

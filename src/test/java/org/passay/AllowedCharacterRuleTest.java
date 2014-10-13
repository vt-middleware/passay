/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link AllowedCharacterRule}.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "boepselwezz";

  /** Test password. */
  private static final String INVALID_PASS = "gbwersco4kk";

  /** For testing. */
  private final AllowedCharacterRule rule = new AllowedCharacterRule(
    new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', });


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

        {rule, new PasswordData(VALID_PASS), null, },
        {
          rule,
          new PasswordData(INVALID_PASS),
          codes(AllowedCharacterRule.ERROR_CODE),
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
          new PasswordData(INVALID_PASS),
          new String[] {
            String.format("Password contains the illegal character '%s'.", "4"),
          },
        },
      };
  }
}

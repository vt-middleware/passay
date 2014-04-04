/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Properties;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Base class for all rule tests.
 *
 * @author  Middleware Services
 */
public abstract class AbstractRuleTest
{

  /** default message resolver. */
  protected static final MessageResolver DEFAULT_RESOLVER =
    new MessageResolver();

  /** empty message resolver. */
  protected static final MessageResolver EMPTY_RESOLVER = new MessageResolver(
    new Properties());


  /**
   * @param  rule  to check password with
   * @param  passwordData  to check
   * @param  errorCodes  Array of error codes to be produced on a failed
   * password validation attempt. A null value indicates that password
   * validation should succeed.
   *
   * @throws  Exception  On test failure.
   */
  @Test(
    groups = {"passtest"},
    dataProvider = "passwords"
  )
  public void checkPassword(
    final Rule rule,
    final PasswordData passwordData,
    final String[] errorCodes)
    throws Exception
  {
    final RuleResult result = rule.validate(passwordData);
    if (errorCodes != null) {
      AssertJUnit.assertFalse(result.isValid());
      AssertJUnit.assertEquals(errorCodes.length, result.getDetails().size());
      for (String code : errorCodes) {
        AssertJUnit.assertTrue(hasErrorCode(code, result));
      }
    } else {
      AssertJUnit.assertTrue(result.isValid());
    }
  }


  /**
   * Converts one or more error codes to a string array.
   *
   * @param  codes  One or more error codes.
   *
   * @return  Array of error codes.
   */
  protected static String[] codes(final String... codes)
  {
    return codes;
  }


  /**
   * Determines whether the given error code is found among the details of the
   * give rule validation result.
   *
   * @param  code  to search for in result details.
   * @param  result  to search for given code.
   *
   * @return  True if code is found among result details, false otherwise.
   */
  protected static boolean hasErrorCode(
    final String code,
    final RuleResult result)
  {
    for (RuleResultDetail detail : result.getDetails()) {
      if (code.equals(detail.getErrorCode())) {
        return true;
      }
    }
    return false;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.Properties;
import java.util.ResourceBundle;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.resolver.AbstractMessageResolver;
import org.passay.resolver.MessageResolver;
import org.passay.resolver.PropertiesMessageResolver;
import org.passay.resolver.ResourceBundleMessageResolver;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Base class for all rule tests.
 *
 * @author  Middleware Services
 */
public abstract class AbstractRuleTest
{

  /** test message resolver. */
  protected static final MessageResolver TEST_RESOLVER = new ResourceBundleMessageResolver(
    ResourceBundle.getBundle("passay-test"));

  /** empty message resolver. */
  protected static final MessageResolver EMPTY_RESOLVER = new PropertiesMessageResolver(new Properties());


  /**
   * @param  rule  to check password with
   * @param  passwordData  to check
   * @param  errorCodes  Array of error codes to be produced on a failed password validation attempt. A null value
   *                     indicates that password validation should succeed.
   */
  @Test(groups = "passtest", dataProvider = "passwords")
  public void checkPassword(final Rule rule, final PasswordData passwordData, final String[] errorCodes)
  {
    final RuleResult result = rule.validate(passwordData);
    if (errorCodes != null) {
      assertThat(result.isValid()).isFalse();
      assertThat(result.getDetails().size()).isEqualTo(errorCodes.length);
      for (String code : errorCodes) {
        assertThat(hasErrorCode(code, result)).isTrue();
      }
    } else {
      assertThat(result.isValid()).isTrue();
    }
  }


  /**
   * @param  rule  to check password with
   * @param  passwordData  to check
   * @param  messages  Array of messages to be produced on a failed password validation attempt
   */
  @Test(groups = "passtest", dataProvider = "messages")
  public void checkMessage(final Rule rule, final PasswordData passwordData, final String[] messages)
  {
    final RuleResult result = rule.validate(passwordData);
    assertThat(result.isValid()).isFalse();
    assertThat(result.getDetails().size()).isEqualTo(messages.length);
    for (int i = 0; i < result.getDetails().size(); i++) {
      final RuleResultDetail detail = result.getDetails().get(i);
      assertThat(TEST_RESOLVER.resolve(detail)).isEqualTo(messages[i]);
      assertThat(EMPTY_RESOLVER.resolve(detail)).isNotNull();
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
   * Determines whether the given error code is found among the details of the give rule validation result.
   *
   * @param  code  to search for in result details.
   * @param  result  to search for given code.
   *
   * @return  True if code is found among result details, false otherwise.
   */
  protected static boolean hasErrorCode(final String code, final RuleResult result)
  {
    for (RuleResultDetail detail : result.getDetails()) {
      if (code.equals(detail.getErrorCode())) {
        return true;
      }
    }
    return false;
  }


  /** Message resolver for testing. */
  public static class TestMessageResolver extends AbstractMessageResolver
  {

    /** Properties for this resolver. */
    private final Properties props = new Properties();


    /**
     * Creates a new test message resolver.
     *
     * @param  key  message key
     * @param  value  message value
     */
    public TestMessageResolver(final String key, final String value)
    {
      props.setProperty(key, value);
    }


    @Override
    protected String getMessage(final String key)
    {
      return props.getProperty(key);
    }
  }
}

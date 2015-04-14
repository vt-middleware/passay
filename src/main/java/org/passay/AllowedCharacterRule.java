/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Rule for determining if a password contains allowed characters. Validation will fail unless the password contains all
 * of the allowed characters.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRule implements Rule
{

  /** Error code for allowed character failures. */
  public static final String ERROR_CODE = "ALLOWED_CHAR";

  /** Stores the characters that are allowed. */
  private final char[] allowedChar;


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   */
  public AllowedCharacterRule(final char[] c)
  {
    allowedChar = c;
    Arrays.sort(allowedChar);
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);

    for (char c : passwordData.getPassword().toCharArray()) {
      if (Arrays.binarySearch(allowedChar, c) < 0) {
        result.setValid(false);
        result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(c)));
        break;
      }
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  c  illegal character
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final char c)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("illegalCharacter", c);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::allowedChar=%s",
        getClass().getName(),
        hashCode(),
        allowedChar != null ? Arrays.toString(allowedChar) : null);
  }
}

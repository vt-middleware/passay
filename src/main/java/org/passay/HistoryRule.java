/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Rule for determining if a password matches one of any previous password a
 * user has chosen. If no historical password reference has been set, then
 * passwords will meet this rule. See {@link
 * PasswordData#setPasswordReferences(List)}.
 *
 * @author  Middleware Services
 */
public class HistoryRule implements Rule
{

  /** Error code for history violation. */
  public static final String ERROR_CODE = "HISTORY_VIOLATION";


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final List<PasswordData.HistoricalReference> references =
      passwordData.getPasswordReferences(
        PasswordData.HistoricalReference.class);
    final int size = references.size();
    if (size == 0) {
      return result;
    }

    final String cleartext = passwordData.getPassword();
    for (PasswordData.HistoricalReference reference : references) {
      if (matches(cleartext, reference)) {
        result.setValid(false);
        result.getDetails().add(
          new RuleResultDetail(
            ERROR_CODE,
            createRuleResultDetailParameters(size)));
      }
    }
    return result;
  }


  /**
   * Determines whether a password matches an historical password.
   *
   * @param  password  candidate password
   * @param  reference  reference password
   *
   * @return  true if passwords match, false otherwise.
   */
  protected boolean matches(
    final String password,
    final PasswordData.Reference reference)
  {
    return password.equals(reference.getPassword());
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  size  of the history list
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int size)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("historySize", size);
    return m;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SuccessRuleResult;

/**
 * Rule for determining if a password matches one of any previous password a user has chosen. If no historical password
 * reference has been set, then passwords will meet this rule. See {@link PasswordData#getPasswordReferences()}.
 *
 * @author  Middleware Services
 */
public class HistoryRule implements Rule
{

  /** Error code for history violation. */
  public static final String ERROR_CODE = "HISTORY_VIOLATION";

  /** Whether to report all history matches or just the first. */
  protected final boolean reportAllFailures;


  /**
   * Creates a new history rule.
   */
  public HistoryRule()
  {
    this(true);
  }


  /**
   * Creates a new history rule.
   *
   * @param  reportAll  whether to report all matches or just the first
   */
  public HistoryRule(final boolean reportAll)
  {
    reportAllFailures = reportAll;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<PasswordData.HistoricalReference> references = passwordData.getPasswordReferences(
      PasswordData.HistoricalReference.class);
    final int size = references.size();
    if (size == 0) {
      return new SuccessRuleResult();
    }

    final List<RuleResultDetail> details = new ArrayList<>();
    final String cleartext = passwordData.getPassword();
    if (reportAllFailures) {
      references.stream()
        .filter(r -> matches(cleartext, r))
        .forEach(r -> details.add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(size))));
    } else {
      references.stream()
        .filter(r -> matches(cleartext, r))
        .findFirst()
        .ifPresent(r -> details.add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(size))));
    }
    return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
  }


  /**
   * Determines whether a password matches an historical password.
   *
   * @param  password  candidate password
   * @param  reference  reference password
   *
   * @return  true if passwords match, false otherwise.
   */
  protected boolean matches(final String password, final PasswordData.Reference reference)
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

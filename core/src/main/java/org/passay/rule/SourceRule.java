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
import org.passay.UnicodeString;
import org.passay.support.Reference;
import org.passay.support.SourceReference;

/**
 * Rule for determining if a password matches a password from a different source. Useful for when separate systems
 * cannot have matching passwords. If no source password reference has been set, then passwords will meet this rule. See
 * {@link PasswordData#getPasswordReferences()}.
 *
 * @author  Middleware Services
 */
public class SourceRule implements Rule
{

  /** Error code for regex validation failures. */
  public static final String ERROR_CODE = "SOURCE_VIOLATION";

  /** Whether to report all source matches or just the first. */
  protected final boolean reportAllFailures;


  /**
   * Creates a new source rule.
   */
  public SourceRule()
  {
    this(true);
  }


  /**
   * Creates a new source rule.
   *
   * @param  reportAll  whether to report all matches or just the first
   */
  public SourceRule(final boolean reportAll)
  {
    reportAllFailures = reportAll;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<SourceReference> references = passwordData.getPasswordReferences(SourceReference.class);
    if (references.isEmpty()) {
      return new SuccessRuleResult();
    }

    final List<RuleResultDetail> details = new ArrayList<>();
    final UnicodeString cleartext = passwordData.getPassword();
    if (reportAllFailures) {
      references.stream()
        .filter(r -> matches(cleartext, r))
        .forEach(r -> details.add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(r.getLabel()))));
    } else {
      references.stream()
        .filter(r -> matches(cleartext, r))
        .findFirst()
        .ifPresent(r -> details.add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(r.getLabel()))));
    }
    return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
  }


  /**
   * Determines whether a password matches a source password.
   *
   * @param  password  candidate password
   * @param  reference  reference password
   *
   * @return  true if passwords match, false otherwise.
   */
  protected boolean matches(final UnicodeString password, final Reference reference)
  {
    return password.equals(reference.getPassword());
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  source  matching source
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String source)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("source", source);
    return m;
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::reportAllFailures=" + reportAllFailures;
  }
}

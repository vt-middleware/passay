/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;

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
    final RuleResult result = new RuleResult();
    final List<PasswordData.SourceReference> references = passwordData.getPasswordReferences(
      PasswordData.SourceReference.class);
    if (references.isEmpty()) {
      return result;
    }

    final String cleartext = passwordData.getPassword();
    if (reportAllFailures) {
      references.stream().filter(reference -> matches(cleartext, reference)).forEach(
        reference -> result.addError(ERROR_CODE, createRuleResultDetailParameters(reference.getLabel())));
    } else {
      references.stream().filter(reference -> matches(cleartext, reference)).findFirst().ifPresent(
        reference -> result.addError(ERROR_CODE, createRuleResultDetailParameters(reference.getLabel())));
    }
    return result;
  }


  /**
   * Determines whether a password matches a source password.
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
}

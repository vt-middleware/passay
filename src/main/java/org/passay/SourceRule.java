/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Rule for determining if a password matches a password from a different
 * source. Useful for when separate systems cannot have matching passwords. If
 * no source password reference has been set, then passwords will meet this
 * rule. See {@link PasswordData#setPasswordReferences(List)}
 *
 * @author  Middleware Services
 */
public class SourceRule implements Rule
{

  /** Error code for regex validation failures. */
  public static final String ERROR_CODE = "SOURCE_VIOLATION";


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final List<PasswordData.SourceReference> references =
      passwordData.getPasswordReferences(PasswordData.SourceReference.class);
    if (references.isEmpty()) {
      return result;
    }

    final String cleartext = passwordData.getPassword();
    for (PasswordData.SourceReference reference : references) {
      if (matches(cleartext, reference)) {
        result.setValid(false);
        result.getDetails().add(
          new RuleResultDetail(
            ERROR_CODE,
            createRuleResultDetailParameters(reference.getLabel())));
      }
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
  protected boolean matches(
    final String password,
    final PasswordData.Reference reference)
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
  protected Map<String, Object> createRuleResultDetailParameters(
    final String source)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("source", source);
    return m;
  }
}

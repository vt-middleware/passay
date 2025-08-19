/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;

/**
 * {@link Rule} that is composed of other rules.
 *
 * @author  Middleware Services
 */
public class CompositeRule implements Rule
{

  /** Password rules. */
  private final List<Rule> passwordRules = new ArrayList<>();


  /**
   * See {@link #CompositeRule(List)}.
   *
   * @param  rules  to validate
   */
  public CompositeRule(final Rule... rules)
  {
    this(Arrays.asList(rules));
  }


  /**
   * Creates a new composite rule.
   *
   * @param  rules  to validate
   */
  public CompositeRule(final List<? extends Rule> rules)
  {
    passwordRules.addAll(
      PassayUtils.assertNotNullArgOr(
        rules,
        v -> v.stream().anyMatch(Objects::isNull),
        "Password rules cannot be null or contain null"));
  }


  /**
   * Validates the supplied password data against the rules in this composite rule. The method fails validation if any
   * underlying rule fails.
   *
   * @param  passwordData  to validate
   *
   * @return  rule result
   */
  public RuleResult validate(final PasswordData passwordData)
  {
    boolean success = true;
    final List<RuleResultDetail> details = new ArrayList<>();
    final List<RuleResultMetadata> metadata = new ArrayList<>();
    for (Rule rule : passwordRules) {
      final RuleResult result = rule.validate(passwordData);
      if (success && !result.isValid()) {
        success = false;
      }
      details.addAll(result.getDetails());
      metadata.add(result.getMetadata());
    }
    return success ?
      new SuccessRuleResult(new RuleResultMetadata(metadata)) :
      new FailureRuleResult(new RuleResultMetadata(metadata), details);
  }
}

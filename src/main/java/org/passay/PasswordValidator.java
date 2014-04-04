/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;

/**
 * The central component for evaluating multiple password rules against a
 * candidate password.
 *
 * @author  Middleware Services
 */

public class PasswordValidator implements Rule
{

  /** Password rules. */
  private final List<Rule> passwordRules;

  /** Message resolver. */
  private final MessageResolver messageResolver;


  /**
   * Creates a new password validator with the default message resolver.
   *
   * @param  rules  to validate
   */
  public PasswordValidator(final List<Rule> rules)
  {
    this(new MessageResolver(), rules);
  }


  /**
   * Creates a new password validator.
   *
   * @param  resolver  message resolver.
   * @param  rules  to validate
   */
  public PasswordValidator(
    final MessageResolver resolver,
    final List<Rule> rules)
  {
    messageResolver = resolver;
    passwordRules = rules;
  }


  /**
   * Validates the supplied password data against the rules in this validator.
   *
   * @param  passwordData  to validate
   *
   * @return  rule result
   */
  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    for (Rule rule : passwordRules) {
      final RuleResult rr = rule.validate(passwordData);
      if (!rr.isValid()) {
        result.setValid(false);
        result.getDetails().addAll(rr.getDetails());
      }
    }
    return result;
  }


  /**
   * Returns a list of human-readable messages by iterating over the details in
   * a failed rule result.
   *
   * @param  result  failed rule result.
   *
   * @return  list of human-readable messages describing the reason(s) for
   * validation failure.
   */
  public List<String> getMessages(final RuleResult result)
  {
    final List<String> messages = new ArrayList<>();
    for (RuleResultDetail detail : result.getDetails()) {
      messages.add(messageResolver.resolve(detail));
    }
    return messages;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::passwordRules=%s,messageResolver=%s",
        getClass().getName(),
        hashCode(),
        passwordRules,
        messageResolver);
  }
}

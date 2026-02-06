/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.List;
import java.util.function.BiFunction;
import org.passay.entropy.Entropy;
import org.passay.resolver.MessageResolver;
import org.passay.rule.Rule;

/**
 * Interface for password validators.
 *
 * @author  Middleware Services
 */
public interface PasswordValidator
{


  /**
   * Returns the password rules for this validator.
   *
   * @return  unmodifiable list of password rules
   */
  List<? extends Rule> getRules();


  /**
   * Returns the message resolver for this validator.
   *
   * @return  message resolver
   */
  MessageResolver getMessageResolver();


  /**
   * Returns the entropy provider for this validator.
   *
   * @return  entropy provider
   */
  BiFunction<List<? extends Rule>, PasswordData, Entropy> getEntropyProvider();


  /**
   * Validates the supplied password data against the rules in this validator.
   *
   * @param  passwordData  to validate
   *
   * @return  validation result
   */
  ValidationResult validate(PasswordData passwordData);
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;


import java.util.List;

/**
 * Result of a password validator.
 *
 * @author  Middleware Services
 */
public interface ValidationResult
{


  /**
   * Returns whether the result of the rule verification is a valid password.
   *
   * @return  valid password for this rule
   */
  boolean isValid();


  /**
   * Returns metadata associated with the rule verification.
   *
   * @return  rule result metadata
   */
  RuleResultMetadata getMetadata();


  /**
   * Returns any details associated with the rule verification.
   *
   * @return  rule result details
   */
  List<RuleResultDetail> getDetails();


  /**
   * Returns a list of human-readable messages by iterating over the details in a failed rule result.
   *
   * @return  list of human-readable messages describing the reason(s) for validation failure.
   */
  List<String> getMessages();


  /**
   * Returns the calculated entropy of the {@link PasswordData} used to produce this validation result, based on the
   * password rules specified.
   *
   * @return  entropy estimate
   */
  double getEntropy();
}

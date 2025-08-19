/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.List;

/**
 * Result of a password rule validation.
 *
 * @author  Middleware Services
 */
public interface RuleResult
{


  /**
   * Returns whether the result of the rule verification is a valid password.
   *
   * @return  valid password for this rule
   */
  boolean isValid();


  /**
   * Returns any details associated with the rule verification.
   *
   * @return  rule result details
   */
  List<RuleResultDetail> getDetails();


  /**
   * Returns metadata associated with the rule verification.
   *
   * @return  rule result metadata
   */
  RuleResultMetadata getMetadata();
}

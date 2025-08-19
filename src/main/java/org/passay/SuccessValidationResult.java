/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Result of a password validator success.
 *
 * @author  Middleware Services
 */
public final class SuccessValidationResult extends AbstractValidationResult
{


  /**
   * Creates a new success validation result.
   */
  public SuccessValidationResult()
  {
    super(true, new RuleResultMetadata());
  }


  /**
   * Creates a new success validation result.
   *
   * @param  metadata  metadata associated by the rule with the password
   */
  public SuccessValidationResult(final RuleResultMetadata metadata)
  {
    super(true, metadata);
  }


  /**
   * Creates a new success validation result.
   *
   * @param  entropy  calculation
   */
  public SuccessValidationResult(final double entropy)
  {
    super(true, entropy, new RuleResultMetadata());
  }


  /**
   * Creates a new success validation result.
   *
   * @param  entropy  calculation
   * @param  metadata  metadata associated by the rule with the password
   */
  public SuccessValidationResult(final double entropy, final RuleResultMetadata metadata)
  {
    super(true, entropy, metadata);
  }
}

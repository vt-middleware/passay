/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Result of a password rule success.
 *
 * @author  Middleware Services
 */
public final class SuccessRuleResult extends AbstractRuleResult
{

  /**
   * Creates a new success rule result.
   */
  public SuccessRuleResult()
  {
    super(true, new RuleResultMetadata());
  }


  /**
   * Creates a new success rule result.
   *
   * @param  metadata  metadata associated by the rule with the password
   */
  public SuccessRuleResult(final RuleResultMetadata metadata)
  {
    super(true, metadata);
  }
}

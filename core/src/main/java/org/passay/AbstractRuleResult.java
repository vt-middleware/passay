/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.List;

/**
 * Base class for rule results.
 *
 * @author  Middleware Services
 */
public abstract class AbstractRuleResult implements RuleResult
{

  /** Whether password rule was successful. */
  private final boolean valid;

  /** Metadata produced by a password rule. */
  private final RuleResultMetadata metadata;


  /**
   * Creates a new abstract rule result.
   *
   * @param  valid  result validity
   * @param  metadata  associated by the rule with the password
   */
  public AbstractRuleResult(final boolean valid, final RuleResultMetadata metadata)
  {
    this.valid = valid;
    this.metadata = PassayUtils.assertNotNullArg(metadata, "Metadata cannot be null");
  }


  @Override
  public boolean isValid()
  {
    return valid;
  }


  @Override
  public RuleResultMetadata getMetadata()
  {
    return metadata;
  }


  @Override
  public List<RuleResultDetail> getDetails()
  {
    return Collections.emptyList();
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "valid=" + isValid() + ", " +
      "details=" + getDetails() + ", " +
      "metadata=" + getMetadata();
  }
}

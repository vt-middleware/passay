/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.List;

/**
 * Result of a password rule validation. This implementation supports a single {@link RuleResultDetail} per result. A
 * valid result cannot have a {@link RuleResultDetail} but can have {@link RuleResultMetadata}.
 *
 * @author  Middleware Services
 */
public final class DefaultRuleResult implements RuleResult
{

  /** Whether password rule was successful. */
  private final boolean valid;

  /** Details associated with a password rule failure. */
  private final RuleResultDetail detail;

  /** Metadata produced by a password rule. */
  private final RuleResultMetadata metadata;


  /**
   * Creates a new default rule result.
   *
   * @param  valid  result validity
   */
  public DefaultRuleResult(final boolean valid)
  {
    this.valid = valid;
    this.detail = null;
    this.metadata = new RuleResultMetadata();
  }


  /**
   * Creates a new default rule result.
   *
   * @param  detail  details associated with this result
   */
  public DefaultRuleResult(final RuleResultDetail detail)
  {
    this.valid = false;
    this.detail = PassayUtils.assertNotNullArg(detail, "Detail cannot be null");
    this.metadata = new RuleResultMetadata();
  }


  /**
   * Creates a new default rule result.
   *
   * @param  valid  result validity
   * @param  metadata  metadata associated by the rule with the password
   */
  public DefaultRuleResult(final boolean valid, final RuleResultMetadata metadata)
  {
    this.valid = valid;
    this.detail = null;
    this.metadata = PassayUtils.assertNotNullArg(metadata, "Metadata cannot be null");
  }


  /**
   * Creates a new default rule result.
   *
   * @param  detail  associated with this result
   * @param  metadata  associated by the rule with the password
   */
  public DefaultRuleResult(final RuleResultDetail detail, final RuleResultMetadata metadata)
  {
    this.valid = false;
    this.detail = PassayUtils.assertNotNullArg(detail, "Detail cannot be null");
    this.metadata = PassayUtils.assertNotNullArg(metadata, "Metadata cannot be null");
  }


  @Override
  public boolean isValid()
  {
    return valid;
  }


  @Override
  public List<RuleResultDetail> getDetails()
  {
    return detail != null ? Collections.singletonList(detail) : Collections.emptyList();
  }


  @Override
  public RuleResultMetadata getMetadata()
  {
    return metadata;
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

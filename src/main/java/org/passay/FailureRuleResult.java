/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Result of a password rule failure.
 *
 * @author  Middleware Services
 */
public final class FailureRuleResult extends AbstractRuleResult
{

  /** Details associated with a password rule failure. */
  private final List<RuleResultDetail> details = new ArrayList<>();



  /**
   * Creates a new failure rule result.
   */
  public FailureRuleResult()
  {
    super(false, new RuleResultMetadata());
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  detail  associated with this result
   */
  public FailureRuleResult(final RuleResultDetail detail)
  {
    super(false, new RuleResultMetadata());
    this.details.add(PassayUtils.assertNotNullArg(detail, "Detail cannot be null"));
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  details  associated with this result
   */
  public FailureRuleResult(final RuleResultDetail... details)
  {
    this(Arrays.asList(details));
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  details  associated with this result
   */
  public FailureRuleResult(final List<RuleResultDetail> details)
  {
    super(false, new RuleResultMetadata());
    this.details.addAll(
      PassayUtils.assertNotNullArgOr(details, v -> v.stream().anyMatch(Objects::isNull), "Details cannot be null"));
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  metadata  associated by the rule with the password
   */
  public FailureRuleResult(final RuleResultMetadata metadata)
  {
    super(false, metadata);
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  metadata  associated by the rule with the password
   * @param  detail  associated with this result
   */
  public FailureRuleResult(final RuleResultMetadata metadata, final RuleResultDetail detail)
  {
    super(false, metadata);
    this.details.add(PassayUtils.assertNotNullArg(detail, "Detail cannot be null"));
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  details  associated with this result
   * @param  metadata  associated by the rule with the password
   */
  public FailureRuleResult(final RuleResultMetadata metadata, final RuleResultDetail... details)
  {
    this(metadata, Arrays.asList(details));
  }


  /**
   * Creates a new failure rule result.
   *
   * @param  metadata  associated by the rule with the password
   * @param  details  associated with this result
   */
  public FailureRuleResult(final RuleResultMetadata metadata, final List<RuleResultDetail> details)
  {
    super(false, metadata);
    this.details.addAll(
      PassayUtils.assertNotNullArgOr(details, v -> v.stream().anyMatch(Objects::isNull), "Details cannot be null"));
  }


  @Override
  public List<RuleResultDetail> getDetails()
  {
    return Collections.unmodifiableList(details);
  }
}

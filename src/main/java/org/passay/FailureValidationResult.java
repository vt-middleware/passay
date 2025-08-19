/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of a password validator failure.
 *
 * @author  Middleware Services
 */
public final class FailureValidationResult extends AbstractValidationResult
{

  /** Details associated with a password rule failure. */
  private final List<RuleResultDetail> details = new ArrayList<>();

  /** Messages produced by a message resolver. */
  private final List<String> messages = new ArrayList<>();


  /**
   * Creates a new failure validation result.
   */
  public FailureValidationResult()
  {
    super(false, new RuleResultMetadata());
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  entropy  calculation
   */
  public FailureValidationResult(final double entropy)
  {
    super(false, entropy, new RuleResultMetadata());
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  metadata  metadata associated by the rule with the password
   */
  public FailureValidationResult(final RuleResultMetadata metadata)
  {
    super(false, metadata);
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  entropy  calculation
   * @param  metadata  metadata associated by the rule with the password
   */
  public FailureValidationResult(final double entropy, final RuleResultMetadata metadata)
  {
    super(false, entropy, metadata);
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  metadata  metadata associated by the rule with the password
   * @param  details  associated with this result
   */
  public FailureValidationResult(final RuleResultMetadata metadata, final List<RuleResultDetail> details)
  {
    super(false, metadata);
    this.details.addAll(details);
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  entropy  calculation
   * @param  metadata  metadata associated by the rule with the password
   * @param  details  associated with this result
   */
  public FailureValidationResult(
    final double entropy, final RuleResultMetadata metadata, final List<RuleResultDetail> details)
  {
    super(false, entropy, metadata);
    this.details.addAll(details);
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  metadata  metadata associated by the rule with the password
   * @param  details  associated with this result
   * @param  messages  produced by the details
   */
  public FailureValidationResult(
    final RuleResultMetadata metadata,
    final List<RuleResultDetail> details,
    final List<String> messages)
  {
    super(false, metadata);
    this.details.addAll(details);
    this.messages.addAll(messages);
  }


  /**
   * Creates a new failure validation result.
   *
   * @param  entropy  calculation
   * @param  metadata  metadata associated by the rule with the password
   * @param  details  associated with this result
   * @param  messages  produced by the details
   */
  public FailureValidationResult(
    final double entropy,
    final RuleResultMetadata metadata,
    final List<RuleResultDetail> details,
    final List<String> messages)
  {
    super(false, entropy, metadata);
    this.details.addAll(details);
    this.messages.addAll(messages);
  }


  @Override
  public List<RuleResultDetail> getDetails()
  {
    return Collections.unmodifiableList(details);
  }


  @Override
  public List<String> getMessages()
  {
    return Collections.unmodifiableList(messages);
  }
}

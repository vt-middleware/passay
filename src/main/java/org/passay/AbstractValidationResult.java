/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.List;

/**
 * Base class for validation results.
 *
 * @author  Middleware Services
 */
public abstract class AbstractValidationResult implements ValidationResult
{

  /** Whether password rule was successful. */
  private final boolean valid;

  /** Entropy of the password used for validation. */
  private final double entropy;

  /** Metadata produced by a password rule. */
  private final RuleResultMetadata metadata;


  /**
   * Creates a new abstract validation result.
   *
   * @param  valid  whether validation was successful
   * @param  metadata  metadata associated by the rule with the password
   */
  public AbstractValidationResult(final boolean valid, final RuleResultMetadata metadata)
  {
    this.valid = valid;
    this.entropy = -1;
    this.metadata = metadata;
  }


  /**
   * Creates a new abstract validation result.
   *
   * @param  valid  whether validation was successful
   * @param  entropy  calculated entropy of the password
   * @param  metadata  metadata associated by the rule with the password
   */
  public AbstractValidationResult(final boolean valid, final double entropy, final RuleResultMetadata metadata)
  {
    this.valid = valid;
    this.entropy = entropy;
    this.metadata = metadata;
  }


  @Override
  public boolean isValid()
  {
    return valid;
  }


  @Override
  public double getEntropy()
  {
    return entropy;
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
  public List<String> getMessages()
  {
    return Collections.emptyList();
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Result of a password rule validation.
 *
 * @author  Middleware Services
 */
public final class RuleResult
{

  /** Details associated with a password rule result. */
  private final List<RuleResultDetail> details = new ArrayList<>();

  /** Whether password rule was successful. */
  private volatile boolean valid;

  /** Metadata produced by a password rule. */
  private RuleResultMetadata metadata = new RuleResultMetadata();

  /** Whether this object has been marked immutable. */
  private volatile boolean immutable;


  /**
   * Creates a new rule result with its validity set to true.
   */
  public RuleResult()
  {
    this(true);
  }


  /**
   * Creates a new rule result.
   *
   * @param  valid  result validity
   */
  public RuleResult(final boolean valid)
  {
    setValid(valid);
  }


  /**
   * Creates a new rule result.
   *
   * @param  valid  result validity
   * @param  detail  details associated with this result
   */
  public RuleResult(final boolean valid, final RuleResultDetail detail)
  {
    setValid(valid);
    details.add(detail);
  }


  /**
   * Creates a new rule result.
   *
   * @param  valid  result validity
   * @param  metadata  metadata associated by the rule with the password
   */
  public RuleResult(final boolean valid, final RuleResultMetadata metadata)
  {
    setValid(valid);
    setMetadata(metadata);
  }


  /**
   * Creates a new rule result.
   *
   * @param  valid  result validity
   * @param  detail  associated with this result
   * @param  metadata  associated by the rule with the password
   */
  public RuleResult(final boolean valid, final RuleResultDetail detail, final RuleResultMetadata metadata)
  {
    setValid(valid);
    details.add(detail);
    setMetadata(metadata);
  }


  /** Freezes this object, making it immutable. */
  public void freeze()
  {
    immutable = true;
  }


  /**
   * Determines whether this object is frozen, i.e. immutable.
   *
   * @return  true if {@link #freeze()} has been invoked, false otherwise.
   */
  public boolean isFrozen()
  {
    return immutable;
  }


  /**
   * Asserts that this object is in a state to permit mutations.
   *
   * @throws  IllegalStateException  if this object is frozen (i.e. immutable).
   */
  public void assertMutable()
  {
    if (immutable) {
      throw new IllegalStateException("Cannot modify immutable object");
    }
  }


  /**
   * Returns whether the result of the rule verification is a valid password.
   *
   * @return  valid password for this rule
   */
  public boolean isValid()
  {
    return valid;
  }


  /**
   * Sets whether the result of the rule verification is a valid password.
   *
   * @param  valid  whether the password is valid for this rule
   */
  public void setValid(final boolean valid)
  {
    assertMutable();
    this.valid = valid;
  }


  /**
   * Returns any details associated with the rule verification.
   *
   * @return  rule result details
   */
  public List<RuleResultDetail> getDetails()
  {
    return immutable ? Collections.unmodifiableList(details) : details;
  }


  /**
   * Adds a new rule result detail with the given error details and
   * sets the result of the rule verification to invalid.
   *
   * @param  code  error code
   * @param  params  error details
   */
  public void addError(final String code, final Map<String, Object> params)
  {
    assertMutable();
    setValid(false);
    details.add(new RuleResultDetail(code, params));
  }


  /**
   * Adds a new rule result detail under multiple error codes.
   *
   * @param  codes  error codes in order of most specific to least specific
   * @param  params  error details
   */
  public void addError(final String[] codes, final Map<String, Object> params)
  {
    assertMutable();
    setValid(false);
    details.add(new RuleResultDetail(codes, params));
  }


  /**
   * Returns metadata associated with the rule verification.
   *
   * @return  rule result metadata
   */
  public RuleResultMetadata getMetadata()
  {
    return metadata;
  }


  /**
   * Sets metadata associated with the rule verification.
   *
   * @param  metadata  rule result metadata
   */
  public void setMetadata(final RuleResultMetadata metadata)
  {
    assertMutable();
    this.metadata = metadata;
  }


  @Override
  public String toString()
  {
    return String.format(
      "%s@%h::valid=%s,details=%s,metadata=%s",
      getClass().getName(),
      hashCode(),
      valid,
      details,
      metadata);
  }
}

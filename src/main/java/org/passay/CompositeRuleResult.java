/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Result of more than one password rule validation. This implementation stores a list of {@link RuleResult} and
 * exposes {@link RuleResult} methods by examining all the results in the list.
 *
 * @author  Middleware Services
 */
public final class CompositeRuleResult implements RuleResult
{


  /** Details associated with password rule results. */
  private final List<RuleResult> results = new ArrayList<>();

  /** Function to determine validity of a list of rules. */
  private final Function<List<RuleResult>, Boolean> validityCondition;

  /** Predicate to determine which rule result details to expose. */
  private final Predicate<RuleResult> detailFilter;

  /** Predicate to determine which rule result metadata to expose. */
  private final Predicate<RuleResult> metadataFilter;


  /**
   * Creates a new composite rule result.
   *
   * @param results to store
   *
   * @throws IllegalArgumentException if results is null or empty
   */
  public CompositeRuleResult(final List<RuleResult> results)
  {
    this(results, rr -> rr.stream().allMatch(RuleResult::isValid), rr -> true, md -> true);
  }


  /**
   * Creates a new composite rule result.
   *
   * @param results to store
   * @param validityCondition to determine whether this result is valid
   * @param detailFilter to determine which details to expose
   * @param metadataFilter to determine which metadata to expose
   *
   * @throws IllegalArgumentException if results is null or empty
   */
  public CompositeRuleResult(
    final List<RuleResult> results,
    final Function<List<RuleResult>, Boolean> validityCondition,
    final Predicate<RuleResult> detailFilter,
    final Predicate<RuleResult> metadataFilter)
  {
    this.results.addAll(
      PassayUtils.assertNotNullArgOr(
        results, v -> v.isEmpty() || v.stream().anyMatch(Objects::isNull), "Results cannot be null or empty"));
    this.validityCondition = PassayUtils.assertNotNullArg(validityCondition, "Validity condition cannot be null");
    this.detailFilter = PassayUtils.assertNotNullArg(detailFilter, "Detail filter cannot be null");
    this.metadataFilter = PassayUtils.assertNotNullArg(metadataFilter, "Metadata filter cannot be null");
  }


  /**
   * Returns whether the rule results in this composite result are valid. The result of this method is determined by
   * {@link #validityCondition}. See {@link RuleResult#isValid()}.
   *
   * @return whether a password is valid based on multiple rule results
   */
  @Override
  public boolean isValid()
  {
    return validityCondition.apply(results);
  }


  /**
   * Returns any details associated with all rule results. Details are filtered by {@link #detailFilter}.
   *
   * @return rule result details
   */
  @Override
  public List<RuleResultDetail> getDetails()
  {
    final List<RuleResultDetail> l = results.stream()
      .filter(detailFilter)
      .map(RuleResult::getDetails)
      .filter(details -> Objects.nonNull(details) && !details.isEmpty())
      .flatMap(List::stream)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    return Collections.unmodifiableList(l);
  }


  /**
   * Returns metadata associated with all rule results. Note that for metadata conflicts, higher indexes take
   * precedence. See {@link RuleResultMetadata#RuleResultMetadata(List)}.
   *
   * @return rule result metadata
   */
  @Override
  public RuleResultMetadata getMetadata()
  {
    final List<RuleResultMetadata> l = results.stream()
      .filter(metadataFilter)
      .map(RuleResult::getMetadata)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    return new RuleResultMetadata(l);
  }


  @Override
  public String toString()
  {
    return String.format(
      "%s@%h::valid=%s,details=%s,metadata=%s",
      getClass().getName(),
      hashCode(),
      isValid(),
      getDetails(),
      getMetadata());
  }
}

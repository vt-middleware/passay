/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Describes metadata relevant to the result of rule validation.
 *
 * @author  Middleware Services
 */
public final class RuleResultMetadata
{

  /** Count category. */
  public enum CountCategory
  {
    /** password length. */
    Length,

    /** lowercase characters. */
    LowerCase,

    /** uppercase characters. */
    UpperCase,

    /** digit characters. */
    Digit,

    /** special characters. */
    Special,

    /** special ascii characters. */
    SpecialAscii,

    /** special unicode characters. */
    SpecialUnicode,

    /** special latin characters. */
    SpecialLatin,

    /** whitespace characters. */
    Whitespace,

    /** allowed characters. */
    Allowed,

    /** illegal characters. */
    Illegal,

    /** Already leaked password. */
    Pwned
  }

  /** Character count metadata. */
  private final Map<CountCategory, Integer> counts = new HashMap<>();


  /**
   * Creates a new rule result metadata.
   */
  public RuleResultMetadata() {}


  /**
   * Creates a new rule result metadata.
   *
   * @param  category  count category.
   * @param  value  count value.
   */
  public RuleResultMetadata(final CountCategory category, final int value)
  {
    PassayUtils.assertNotNullArg(category, "Category cannot be null");
    if (value < 0) {
      throw new IllegalArgumentException("Count value must be greater than or equal to zero");
    }
    counts.put(category, value);
  }


  /**
   * Creates a new rule result metadata.
   *
   * @param  metadata  to copy.
   */
  public RuleResultMetadata(final RuleResultMetadata metadata)
  {
    PassayUtils.assertNotNullArg(metadata, "Metadata cannot be null");
    counts.putAll(metadata.counts);
  }


  /**
   * Creates a new rule result metadata.
   *
   * @param  metadata  to copy.
   */
  public RuleResultMetadata(final List<RuleResultMetadata> metadata)
  {
    PassayUtils.assertNotNullArgOr(
      metadata, v -> v.stream().anyMatch(Objects::isNull), "Metadata cannot be null or contain null");
    metadata.forEach(md -> counts.putAll(md.getCounts()));
  }


  /**
   * Returns whether a count exists for the supplied category.
   *
   * @param  category  of the count.
   *
   * @return  whether a count exists.
   */
  public boolean hasCount(final CountCategory category)
  {
    return counts.containsKey(category);
  }


  /**
   * Returns the count for the supplied category.
   *
   * @param  category  of the count.
   *
   * @return  character count.
   */
  public int getCount(final CountCategory category)
  {
    return counts.get(category);
  }


  /**
   * Returns an unmodifiable map of all count metadata.
   *
   * @return  count metadata.
   */
  public Map<CountCategory, Integer> getCounts()
  {
    return Collections.unmodifiableMap(counts);
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::counts=" + counts;
  }
}

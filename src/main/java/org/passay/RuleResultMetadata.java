/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes metadata relevant to the result of rule validation.
 *
 * @author  Middleware Services
 */
public class RuleResultMetadata
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

    /** whitespace characters. */
    Whitespace,

    /** allowed characters. */
    Allowed,

    /** illegal characters. */
    Illegal,

    /** Already leaked password. */
    Pwned;


    /**
     * Returns whether a count category exists with the supplied name.
     *
     * @param  name  to check.
     *
     * @return  whether the supplied name exists.
     * @deprecated use the standard {@link CountCategory#valueOf(String)} instead
     */
    @Deprecated
    public static boolean exists(final String name)
    {
      for (CountCategory cc : CountCategory.values()) {
        if (cc.name().equals(name)) {
          return true;
        }
      }
      return false;
    }
  }

  /** Character count metadata. */
  protected final Map<CountCategory, Integer> counts = new HashMap<>();


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
    putCount(category, value);
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


  /**
   * Adds a count to the metadata.
   *
   * @param  category  of the count.
   * @param  value  non-negative character count.
   */
  public void putCount(final CountCategory category, final int value)
  {
    if (value < 0) {
      throw new IllegalArgumentException("Count value must be greater than or equals to zero");
    }
    counts.put(category, value);
  }


  /**
   * Merges the supplied metadata with this metadata. This method will overwrite any existing categories.
   *
   * @param  metadata  to merge.
   */
  public void merge(final RuleResultMetadata metadata)
  {
    counts.putAll(metadata.getCounts());
  }


  @Override
  public String toString()
  {
    return String.format("counts=%s", counts);
  }
}

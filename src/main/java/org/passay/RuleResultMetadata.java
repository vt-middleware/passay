/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describes metadata relevant to the result of rule validation.
 *
 * @author  Middleware Services
 */
public class RuleResultMetadata
{

  /** Additional metadata that provide information about the rule. */
  protected final Map<String, Object> metadata;


  /**
   * Creates a new rule result metadata.
   *
   * @param  data  metadata.
   */
  public RuleResultMetadata(final Map<String, Object> data)
  {
    if (data == null) {
      metadata = new LinkedHashMap<>();
    } else {
      metadata = new LinkedHashMap<>(data);
    }
  }


  /**
   * Puts the supplied key and value into this metadata object.
   *
   * @param  key  of the metadata.
   * @param  value  of the metadata.
   */
  public void put(final String key, final Object value)
  {
    metadata.put(key, value);
  }


  /**
   * Puts all the supplied metadata into this metadata object.
   *
   * @param  data  to add.
   */
  public void putAll(final Map<String, Object> data)
  {
    metadata.putAll(data);
  }


  /**
   * Returns the metadata value for the supplied key.
   *
   * @param  <T>  type of value to return
   * @param  key  to the metadata to retrieve.
   * @param  type  to cast the metadata value to.
   *
   * @return  metadata value.
   */
  public <T> T get(final String key, final Class<T> type)
  {
    return type.cast(metadata.get(key));
  }


  /**
   * Returns an unmodifiable map of all the metadata.
   *
   * @return  all metadata.
   */
  public Map<String, Object> getAll()
  {
    return Collections.unmodifiableMap(metadata);
  }


  @Override
  public String toString()
  {
    return String.format("%s", metadata);
  }
}

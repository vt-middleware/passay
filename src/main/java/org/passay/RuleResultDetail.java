/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Describes an exact cause of a rule validation failure.
 *
 * @author  Middleware Services
 */
public final class RuleResultDetail
{

  /** Detail error codes. */
  private final String[] errorCodes;

  /** Additional parameters that provide information about validation failure. */
  private final Map<String, Object> parameters;


  /**
   * Creates a new rule result detail.
   *
   * @param  code  error code.
   * @param  params  error details.
   */
  public RuleResultDetail(final String code, final Map<String, Object> params)
  {
    PassayUtils.assertNotNullArgOr(
      code,
      String::isEmpty,
      "Code cannot be null or empty");
    PassayUtils.assertNotNullArgOr(
      params,
      v -> v.entrySet().stream().anyMatch(e -> Objects.isNull(e.getKey()) || Objects.isNull(e.getValue())),
      "Params cannot be null or contain null");
    errorCodes = new String[] {code};
    parameters = new LinkedHashMap<>(params);
  }



  /**
   * Creates a new rule result detail.
   *
   * @param  codes  One or more error codes. Codes MUST be provided in order of decreasing specificity.
   * @param  params  error details.
   */
  public RuleResultDetail(final String[] codes, final Map<String, Object> params)
  {
    PassayUtils.assertNotNullArgOr(
      codes,
      v -> v.length == 0 || Stream.of(codes).anyMatch(c -> Objects.isNull(c) || c.isEmpty()),
      "Code cannot be null or empty and must contain at least one error code");
    PassayUtils.assertNotNullArgOr(
      params,
      v -> v.entrySet().stream().anyMatch(e -> Objects.isNull(e.getKey()) || Objects.isNull(e.getValue())),
      "Params cannot be null or contain null");
    errorCodes = codes;
    parameters = new LinkedHashMap<>(params);
  }


  /**
   * Returns the least-specific error code.
   *
   * @return  error code.
   */
  public String getErrorCode()
  {
    return errorCodes[errorCodes.length - 1];
  }


  /**
   * Returns an array of error codes as provided at creation time.
   *
   * @return  Array of error codes that the caller may assume are organized in order of decreasing specificity.
   */
  public String[] getErrorCodes()
  {
    return Arrays.copyOf(errorCodes, errorCodes.length);
  }


  /**
   * Returns the parameters.
   *
   * @return  map of parameter name to value.
   */
  public Map<String, Object> getParameters()
  {
    return Collections.unmodifiableMap(parameters);
  }


  /**
   * Returns the parameter values.
   *
   * @return  array of parameters or empty array if no parameters defined.
   */
  public Object[] getValues()
  {
    return parameters.values().toArray();
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "errorCodes=" + Arrays.toString(errorCodes) + ", " +
      "parameters=" + parameters;
  }
}

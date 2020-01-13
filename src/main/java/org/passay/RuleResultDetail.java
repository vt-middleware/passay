/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describes an exact cause of a rule validation failure.
 *
 * @author  Middleware Services
 */
public class RuleResultDetail
{

  /** Detail error codes. */
  protected final String[] errorCodes;

  /** Additional parameters that provide information about validation failure. */
  protected final Map<String, Object> parameters;


  /**
   * Creates a new rule result detail.
   *
   * @param  code  error code.
   * @param  params  error details.
   */
  public RuleResultDetail(final String code, final Map<String, Object> params)
  {
    if (code == null || code.length() == 0) {
      throw new IllegalArgumentException("Code cannot be null or empty.");
    }
    errorCodes = new String[] {code};
    parameters = params == null ? new LinkedHashMap<>() : new LinkedHashMap<>(params);
  }



  /**
   * Creates a new rule result detail.
   *
   * @param  params  error details.
   * @param  codes  One or more error codes. Codes MUST be provided in order of decreasing specificity.
   */
  public RuleResultDetail(final Map<String, Object> params, final String... codes)
  {
    if (codes == null || codes.length == 0) {
      throw new IllegalArgumentException("Must specify at least one error code.");
    }
    for (String code : codes) {
      if (code == null || code.length() == 0) {
        throw new IllegalArgumentException("Code cannot be null or empty.");
      }
    }
    errorCodes = codes;
    parameters = params == null ? new LinkedHashMap<>() : new LinkedHashMap<>(params);
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
    return errorCodes;
  }


  /**
   * Returns the parameters.
   *
   * @return  map of parameter name to value.
   */
  public Map<String, Object> getParameters()
  {
    return parameters;
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
    return String.format("%s:%s", Arrays.asList(errorCodes), parameters);
  }
}

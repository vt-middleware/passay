/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Input data used by {@link IllegalSequenceRule}.
 *
 * @author  Middleware Services
 */
public interface IllegalSequenceData
{
  /**
   * Return the error code used for message resolution.
   *
   * @return  error code
   */
  String getErrorCode();


  /**
   * @return  one or more illegal character sequences.
   */
  IllegalSequence[] getSequences();
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data;

/**
 * Container for one or more {@link CharacterSequence}.
 *
 * @author  Middleware Services
 */
public interface SequenceData
{


  /**
   * Return the error code used for message resolution.
   *
   * @return  error code
   */
  String getErrorCode();


  /** @return  one or more illegal character sequences. */
  CharacterSequence[] getSequences();
}

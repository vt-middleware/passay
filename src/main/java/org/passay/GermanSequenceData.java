/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * german character sequence.
 *
 * @author Middleware Services + Torben Reetz
 */
public enum GermanSequenceData implements SequenceData
{

  /**
   * Alphabetical sequence.
   */
  Alphabetical(
      "ILLEGAL_ALPHABETICAL_SEQUENCE",
      new CharacterSequence[] {
        new CharacterSequence("abcdefghijklmnopqrstuvwxyzäöüß", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞ"),
      });

  /**
   * Error code.
   */
  private final String errorCode;

  /**
   * Character sequences.
   */
  private final CharacterSequence[] sequences;


  /**
   * Creates new german sequence data.
   *
   * @param code Error code used for message resolution.
   * @param seqs One or more character sequences.
   */
  GermanSequenceData(final String code, final CharacterSequence[] seqs)
  {
    errorCode = code;
    sequences = seqs;
  }

  @Override
  public String getErrorCode()
  {
    return errorCode;
  }

  @Override
  public CharacterSequence[] getSequences()
  {
    return sequences;
  }

}

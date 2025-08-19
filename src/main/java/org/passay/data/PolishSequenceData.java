/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data;

/**
 * Polish character sequences.
 *
 * @author  Middleware Services + Wojciech Buczko
 */
public enum PolishSequenceData implements SequenceData {

  /** Alphabetical sequence. */
  Alphabetical(
    "ILLEGAL_ALPHABETICAL_SEQUENCE",
    new CharacterSequence[] {
      new CharacterSequence("aąbcćdeęfghijklłmnńoópqrsśtuwxyzźż", "AĄBCĆDEĘFGHIJKLŁMNŃOÓPQRSŚTUWXYZŹŻ"),
    });

  /** Error code. */
  private final String errorCode;

  /** Character sequences. */
  private final CharacterSequence[] sequences;


  /**
   * Creates a new polish sequence data.
   *
   * @param  code  Error code used for message resolution.
   * @param  seqs  One or more character sequences.
   */
  PolishSequenceData(final String code, final CharacterSequence[] seqs)
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

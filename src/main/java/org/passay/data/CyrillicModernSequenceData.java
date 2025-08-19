/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data;

/**
 * Cyrillic character sequence.
 *
 * @author Middleware Services
 */
public enum CyrillicModernSequenceData implements SequenceData {

  /**
   * Alphabetical sequence.
   */
  Alphabetical(
    "ILLEGAL_ALPHABETICAL_SEQUENCE",
    new CharacterSequence[] {
      new CharacterSequence("абвгдеёжзийклмнопрстуфхцчшщъыьэюя", "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"),
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
   * Creates new cyrillic sequence data.
   *
   * @param code Error code used for message resolution.
   * @param seqs One or more character sequences.
   */
  CyrillicModernSequenceData(final String code, final CharacterSequence[] seqs)
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

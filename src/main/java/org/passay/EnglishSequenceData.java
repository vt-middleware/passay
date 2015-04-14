/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * English character sequences.
 *
 * @author  Middleware Services
 */
public enum EnglishSequenceData implements SequenceData {

  /** Alphabetical sequence. */
  Alphabetical(
    "ILLEGAL_ALPHABETICAL_SEQUENCE",
    new CharacterSequence[] {new CharacterSequence("abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"), }),

  /** Numerical sequence. */
  Numerical(
    "ILLEGAL_NUMERICAL_SEQUENCE",
    new CharacterSequence[] {new CharacterSequence("0123456789"), }),

  /** QWERTY keyboard sequence. */
  Qwerty(
    "ILLEGAL_QWERTY_SEQUENCE",
    new CharacterSequence[] {
      new CharacterSequence("`1234567890-=", "~!@#$%^&*()_+"),
      new CharacterSequence("qwertyuiop[]\\", "QWERTYUIOP{}|"),
      new CharacterSequence("asdfghjkl;'", "ASDFGHJKL:\""),
      new CharacterSequence("zxcvbnm,./", "ZXCVBNM<>?"),
    });

  /** Error code. */
  private String errorCode;

  /** Character sequences. */
  private CharacterSequence[] sequences;

  /**
   * Creates a new instance with given parameters.
   *
   * @param  code  Error code used for message resolution.
   * @param  seqs  One or more character sequences.
   */
  private EnglishSequenceData(final String code, final CharacterSequence[] seqs)
  {
    this.errorCode = code;
    this.sequences = seqs;
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

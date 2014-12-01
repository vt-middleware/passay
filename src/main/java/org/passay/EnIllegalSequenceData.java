/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * English illegal character sequences.
 *
 * @author  Middleware Services
 */
public enum EnIllegalSequenceData implements IllegalSequenceData
{
  /** Alphabetical sequence. */
  Alphabetical("ILLEGAL_ALPHABETICAL_SEQUENCE", new IllegalSequence[] {
    new IllegalSequence(
      "abcdefghijklmnopqrstuvwxyz",
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
  }),

  /** Numerical sequence. */
  Numerical("ILLEGAL_NUMERICAL_SEQUENCE", new IllegalSequence[] {
    new IllegalSequence("0123456789"),
  }),

  /** QWERTY keyboard sequence. */
  Qwerty("ILLEGAL_QWERTY_SEQUENCE", new IllegalSequence[] {
    new IllegalSequence("`1234567890-=", "~!@#$%^&*()_+"),
    new IllegalSequence("qwertyuiop[]\\", "QWERTYUIOP{}|"),
    new IllegalSequence("asdfghjkl;'", "ASDFGHJKL:\""),
    new IllegalSequence("zxcvbnm,./", "ZXCVBNM<>?"),
  });

  /** Error code. */
  private String errorCode;

  /** Character sequences. */
  private IllegalSequence[] sequences;

  /**
   * Creates a new instance with given parameters.
   *
   * @param  code  Error code used for message resolution.
   * @param  seqs  One or more character sequences.
   */
  private EnIllegalSequenceData(final String code, final IllegalSequence[] seqs)
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
  public IllegalSequence[] getSequences()
  {
    return sequences;
  }
}

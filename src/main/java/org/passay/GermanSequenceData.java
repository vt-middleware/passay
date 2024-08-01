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
    new CharacterSequence[]{
      new CharacterSequence("abcdefghijklmnopqrstuvwxyzäöüß", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞ"),
    }),

  /**
   * German QWERTZ keyboard sequence.
   */
  DEQwertz(
    "ILLEGAL_QWERTY_SEQUENCE",
    // rows are no-modifier, shift, alt-gr
    // dead keys are denoted with the null character /u0000
    new CharacterSequence[]{
      new CharacterSequence(
        "^1234567890ß\\´",
        "°!\"§$%&/()=?`",
        "\u0000\u0000²³\u0000\u0000\u0000{[]}\\\u0000"),
      new CharacterSequence(
        "qwertzuiopü+",
        "QWERTZUIOPÜ*",
        "@\u0000€\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000~"),
      new CharacterSequence(
        "asdfghjklöä#",
        "ASDFGHJKLÖÄ'",
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"),
      new CharacterSequence(
        "<yxcvbnm,.-",
        ">YXCVBNM;:_",
        "|\u0000\u0000\u0000\u0000\u0000\u0000µ\u0000\u0000\u0000"),
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

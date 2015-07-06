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

  /** US QWERTY keyboard sequence. */
  USQwerty(
    "ILLEGAL_QWERTY_SEQUENCE",
    // rows are no-modifier, shift, alt, shift+alt
    // dead keys are denoted with the null character /u0000
    new CharacterSequence[] {
      new CharacterSequence(
        "`1234567890-=",
        "~!@#$%^&*()_+",
        "\u0000\u00a1\u2122\u00a3\u00a2\u221e\u00a7\u00b6\u2022\u00aa\u00ba\u2013\u2260",
        "\u0060\u2044\u20ac\u2039\u203a\ufb01\ufb02\u2021\u00b0\u00b7\u201a\u2014\u00b1"),
      new CharacterSequence(
        "qwertyuiop[]\\",
        "QWERTYUIOP{}|",
        "\u0153\u2211\u0000\u00ae\u2020\u00a5\u0000\u0000\u00f8\u03c0\u201c\u2018\u00ab",
        "\u0152\u201e\u00b4\u2030\u02c7\u00c1\u00a8\u02c6\u00d8\u220f\u201d\u2019\u00bb"),
      new CharacterSequence(
        "asdfghjkl;'",
        "ASDFGHJKL:\"",
        "\u00e5\u00df\u2202\u0192\u00a9\u02d9\u2206\u02da\u00ac\u2026\u00e6",
        "\u00c5\u00cd\u00ce\u00cf\u02dd\u00d3\u00d4\uf8ff\u00d2\u00da\u00c6"),
      new CharacterSequence(
        "zxcvbnm,./",
        "ZXCVBNM<>?",
        "\u03a9\u2248\u00e7\u221a\u222b\u0000\u00b5\u2264\u2265\u00f7",
        "\u00b8\u02db\u00c7\u25ca\u0131\u02dc\u00c2\u00af\u02d8\u00bf"),
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

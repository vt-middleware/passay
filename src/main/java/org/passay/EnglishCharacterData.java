/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * English language character data.
 *
 * @author  Middleware Services
 */
public enum EnglishCharacterData implements CharacterData {

  /** Lower case characters. */
  LowerCase("INSUFFICIENT_LOWERCASE", "abcdefghijklmnopqrstuvwxyz"),

  /** Upper case characters. */
  UpperCase("INSUFFICIENT_UPPERCASE", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

  /** Digit characters. */
  Digit("INSUFFICIENT_DIGIT", "0123456789"),

  /** Alphabetical characters (upper and lower case). */
  Alphabetical("INSUFFICIENT_ALPHABETICAL", UpperCase.getCharacters() + LowerCase.getCharacters()),

  /** Special ascii characters. */
  SpecialAscii("INSUFFICIENT_SPECIAL",
    // ASCII symbols
    "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"),

  /** Special unicode characters. */
  SpecialUnicode("INSUFFICIENT_SPECIAL",
          // Unicode symbols
          "\u2013\u2014\u2015\u2017\u2018\u2019\u201a\u201b\u201c\u201d\u201e\u2020\u2021\u2022\u2026\u2030\u2032\u2033" +
          "\u2039\u203a\u203c\u203e\u2044\u204a" +
          // Unicode currency
          "\u20a0\u20a1\u20a2\u20a3\u20a4\u20a5\u20a6\u20a7\u20a8\u20a9\u20aa\u20ab\u20ac\u20ad\u20ae\u20af" +
          "\u20b0\u20b1\u20b2\u20b3\u20b4\u20b5\u20b6\u20b7\u20b8\u20b9\u20ba\u20bb\u20bc\u20bd\u20be"),

  /** Special latin characters. */
  SpecialLatin( "INSUFFICIENT_SPECIAL",
          // Latin-1 symbols
          "\u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af" +
          "\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf" +
          // Latin-1 math
          "\u00d7\u00f7"),

  /** Special characters. */
  Special("INSUFFICIENT_SPECIAL",
          // all symbols
          SpecialAscii.getCharacters() + SpecialUnicode.getCharacters() + SpecialLatin.getCharacters());

  /** Error code. */
  private final String errorCode;

  /** Characters. */
  private final String characters;


  /**
   * Creates a new english character data.
   *
   * @param  code  Error code.
   * @param  charString  Characters as string.
   */
  EnglishCharacterData(final String code, final String charString)
  {
    errorCode = code;
    characters = charString;
  }

  @Override
  public String getErrorCode()
  {
    return errorCode;
  }

  @Override
  public String getCharacters()
  {
    return characters;
  }
}

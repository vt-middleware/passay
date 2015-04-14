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

  /** Alphabetical characters (upper and lower case). */
  Special("INSUFFICIENT_SPECIAL", "`~@#$%^&*()-_=+[{]}\\|;:'\",<.>/?");


  /** Error code. */
  private String errorCode;

  /** Characters. */
  private String characters;


  /**
   * Creates a new instance from given parameters.
   *
   * @param  code  Error code.
   * @param  charString  Characters as string.
   */
  private EnglishCharacterData(final String code, final String charString)
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

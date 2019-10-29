/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Polish language character data.
 *
 * @author  Middleware Services + Wojciech Buczko
 */
public enum  PolishCharacterData implements CharacterData {

  /** Lower case characters. */
  LowerCase("INSUFFICIENT_LOWERCASE", "aąbcćdeęfghijklmnoópqrsśtuvwxyzźż"),

  /** Upper case characters. */
  UpperCase("INSUFFICIENT_UPPERCASE", "AĄBCĆDEĘFGHIJKLMNOÓPQRSŚTUVWXYZŹŻ");

  /** Error code. */
  private final String errorCode;

  /** Characters. */
  private final String characters;


  /**
   * Creates a new polish character data.
   *
   * @param  code  Error code.
   * @param  charString  Characters as string.
   */
  PolishCharacterData(final String code, final String charString)
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


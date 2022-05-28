/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data.character;

/**
 * german character data.
 *
 * @author Middleware Services + Torben Reetz
 */
public enum GermanCharacterData implements CharacterData
{

  /**
   * Lower case characters.
   */
  LowerCase("INSUFFICIENT_LOWERCASE", "abcdefghijklmnopqrstuvwxyzäöüß"),

  /**
   * Upper case characters.
   */
  UpperCase("INSUFFICIENT_UPPERCASE", "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞ");

  /**
   * Error code.
   */
  private final String errorCode;

  /**
   * Characters.
   */
  private final String characters;


  /**
   * Creates german character data.
   *
   * @param code       Error code.
   * @param charString Characters as string.
   */
  GermanCharacterData(final String code, final String charString)
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


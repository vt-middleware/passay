/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data.character;

/**
 * Archaic cyrillic character data.
 *
 * @author  Middleware Services
 */
public enum ArchaicCyrillicCharacterData implements CharacterData {

  /**
   * Lower case characters.
   */
  LowerCase("INSUFFICIENT_LOWERCASE", "абвгдеёжзийклмнопрстуфхцчшщъыьэюяіѣѳѵ"),

  /**
   * Upper case characters.
   */
  UpperCase("INSUFFICIENT_UPPERCASE", "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯІѢѲѴ");

  /**
   * Error code.
   */
  private final String errorCode;

  /**
   * Characters.
   */
  private final String characters;

  /**
   * Creates a new cyrillic character data.
   *
   * @param code          Error code
   * @param charString    Characters as string
   */
  ArchaicCyrillicCharacterData(final String code, final String charString)
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

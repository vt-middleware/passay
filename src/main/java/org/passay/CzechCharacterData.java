/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Czech character data.
 *
 * @author Middleware Services + Martin Pechacek
 */
public enum CzechCharacterData implements CharacterData {

  /**
   * Lower case characters.
   */
  LowerCase("INSUFFICIENT_LOWERCASE", "aábcčdďeěéfghiíjklmnňoópqrřsštťuúůvwxyýzž"),

  /**
   * Upper case characters.
   */
  UpperCase("INSUFFICIENT_UPPERCASE", "AÁBCČDĎEĚÉFGHIÍJKLMNŇOÓPQRŘSŠTŤUÚŮVWXYÝZŽ");

  /**
   * Error code.
   */
  private final String errorCode;

  /**
   * Characters.
   */
  private final String characters;


  /**
   * Creates a new czech character data.
   *
   * @param code          Error code
   * @param charString    Characters as string
   */
  CzechCharacterData(final String code, final String charString)
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

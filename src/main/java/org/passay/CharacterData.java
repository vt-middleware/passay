/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Input data used by {@link CharacterRule}.
 *
 * @author  Middleware Services
 */
public interface CharacterData
{

  /** Lowercase character data. */
  CharacterData LOWER_CASE = new CharacterData()
  {


    @Override
    public String getErrorCode()
    {
      return "INSUFFICIENT_LOWERCASE";
    }


    @Override
    public char[] getCharacters()
    {
      return Characters.EN_LOWERCASE;
    }
  };

  /** Uppercase character data. */
  CharacterData UPPER_CASE = new CharacterData()
  {


    @Override
    public String getErrorCode()
    {
      return "INSUFFICIENT_UPPERCASE";
    }


    @Override
    public char[] getCharacters()
    {
      return Characters.EN_UPPERCASE;
    }
  };

  /** Digit character data. */
  CharacterData DIGIT = new CharacterData()
  {


    @Override
    public String getErrorCode()
    {
      return "INSUFFICIENT_DIGIT";
    }


    @Override
    public char[] getCharacters()
    {
      return Characters.EN_DIGIT;
    }
  };

  /** Alphabetical character data. */
  CharacterData ALPHABETICAL = new CharacterData()
  {


    @Override
    public String getErrorCode()
    {
      return "INSUFFICIENT_ALPHABETICAL";
    }


    @Override
    public char[] getCharacters()
    {
      return Characters.EN_ALPHABETICAL;
    }
  };

  /** Special character data. */
  CharacterData SPECIAL = new CharacterData()
  {


    @Override
    public String getErrorCode()
    {
      return "INSUFFICIENT_SPECIAL";
    }


    @Override
    public char[] getCharacters()
    {
      return Characters.EN_SPECIAL;
    }
  };


  /**
   * Return the error code used for message resolution.
   *
   * @return  error code
   */
  String getErrorCode();


  /**
   * Returns the characters.
   *
   * @return  characters
   */
  char[] getCharacters();
}

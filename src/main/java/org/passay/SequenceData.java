/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Input data used by {@link SequenceRule}.
 *
 * @author  Middleware Services
 */
public interface SequenceData
{

  /** Qwerty sequence data. */
  SequenceData QWERTY = new SequenceData()
  {


    @Override
    public String getErrorCode()
    {
      return "ILLEGAL_QWERTY_SEQUENCE";
    }


    @Override
    public char[][][] getCharacters()
    {
      return Sequences.EN_QWERTY;
    }
  };

  /** Alphabetical sequence data. */
  SequenceData ALPHABETICAL = new SequenceData()
  {


    @Override
    public String getErrorCode()
    {
      return "ILLEGAL_ALPHABETICAL_SEQUENCE";
    }


    @Override
    public char[][][] getCharacters()
    {
      return Sequences.EN_ALPHABETICAL;
    }
  };

  /** Numerical sequence data. */
  SequenceData NUMERICAL = new SequenceData()
  {


    @Override
    public String getErrorCode()
    {
      return "ILLEGAL_NUMERICAL_SEQUENCE";
    }


    @Override
    public char[][][] getCharacters()
    {
      return Sequences.EN_NUMERICAL;
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
  char[][][] getCharacters();
}

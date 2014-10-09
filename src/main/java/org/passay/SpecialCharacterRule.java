/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of special
 * characters. Characters are defined in {@link #CHARS}.
 *
 * @author  Middleware Services
 */
public class SpecialCharacterRule extends AbstractCharacterRule
{

  /** Special characters, value is {@value}. */
  public static final String CHARS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

  /** Error code for insufficient number of characters of particular class. */
  public static final String ERROR_CODE = "INSUFFICIENT_SPECIAL";


  /** Default constructor. */
  public SpecialCharacterRule() {}


  /**
   * Creates a new non alphanumeric character rule.
   *
   * @param  num  number of non-alphanumeric characters to enforce
   */
  public SpecialCharacterRule(final int num)
  {
    setNumberOfCharacters(num);
  }


  @Override
  public String getValidCharacters()
  {
    return CHARS;
  }


  @Override
  protected String getErrorCode()
  {
    return ERROR_CODE;
  }


  @Override
  protected String getCharacterTypes(final String password)
  {
    return PasswordUtils.getMatchingCharacters(CHARS, password);
  }
}

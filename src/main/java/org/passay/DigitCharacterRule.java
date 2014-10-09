/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of digit
 * characters. Characters are defined in {@link #CHARS}.
 *
 * @author  Middleware Services
 */
public class DigitCharacterRule extends AbstractCharacterRule
{

  /** Digit characters, value is {@value}. */
  public static final String CHARS = "0123456789";

  /** Error code for insufficient number of characters of particular class. */
  public static final String ERROR_CODE = "INSUFFICIENT_DIGIT";

  /** Default constructor. */
  public DigitCharacterRule() {}


  /**
   * Create a new digit character rule.
   *
   * @param  num  of digit characters to enforce
   */
  public DigitCharacterRule(final int num)
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

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of uppercase
 * characters. Characters are defined in {@link #CHARS}.
 *
 * @author  Middleware Services
 */
public class UppercaseCharacterRule extends AbstractCharacterRule
{

  /** Uppercase characters, value is {@value}. */
  public static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /** Error code for insufficient number of characters of particular class. */
  public static final String ERROR_CODE = "INSUFFICIENT_UPPERCASE";


  /** Default constructor. */
  public UppercaseCharacterRule() {}


  /**
   * Create a new uppercase character rule.
   *
   * @param  num  number of uppercase characters to enforce
   */
  public UppercaseCharacterRule(final int num)
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

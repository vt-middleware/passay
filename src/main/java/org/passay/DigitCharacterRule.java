/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of digit
 * characters.
 *
 * @author  Middleware Services
 */
public class DigitCharacterRule extends AbstractCharacterRule
{

  /** Character type. */
  private static final String CHARACTER_TYPE = "digit";


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
    return "0123456789";
  }


  @Override
  protected int getNumberOfCharacterType(final Password password)
  {
    return password.getNumberOfDigits();
  }


  @Override
  protected String getCharacterType()
  {
    return CHARACTER_TYPE;
  }
}

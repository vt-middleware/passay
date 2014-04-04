/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of lowercase
 * characters.
 *
 * @author  Middleware Services
 */
public class LowercaseCharacterRule extends AbstractCharacterRule
{

  /** Character type. */
  private static final String CHARACTER_TYPE = "lowercase";


  /** Default constructor. */
  public LowercaseCharacterRule() {}


  /**
   * Creates a new lowercase character rule.
   *
   * @param  num  number of lowercase characters to enforce
   */
  public LowercaseCharacterRule(final int num)
  {
    setNumberOfCharacters(num);
  }


  @Override
  public String getValidCharacters()
  {
    return "abcdefghijklmnopqrstuvwxyz";
  }


  @Override
  protected int getNumberOfCharacterType(final Password password)
  {
    return password.getNumberOfLowercase();
  }


  @Override
  protected String getCharacterType()
  {
    return CHARACTER_TYPE;
  }
}

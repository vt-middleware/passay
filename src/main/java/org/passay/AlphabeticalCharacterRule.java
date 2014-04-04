/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of
 * alphabetical characters.
 *
 * @author  Middleware Services
 */
public class AlphabeticalCharacterRule extends AbstractCharacterRule
{

  /** Character type. */
  private static final String CHARACTER_TYPE = "alphabetical";


  /** Default constructor. */
  public AlphabeticalCharacterRule() {}


  /**
   * Creates a new alphabetical character rule.
   *
   * @param  num  of alphabetical characters to enforce
   */
  public AlphabeticalCharacterRule(final int num)
  {
    setNumberOfCharacters(num);
  }


  @Override
  public String getValidCharacters()
  {
    return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  }


  @Override
  protected int getNumberOfCharacterType(final Password password)
  {
    return password.getNumberOfAlphabetical();
  }


  @Override
  protected String getCharacterType()
  {
    return CHARACTER_TYPE;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of
 * non-alphanumeric characters.
 *
 * @author  Middleware Services
 */
public class NonAlphanumericCharacterRule extends AbstractCharacterRule
{

  /** Character type. */
  private static final String CHARACTER_TYPE = "non-alphanumeric";


  /** Default constructor. */
  public NonAlphanumericCharacterRule() {}


  /**
   * Creates a new non alphanumeric character rule.
   *
   * @param  num  number of non-alphanumeric characters to enforce
   */
  public NonAlphanumericCharacterRule(final int num)
  {
    setNumberOfCharacters(num);
  }


  @Override
  public String getValidCharacters()
  {
    return "`~!@#$%^&*()-_=+[{]}\\|;:'\"<,>./?";
  }


  @Override
  protected int getNumberOfCharacterType(final Password password)
  {
    return password.getNumberOfNonAlphanumeric();
  }


  @Override
  protected String getCharacterType()
  {
    return CHARACTER_TYPE;
  }
}

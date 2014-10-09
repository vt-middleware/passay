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

  /** Character type. */
  private static final String CHARACTER_TYPE = "uppercase";


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
  protected int getNumberOfCharacterType(final String password)
  {
    return PasswordUtils.getMatchingCharacterCount(CHARS, password);
  }


  @Override
  protected String getCharacterType()
  {
    return CHARACTER_TYPE;
  }
}

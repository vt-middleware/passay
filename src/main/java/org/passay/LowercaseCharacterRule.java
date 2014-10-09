/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of lowercase
 * characters. Characters are defined in {@link #CHARS}.
 *
 * @author  Middleware Services
 */
public class LowercaseCharacterRule extends AbstractCharacterRule
{

  /** Lowercase characters, value is {@value}. */
  public static final String CHARS = "abcdefghijklmnopqrstuvwxyz";

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

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

  /** Character type. */
  private static final String CHARACTER_TYPE = "non-alphanumeric";


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

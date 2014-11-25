/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Rule for determining if a password contains the correct number of
 * alphabetical characters. Characters are defined in {@link #CHARS}.
 *
 * @author  Middleware Services
 */
public class AlphabeticalCharacterRule extends AbstractCharacterRule
{

  /** Lowercase and uppercase characters, value is {@value}. */
  public static final String CHARS = LowercaseCharacterRule.CHARS +
    UppercaseCharacterRule.CHARS;

  /** Error code for insufficient number of characters of particular class. */
  public static final String ERROR_CODE = "INSUFFICIENT_ALPHABETICAL";


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

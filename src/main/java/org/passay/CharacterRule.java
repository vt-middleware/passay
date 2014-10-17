/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Validates whether a password contains a certain number of a type of
 * character.
 *
 * @author  Middleware Services
 */
public class CharacterRule implements Rule
{

  /** Character data for this rule. */
  protected final CharacterData characterData;

  /** Number of characters to require. Default value is {@value}. */
  protected int numCharacters = 1;


  /**
   * Creates a new character rule.
   *
   * @param  data  character data for this rule
   */
  public CharacterRule(final CharacterData data)
  {
    this(data, 1);
  }


  /**
   * Creates a new character rule.
   *
   * @param  data  character data for this rule
   * @param  num  of characters to enforce
   */
  public CharacterRule(final CharacterData data, final int num)
  {
    setNumberOfCharacters(num);
    characterData = data;
  }


  /**
   * Sets the number of characters to require in a password.
   *
   * @param  n  number of characters to require where n > 0
   */
  public void setNumberOfCharacters(final int n)
  {
    if (n > 0) {
      numCharacters = n;
    } else {
      throw new IllegalArgumentException("argument must be greater than zero");
    }
  }


  /**
   * Returns the number of characters which must exist in order for a password
   * to meet the requirements of this rule.
   *
   * @return  number of characters to require
   */
  public int getNumberOfCharacters()
  {
    return numCharacters;
  }


  /**
   * Returns the characters that are considered valid for this rule.
   *
   * @return  valid characters
   */
  public String getValidCharacters()
  {
    return characterData.getCharacters();
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final String matchingChars = PasswordUtils.getMatchingCharacters(
      String.valueOf(characterData.getCharacters()),
      passwordData.getPassword());
    if (matchingChars.length() >= numCharacters) {
      return new RuleResult(true);
    } else {
      return
        new RuleResult(
          false,
          new RuleResultDetail(
            characterData.getErrorCode(),
            createRuleResultDetailParameters(matchingChars)));
    }
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  matchingChars  characters found in the password
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(
    final String matchingChars)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("minimumRequired", numCharacters);
    m.put("matchingCharacterCount", matchingChars.length());
    m.put("validCharacters", String.valueOf(characterData.getCharacters()));
    m.put("matchingCharacters", matchingChars);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::numberOfCharacters=%s",
        getClass().getName(),
        hashCode(),
        numCharacters);
  }
}

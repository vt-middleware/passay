/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Validates whether a password contains a certain number of a type of character.
 *
 * @author  Middleware Services
 */
public class CharacterRule implements Rule
{

  /** Character data for this rule. */
  protected final CharacterData characterData;

  /** Number of characters to require. Default value is 1. */
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
   * @param  n  number of characters to require where n &gt; 0
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
   * Returns the number of characters which must exist in order for a password to meet the requirements of this rule.
   *
   * @return  number of characters to require
   */
  public int getNumberOfCharacters()
  {
    return numCharacters;
  }


  /**
   * Returns the character data for this rule.
   *
   * @return  character data
   */
  public CharacterData getCharacterData()
  {
    return characterData;
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
    final String matchingChars = getMatchingCharacters(
      String.valueOf(characterData.getCharacters()),
      passwordData.getPassword(),
      numCharacters);
    if (matchingChars.length() < numCharacters) {
      return new RuleResult(
        false,
        new RuleResultDetail(characterData.getErrorCode(), createRuleResultDetailParameters(matchingChars)),
        createRuleResultMetadata(passwordData));
    }
    return new RuleResult(true, createRuleResultMetadata(passwordData));
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  matchingChars  characters found in the password
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String matchingChars)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("minimumRequired", numCharacters);
    m.put("matchingCharacterCount", CodePoints.charCount(matchingChars));
    m.put("validCharacters", String.valueOf(characterData.getCharacters()));
    m.put("matchingCharacters", matchingChars);
    return m;
  }


  /**
   * Creates the rule result metadata.
   *
   * @param  password  data used for metadata creation
   *
   * @return  rule result metadata
   */
  protected RuleResultMetadata createRuleResultMetadata(final PasswordData password)
  {
    try {
      return new RuleResultMetadata(
        RuleResultMetadata.CountCategory.valueOf(characterData.toString()),
        CodePoints.countMatchingCharacters(characterData.getCharacters(), password.getPassword()));
    } catch (IllegalArgumentException iae) {
      return new RuleResultMetadata();
    }
  }


  /**
   * Returns all the characters in the input string that are also in the characters string.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   * @param  maximumLength maximum length of matching characters
   *
   * @return  matching characters or empty string
   */
  private static String getMatchingCharacters(final String characters, final String input, final int maximumLength)
  {
    final StringBuilder sb = new StringBuilder(input.length());
    int i = 0;
    while (i < input.length()) {
      final int cp = input.codePointAt(i);
      if (characters.indexOf(cp) != -1) {
        if (sb.length() < maximumLength) {
          sb.append(CodePoints.toString(cp));
        } else {
          break;
        }
      }
      i += Character.charCount(cp);
    }
    return sb.toString();
  }


  @Override
  public String toString()
  {
    return String.format(
      "%s@%h::characterData=%s,numberOfCharacters=%s",
      getClass().getName(),
      hashCode(),
      characterData,
      numCharacters);
  }
}

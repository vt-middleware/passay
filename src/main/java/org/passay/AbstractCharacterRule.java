/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides common implementation for password character rules.
 *
 * @author  Middleware Services
 */
public abstract class AbstractCharacterRule implements CharacterRule
{

  /** Number of characters to require. Default value is 1. */
  protected int numCharacters = 1;


  @Override
  public void setNumberOfCharacters(final int n)
  {
    if (n > 0) {
      numCharacters = n;
    } else {
      throw new IllegalArgumentException("argument must be greater than zero");
    }
  }


  @Override
  public int getNumberOfCharacters()
  {
    return numCharacters;
  }


  /**
   * Returns the characters in the supplied password that matched the type for
   * the implementing class.
   *
   * @param  password  to get characters from
   *
   * @return  characters
   */
  protected abstract String getCharacterTypes(final String password);


  /**
   * Returns the error code used in the {@link RuleResultDetail}.
   *
   * @return  error code
   */
  protected abstract String getErrorCode();


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final String matchingChars = getCharacterTypes(passwordData.getPassword());
    if (matchingChars.length() >= numCharacters) {
      return new RuleResult(true);
    } else {
      return
        new RuleResult(
          false,
          new RuleResultDetail(
            getErrorCode(),
            createRuleResultDetailParameters(
              passwordData.getPassword(),
              matchingChars)));
    }
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  password  password
   * @param  matchingChars  characters found in the password
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(
    final String password,
    final String matchingChars)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("minimumRequired", numCharacters);
    m.put("matchingCharacterCount", matchingChars.length());
    m.put("validCharacters", getValidCharacters());
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

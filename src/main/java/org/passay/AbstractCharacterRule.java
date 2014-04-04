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

  /** Error code for insufficient number of characters of particular class. */
  public static final String ERROR_CODE = "INSUFFICIENT_CHARACTERS";

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
   * Returns the number of the type of characters in the supplied password for
   * the implementing class.
   *
   * @param  password  to get character count from
   *
   * @return  number of characters
   */
  protected abstract int getNumberOfCharacterType(final Password password);


  /**
   * Returns the type of character managed by this rule.
   *
   * @return  name of a character type, e.g. "digits."
   */
  protected abstract String getCharacterType();


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    if (getNumberOfCharacterType(passwordData.getPassword()) >= numCharacters) {
      return new RuleResult(true);
    } else {
      return
        new RuleResult(
          false,
          new RuleResultDetail(
            ERROR_CODE,
            createRuleResultDetailParameters(passwordData.getPassword())));
    }
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  p  password
   *
   * @return  map of parameter name to value
   */
  protected Map<String, ?> createRuleResultDetailParameters(final Password p)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("minimumRequired", numCharacters);
    m.put("characterType", getCharacterType());
    m.put("validCharacterCount", getNumberOfCharacterType(p));
    m.put("validCharacters", getValidCharacters());
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

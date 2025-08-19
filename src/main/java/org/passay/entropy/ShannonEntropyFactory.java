/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import org.passay.DefaultPasswordValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.data.EnglishCharacterData;
import org.passay.rule.AbstractDictionaryRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.Rule;

/**
 * Factory for creating {@link ShannonEntropy} from password rules and password data.
 *
 * @author  Middleware Services
 */
public final class ShannonEntropyFactory
{

  /** Number of character characteristics rules to enforce for a password to have composition. */
  private static final int COMPOSITION_CHARACTERISTICS_REQUIREMENT = 4;

  /** Validator which decides whether a password has composition. */
  private static final PasswordValidator COMPOSITION_VALIDATOR;

  /* Initialize the composition rule. */
  static {
    COMPOSITION_VALIDATOR = new DefaultPasswordValidator(
      new CharacterCharacteristicsRule(
        COMPOSITION_CHARACTERISTICS_REQUIREMENT,
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(EnglishCharacterData.Special, 1),
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        new CharacterRule(EnglishCharacterData.LowerCase, 1)));
  }


  /**
   * Private constructor for factory class.
   */
  private ShannonEntropyFactory() {}


  /**
   * Creates a new shannon entropy.
   *
   * @param  passwordRules  to aid in entropy calculation
   * @param  passwordData  to aid in entropy calculation
   *
   * @return  shannon entropy
   */
  public static ShannonEntropy createEntropy(final List<? extends Rule> passwordRules, final PasswordData passwordData)
  {
    final boolean dictionaryCheck = passwordRules.stream().anyMatch(
      rule -> rule instanceof AbstractDictionaryRule && ((AbstractDictionaryRule) rule).getDictionary().size() > 0);
    return createEntropy(dictionaryCheck, passwordData);
  }


  /**
   * Creates a new shannon entropy.
   *
   * @param  dictionaryCheck   whether or not a common passwords dictionary is checked against the password
   * @param  passwordData  to aid in entropy calculation
   *
   * @return  shannon entropy
   */
  public static ShannonEntropy createEntropy(final boolean dictionaryCheck, final PasswordData passwordData)
  {
    if (!passwordData.getOrigin().equals(PasswordData.Origin.User)) {
      throw new IllegalArgumentException("Password data must have an origin of " + PasswordData.Origin.User);
    }
    final boolean compositionCheck = hasComposition(passwordData);
    return new ShannonEntropy(dictionaryCheck, compositionCheck, passwordData.getCharacterCount());
  }


  /**
   * Checks whether the supplied passwordData has composition. (As suggested by NIST SP-800-63-1)
   *
   * @param  passwordData  to check for composition
   *
   * @return  true if valid, false otherwise
   */
  private static boolean hasComposition(final PasswordData passwordData)
  {
    return COMPOSITION_VALIDATOR.validate(passwordData).isValid();
  }
}

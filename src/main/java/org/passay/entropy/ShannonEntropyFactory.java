/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.ArrayList;
import java.util.List;
import org.passay.AbstractDictionaryRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;

/**
 * Factory for creating {@link ShannonEntropy} from password rules and password data.
 *
 * @author  Middleware Services
 */
public final class ShannonEntropyFactory
{

  /** Number of character characteristics rules to enforce for a password to have composition. */
  private static final int COMPOSITION_CHARACTERISTICS_REQUIREMENT = 4;


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
  public static ShannonEntropy createEntropy(final List<Rule> passwordRules, final PasswordData passwordData)
  {
    if (!passwordData.getOrigin().equals(PasswordData.Origin.User)) {
      throw new IllegalArgumentException("Password data must have an origin of " + PasswordData.Origin.User);
    }
    final boolean dictionaryCheck = passwordRules.stream().filter(
      rule -> AbstractDictionaryRule.class.isAssignableFrom(
        rule.getClass()) && ((AbstractDictionaryRule) rule).getDictionary().size() > 0).count() > 0;
    final boolean compositionCheck = hasComposition(passwordData);
    return new ShannonEntropy(dictionaryCheck, compositionCheck, passwordData.getPassword().length());
  }


  /**
   * Checks whether a given passwordData has composition. (As suggested by NIST SP-800-63-1)
   *
   * @param passwordData Password to check for composition
   *
   * @return true if valid, false otherwise
   */
  protected static boolean hasComposition(final PasswordData passwordData)
  {
    final CharacterCharacteristicsRule compositionRule = new CharacterCharacteristicsRule();
    compositionRule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
    compositionRule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));
    compositionRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    compositionRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
    compositionRule.setNumberOfCharacteristics(COMPOSITION_CHARACTERISTICS_REQUIREMENT);
    final List<Rule> rules = new ArrayList<>();
    rules.add(compositionRule);
    final PasswordValidator compositionValidator = new PasswordValidator(rules);
    return compositionValidator.validate(passwordData).isValid();
  }
}

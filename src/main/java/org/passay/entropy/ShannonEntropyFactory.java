/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import org.passay.AbstractDictionaryRule;
import org.passay.AllowedCharacterRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.Rule;

/**
 * Factory for creating {@link ShannonEntropy} from password rules and password data.
 * <p>
 * See {@link ShannonEntropy}.
 *
 * @author  Middleware Services
 */
public final class ShannonEntropyFactory
{


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
    boolean dictionaryCheck = false;
    boolean compositionCheck = false;
    for (Rule rule : passwordRules) {
      if (!compositionCheck) {
        if (rule instanceof CharacterCharacteristicsRule) {
          final CharacterCharacteristicsRule characteristicRule = (CharacterCharacteristicsRule) rule;
          for (CharacterRule r : characteristicRule.getRules()) {
            compositionCheck = hasComposition(r, r.getValidCharacters(), passwordData);
            if (compositionCheck) {
              break;
            }
          }
        } else if (rule instanceof CharacterRule) {
          final CharacterRule characterRule = (CharacterRule) rule;
          compositionCheck = hasComposition(characterRule, characterRule.getValidCharacters(), passwordData);
        } else if (rule instanceof AllowedCharacterRule) {
          final AllowedCharacterRule allowedCharacterRule = (AllowedCharacterRule) rule;
          compositionCheck = hasComposition(
            rule,
            String.valueOf(allowedCharacterRule.getAllowedCharacters()),
            passwordData);
        }
      }
      if (AbstractDictionaryRule.class.isAssignableFrom(rule.getClass())) {
        dictionaryCheck = true;
      }
    }
    return new ShannonEntropy(dictionaryCheck, compositionCheck, passwordData.getPassword().length());
  }


  /**
   * Determine if the supplied rule and characters has composition. (As defined by NIST SP-800-63-1 a password has
   * composition rules if it checks for anything other than lowercase letter password).
   *
   * @param  rule  to validate for the matching composition rule
   * @param  characters  to check for composition
   * @param  passwordData  with the contents of the password.
   *
   * @return  whether the supplied rule has the correct composition
   */
  private static boolean hasComposition(final Rule rule, final String characters, final PasswordData passwordData)
  {
    if (characters != null) {
      for (char c : characters.toCharArray()) {
        if (EnglishCharacterData.LowerCase.getCharacters().indexOf(c) == -1 && rule.validate(passwordData).isValid()) {
          //found at least 1 non-lowercase character in CharacterRule
          return true;
        }
      }
    }
    return false;
  }
}

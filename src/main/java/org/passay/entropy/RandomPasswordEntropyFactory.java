/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.logic.PasswordData;
import org.passay.rule.Rule;

/**
 * Factory for creating {@link RandomPasswordEntropy} from password rules and password data.
 *
 * @author  Middleware Services
 */
public final class RandomPasswordEntropyFactory
{


  /**
   * Private constructor for factory class.
   */
  private RandomPasswordEntropyFactory() {}


  /**
   * Creates a new random password entropy.
   *
   * @param  passwordRules  to aid in entropy calculation
   * @param  passwordData  to aid in entropy calculation
   *
   * @return  random password entropy
   */
  public static RandomPasswordEntropy createEntropy(
    final List<? extends Rule> passwordRules,
    final PasswordData passwordData)
  {
    if (!passwordData.getOrigin().equals(PasswordData.Origin.Generated)) {
      throw new IllegalArgumentException("Password data must have an origin of " + PasswordData.Origin.Generated);
    }
    final Set<Character> uniqueCharacters = new HashSet<>();
    passwordRules.forEach(rule -> {
      if (rule instanceof CharacterCharacteristicsRule) {
        final CharacterCharacteristicsRule characteristicRule = (CharacterCharacteristicsRule) rule;
        characteristicRule.getRules().forEach(characterRule ->
          uniqueCharacters.addAll(getUniqueCharacters(characterRule.getValidCharacters())));
      } else if (rule instanceof CharacterRule) {
        final CharacterRule characterRule = (CharacterRule) rule;
        uniqueCharacters.addAll(getUniqueCharacters(characterRule.getValidCharacters()));
      } else if (rule instanceof AllowedCharacterRule) {
        final AllowedCharacterRule allowedCharacterRule = (AllowedCharacterRule) rule;
        uniqueCharacters.addAll(getUniqueCharacters(String.valueOf(allowedCharacterRule.getAllowedCharacters())));
      }
    });
    if (uniqueCharacters.isEmpty()) {
      throw new IllegalArgumentException(
        "Password rules must contain at least 1 unique character by CharacterRule definition");
    }
    return new RandomPasswordEntropy(uniqueCharacters.size(), passwordData.getPassword().length());
  }


  /**
   * Returns the set of unique characters in the supplied string
   *
   * @param  characters  used to populate unique characters set with from the rule
   *
   * @return  unique characters
   */
  private static Set<Character> getUniqueCharacters(final String characters)
  {
    final Set<Character> uniqueCharacters = new HashSet<>();
    if (characters != null) {
      for (char c : characters.toCharArray()) {
        uniqueCharacters.add(c);
      }
    }
    return uniqueCharacters;
  }
}

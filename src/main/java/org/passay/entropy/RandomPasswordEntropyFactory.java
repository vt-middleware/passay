/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.passay.AllowedCharacterRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.PasswordData;
import org.passay.Rule;

/**
 * Factory for creating {@link RandomPasswordEntropy} from password rules and password data.
 * <p>
 * See {@link RandomPasswordEntropy}.
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
  public static RandomPasswordEntropy createEntropy(final List<Rule> passwordRules, final PasswordData passwordData)
  {
    if (!passwordData.getOrigin().equals(PasswordData.Origin.Generated)) {
      throw new IllegalArgumentException("Password data must have an origin of " + PasswordData.Origin.Generated);
    }
    final ConcurrentHashMap<Character, Boolean> uniqueCharacters = new ConcurrentHashMap<>();
    passwordRules.parallelStream().forEach((rule) -> {
      if (rule instanceof CharacterCharacteristicsRule) {
        final CharacterCharacteristicsRule characteristicRule = (CharacterCharacteristicsRule) rule;
        characteristicRule.getRules().parallelStream().forEach((characterRule) ->
          putUniqueCharacters(uniqueCharacters, characterRule.getValidCharacters()));
      } else if (rule instanceof CharacterRule) {
        final CharacterRule characterRule = (CharacterRule) rule;
        putUniqueCharacters(uniqueCharacters, characterRule.getValidCharacters());
      } else if (rule instanceof AllowedCharacterRule) {
        final AllowedCharacterRule allowedCharacterRule = (AllowedCharacterRule) rule;
        putUniqueCharacters(uniqueCharacters, String.valueOf(allowedCharacterRule.getAllowedCharacters()));
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
   * @param  uniqueCharacters used to populate keys for unique characters much like the underlying mechanism
   * in {@link java.util.HashSet}s
   * @param  characters  used to populate unique characters set with from the rule
   *
   */
  private static void putUniqueCharacters(
          final ConcurrentHashMap<Character, Boolean> uniqueCharacters,
          final String characters)
  {
    if (characters != null) {
      for (char c : characters.toCharArray()) {
        uniqueCharacters.put(c, Boolean.TRUE);
      }
    }
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.rule.AllowedCharacterRule;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
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
    final Set<String> uniqueCharacters = new HashSet<>();
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
        uniqueCharacters.addAll(getUniqueCharacters(allowedCharacterRule.getAllowedCharacters()));
      }
    });
    if (uniqueCharacters.isEmpty()) {
      throw new IllegalArgumentException(
        "Password rules must contain at least 1 unique character by CharacterRule definition");
    }
    return new RandomPasswordEntropy(uniqueCharacters.size(), passwordData.getCharacterCount());
  }


  /**
   * Returns the set of unique characters in the supplied string
   *
   * @param  characters  used to populate unique characters set with from the rule
   *
   * @return  unique characters
   */
  private static Set<String> getUniqueCharacters(final CharSequence characters)
  {
    if (characters != null && characters.length() > 0) {
      return characters.codePoints().mapToObj(PassayUtils::toString).collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }
}

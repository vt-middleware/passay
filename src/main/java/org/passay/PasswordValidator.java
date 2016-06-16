/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The central component for evaluating multiple password rules against a candidate password.
 *
 * @author  Middleware Services
 */

public class PasswordValidator implements Rule
{

  /** Password rules. */
  private final List<Rule> passwordRules;

  /** Message resolver. */
  private final MessageResolver messageResolver;


  /**
   * Creates a new password validator with a {@link PropertiesMessageResolver}.
   *
   * @param  rules  to validate
   */
  public PasswordValidator(final List<Rule> rules)
  {
    this(new PropertiesMessageResolver(), rules);
  }


  /**
   * Creates a new password validator.
   *
   * @param  resolver  message resolver.
   * @param  rules  to validate
   */
  public PasswordValidator(final MessageResolver resolver, final List<Rule> rules)
  {
    messageResolver = resolver;
    passwordRules = rules;
  }


  /**
   * Validates the supplied password data against the rules in this validator.
   *
   * @param  passwordData  to validate
   *
   * @return  rule result
   */
  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    passwordRules.stream().map((rule) -> rule.validate(passwordData)).filter((rr) -> !rr.isValid()).forEach((rr) -> {
      result.setValid(false);
      result.getDetails().addAll(rr.getDetails());
    });
    return result;
  }


  /**
   * Calculates the entropy of the given {@link PasswordData} based on the password rules specified in this instance.
   * <p>
   * <b>It's important to note that this method does NOT take into account {@link IllegalRegexRule} or
   * {@link AllowedRegexRule}</b> as the regular expressions driving the rules may be negative matches.
   *
   * @param  passwordData  to validate
   * @throws org.passay.EntropyException Thrown when passwordData {@link PasswordOrigin} is not implemented
   * by this method or if the required {@link CharacterRule} instances are unavailable in the passwordRules of
   * this validator instance.
   *
   * @see <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   * @return  rule result
   */
  public Double getEntropyBits(final PasswordData passwordData)
          throws EntropyException
  {
    final Set<Character> uniqueCharacters = new HashSet<>();
    final Map<String, Boolean> entropyCharacteristics = new HashMap<>();
    if (passwordRules == null || passwordRules.isEmpty()) {
      throw new IllegalStateException("passwordRules cannot be null or empty");
    }
    passwordRules.stream().forEach((rule) -> {
      if (rule instanceof CharacterCharacteristicsRule) {
        final CharacterCharacteristicsRule characteristicRule = (CharacterCharacteristicsRule) rule;
        characteristicRule.getRules().forEach((characterRule) -> {
          hasComposition(
                  characterRule, entropyCharacteristics, characterRule.getCharacters(), uniqueCharacters, passwordData);
        });
      } else if (implementsInterface(rule, CharacterData.class)) {
        final CharacterData characterData = (CharacterData) rule;
        hasComposition(rule, entropyCharacteristics, characterData.getCharacters(), uniqueCharacters, passwordData);
      } else if (AbstractDictionaryRule.class.isAssignableFrom(rule.getClass())) {
        entropyCharacteristics.put("dictionary", true);
      }
    });
    if (uniqueCharacters.isEmpty()) {
      throw new EntropyException(
              "password validator instance must contain at least 1 unique character by CharacterRule definition");
    }
    if (passwordData.getOrigin().equals(PasswordOrigin.RANDOM_GENERATED)) {
      return PasswordUtils.getEntropyBitsForRandomlySelectedPassword(
              uniqueCharacters.size(),
              passwordData.getPassword().length()
      );
    } else if (passwordData.getOrigin().equals(PasswordOrigin.USER_GENERATED)) {
      return PasswordUtils.getEntropyBitsForUserSelectedPassword(
              entropyCharacteristics.getOrDefault("dictionary", false),
              entropyCharacteristics.getOrDefault("composition", false),
              passwordData.getPassword().length()
      );
    } else {
      throw new EntropyException("unknown passwordData origin");
    }
  }


  /**
   * Checks whether or not an object instance implements a given interface class.
   *
   * @param object Object instance to check
   * @param interfaze Interface to test if the object implements
   * @return true if the instance implements the interface, false otherwise
   */
  private static boolean implementsInterface(final Object object, final Class interfaze)
  {
    for (Class c : object.getClass().getInterfaces()) {
      if (c.equals(interfaze)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Private function used to populate a set of unique characters from a given input of unique characters
   * for entropy calculation.
   *
   * @param rule Rule to validate for the matching composition rule (As defined by NIST SP-800-63-1 a password
   * has composition rules if it checks for anything other than lowercase letter password)
   * @param entropyCharacteristics The map to hold entropy characteristics to provide to entropy function.
   * @param characters Characters used to populate unique characters set with from the rule
   * @param uniqueCharacters Unique characters set populated to provide to entropy function.
   * @param passwordData The passwordData to check against having a composition rule.
   */
  private void hasComposition(final Rule rule, final Map<String, Boolean> entropyCharacteristics,
          final String characters, final Set<Character> uniqueCharacters,
          final PasswordData passwordData)
  {
    if (characters != null) {
      for (char c : characters.toCharArray()) {
        if (!entropyCharacteristics.getOrDefault("composition", false) && EnglishCharacterData.LowerCase.getCharacters()
                .indexOf(c) == -1) {
          //found at least 1 non-lowercase character in CharacterRule
          entropyCharacteristics.put("composition", rule.validate(passwordData).isValid());
        }
        uniqueCharacters.add(c);
      }
    }
  }


  /**
   * Returns a list of human-readable messages by iterating over the details in a failed rule result.
   *
   * @param  result  failed rule result.
   *
   * @return  list of human-readable messages describing the reason(s) for validation failure.
   */
  public List<String> getMessages(final RuleResult result)
  {
    final List<String> messages = result.getDetails().stream().map(
      messageResolver::resolve).collect(Collectors.toList());
    return messages;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::passwordRules=%s,messageResolver=%s",
        getClass().getName(),
        hashCode(),
        passwordRules,
        messageResolver);
  }
}

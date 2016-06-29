/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import java.util.regex.Pattern;
import org.passay.AbstractDictionaryRule;
import org.passay.PasswordData;
import org.passay.Rule;

/**
 * Factory for creating {@link ShannonEntropy} from password rules and password data.
 *
 * @author  Middleware Services
 */
public final class ShannonEntropyFactory
{

  /**
   * Pattern to search for uppercase characters.  (As suggested by NIST SP-800-63-1 a password has
   * composition rules if it checks for anything other than lowercase letter password).
   */
  private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

  /**
   * Pattern to search for non alphabetic characters. (As suggested by NIST SP-800-63-1 a password has
   * composition rules if it checks for a rule that requires both upper case and non-alphabetic characters).
   */
  private static final Pattern NONALPHABETIC_PATTERN = Pattern.compile("[^a-zA-Z]");

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
    final boolean dictionaryCheck = passwordRules.stream()
            .filter(rule -> AbstractDictionaryRule.class.isAssignableFrom(rule.getClass()) &&
                    ((AbstractDictionaryRule) rule).getDictionary().size() > 0).count() > 0;
    final boolean compositionCheck = UPPERCASE_PATTERN
            .matcher(passwordData.getPassword()).find() &&
            NONALPHABETIC_PATTERN.matcher(passwordData.getPassword()).find();
    return new ShannonEntropy(dictionaryCheck, compositionCheck, passwordData.getPassword().length());
  }
}

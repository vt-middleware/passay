/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Validates that a password does not contain too many occurrences of the same character.
 *
 * @author  Amichai Rothman
 */
public class CharacterOccurrencesRule implements Rule
{

  /** Error code for too many occurrences of a character. */
  public static final String ERROR_CODE = "TOO_MANY_OCCURRENCES";

  /** Maximum umber of occurrences to allow. */
  protected int maxOccurrences;

  /**
   * Creates a new character occurrences rule.
   *
   * @param  maxAllowedOccurrences  the maximum allowed number of occurrences of any character
   */
  public CharacterOccurrencesRule(final int maxAllowedOccurrences)
  {
    maxOccurrences = maxAllowedOccurrences;
  }

  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult();
    final String password = passwordData.getPassword();
    final int[] codePoints = IntStream.concat(password.codePoints(), IntStream.of(Integer.MAX_VALUE)).toArray();
    Arrays.sort(codePoints);
    int repeat = 1;
    for (int i = 1; i < codePoints.length; i++) {
      if (codePoints[i] == codePoints[i - 1]) {
        repeat++;
      } else {
        if (repeat > maxOccurrences) {
          result.addError(
            ERROR_CODE, createRuleResultDetailParameters(PasswordUtils.toString(codePoints[i - 1]), repeat));
        }
        repeat = 1;
      }
    }
    return result;
  }

  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  c  the character that occurred too many times
   * @param  occurrences the number of times the character occurred
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String c, final int occurrences)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("matchingCharacter", c);
    m.put("matchingCharacterCount", occurrences);
    m.put("maximumOccurrences", maxOccurrences);
    return m;
  }

  @Override
  public String toString()
  {
    return String.format(
      "%s@%h::maxOccurrences=%s",
      getClass().getName(),
      hashCode(),
      maxOccurrences);
  }
}

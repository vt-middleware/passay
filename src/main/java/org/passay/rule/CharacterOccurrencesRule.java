/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.SuccessRuleResult;
import org.passay.UnicodeString;

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
  protected final int maxOccurrences;

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
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<RuleResultDetail> details = new ArrayList<>();
    final String password = passwordData.getPassword();
    final int[] codePoints = IntStream.concat(password.codePoints(), IntStream.of(Integer.MAX_VALUE)).toArray();
    Arrays.sort(codePoints);
    int repeat = 1;
    for (int i = 1; i < codePoints.length; i++) {
      if (codePoints[i] == codePoints[i - 1]) {
        repeat++;
      } else {
        if (repeat > maxOccurrences) {
          details.add(
            new RuleResultDetail(
                ERROR_CODE,
                createRuleResultDetailParameters(UnicodeString.toString(codePoints[i - 1]), repeat)));
        }
        repeat = 1;
      }
    }
    return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
  }

  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  character  the character that occurred too many times
   * @param  occurrences the number of times the character occurred
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final String character, final int occurrences)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("matchingCharacter", character);
    m.put("matchingCharacterCount", occurrences);
    m.put("maximumOccurrences", maxOccurrences);
    return m;
  }

  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::maxOccurrences=" + maxOccurrences;
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * Rule for determining if a password contains multiple sequences of repeating characters.
 * <p>
 * For example, the password "11a22b333xyz" will fail validation of this rule with
 * a sequence length of 2 and sequence count of 3, since it contains 3 sequences (or more)
 * of 2 repeating characters (or more).
 *
 * @author  Amichai Rothman
 */
public class RepeatCharactersRule implements Rule
{

  /** Error code for regex validation failures. */
  public static final String ERROR_CODE = "ILLEGAL_REPEATED_CHARS";

  /** Default length of sequence, value is {@value}. */
  public static final int DEFAULT_SEQUENCE_LENGTH = 5;

  /** Default number of sequences, value is {@value}. */
  public static final int DEFAULT_SEQUENCE_COUNT = 1;

  /** Number of repeating characters to match in each sequence. */
  protected int sequenceLength;

  /** Number of sequences of repeating characters to match. */
  protected int sequenceCount;


  /**
   * Creates a new repeat characters rule for a single sequence of the default sequence length.
   */
  public RepeatCharactersRule()
  {
    this(DEFAULT_SEQUENCE_LENGTH);
  }

  /**
   * Creates a new repeat characters rule for a single sequence of the given length.
   *
   * @param  length  sequence length
   */
  public RepeatCharactersRule(final int length)
  {
    this(length, DEFAULT_SEQUENCE_COUNT);
  }

  /**
   * Creates a new repeat characters rule for the given number of sequences of the given length.
   *
   * @param  length  sequence length
   * @param  count  sequence count
   */
  public RepeatCharactersRule(final int length, final int count)
  {
    if (length < 2) {
      throw new IllegalArgumentException("Sequence length must be greater than or equal to 2");
    }
    if (count < 1) {
      throw new IllegalArgumentException("Sequence count must be greater than or equal to 1");
    }
    sequenceLength = length;
    sequenceCount = count;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final RuleResult result = new RuleResult();
    final List<String> matches = new ArrayList<>();
    final String password = passwordData.getPassword() + '\uffff';
    final int max = password.length() - 1;
    int count = 0;
    int repeat = 1;
    int prev = -1;
    int i = 0;
    while (i <= max) {
      final int curr = password.codePointAt(i);
      if (curr == prev) {
        repeat++;
      } else {
        if (repeat >= sequenceLength) {
          final String match = password.substring(i - Character.charCount(prev) * repeat, i);
          matches.add(match);
          count++;
        }
        repeat = 1;
      }
      prev = curr;
      i += Character.charCount(curr);
    }
    if (count >= sequenceCount) {
      result.addError(ERROR_CODE, createRuleResultDetailParameters(matches));
    }
    return result;
  }

  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  matches the illegal matched sequences
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final List<String> matches)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("sequenceLength", sequenceLength);
    m.put("sequenceCount", sequenceCount);
    m.put("matchesCount", matches.size());
    m.put("matches", matches);
    return m;
  }
}

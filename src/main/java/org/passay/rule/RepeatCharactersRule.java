/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.logic.PasswordData;
import org.passay.rule.result.RuleResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
   * @param  sl  sequence length
   */
  public RepeatCharactersRule(final int sl)
  {
    this(sl, DEFAULT_SEQUENCE_COUNT);
  }

  /**
   * Creates a new repeat characters rule for the given number of sequences of the given length.
   *
   * @param  sl  sequence length
   * @param  sc  sequence count
   */
  public RepeatCharactersRule(final int sl, final int sc)
  {
    if (sl < 2 || sc < 1) {
      throw new IllegalArgumentException("invalid sequence length or sequence count");
    }
    sequenceLength = sl;
    sequenceCount = sc;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult();
    final List<String> matches = new ArrayList<>();
    final String password = passwordData.getPassword() + '\uffff';
    final int max = password.length() - 1;
    int count = 0;
    int repeat = 1;
    int prev = -1;
    for (int i = 0; i <= max; i++) {
      final int c = password.charAt(i);
      if (c == prev) {
        repeat++;
      } else {
        if (repeat >= sequenceLength) {
          final String match = password.substring(i - repeat, i);
          matches.add(match);
          count++;
        }
        repeat = 1;
      }
      prev = c;
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

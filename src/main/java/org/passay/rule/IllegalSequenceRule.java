/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
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
import org.passay.data.CharacterSequence;
import org.passay.data.SequenceData;

/**
 * Password validation rule that prevents illegal sequences of characters, e.g. keyboard, alphabetical, numeric.
 *
 * @author  Middleware Services
 */
public class IllegalSequenceRule implements Rule
{

  /** Default length of keyboard sequence, value is {@value}. */
  public static final int DEFAULT_SEQUENCE_LENGTH = 5;

  /** Minimum length of keyboard sequence, value is {@value}. */
  public static final int MINIMUM_SEQUENCE_LENGTH = 3;

  /** Sequence data for this rule. */
  protected final SequenceData sequenceData;

  /** Number of characters in sequence to match. */
  protected final int sequenceLength;

  /** Whether or not to wrap a sequence when searching for matches. */
  protected final boolean wrapSequence;

  /** Whether to report all sequence matches or just the first. */
  protected final boolean reportAllFailures;


  /**
   * Creates a new sequence rule with the supplied list of characters.
   *
   * @param  data  sequence data for this rule
   */
  public IllegalSequenceRule(final SequenceData data)
  {
    this(data, DEFAULT_SEQUENCE_LENGTH, false, true);
  }


  /**
   * Creates a new sequence rule with the supplied list of characters.
   *
   * @param  data  sequence data for this rule
   * @param  length  sequence length
   * @param  wrap  whether to wrap sequences
   */
  public IllegalSequenceRule(final SequenceData data, final int length, final boolean wrap)
  {
    this(data, length, wrap, true);
  }


  /**
   * Creates a new sequence rule with the supplied list of characters.
   *
   * @param  data  sequence data for this rule
   * @param  length  sequence length
   * @param  wrap  whether to wrap sequences
   * @param  reportAll  whether to report all sequence matches or just the first
   */
  public IllegalSequenceRule(final SequenceData data, final int length, final boolean wrap, final boolean reportAll)
  {
    if (length < MINIMUM_SEQUENCE_LENGTH) {
      throw new IllegalArgumentException("Sequence length must be >= " + MINIMUM_SEQUENCE_LENGTH);
    }
    sequenceData = PassayUtils.assertNotNullArgOr(
      data, v -> v.getSequences() == null && v.getErrorCode() == null, "Sequence data cannot be null or contain null");
    sequenceLength = length;
    wrapSequence = wrap;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the sequence length for this rule.
   *
   * @return  sequence length
   */
  public int getSequenceLength()
  {
    return sequenceLength;
  }


  /**
   * Returns the sequence data for this rule.
   *
   * @return  sequence data
   */
  public SequenceData getSequenceData()
  {
    return sequenceData;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final List<RuleResultDetail> details = new ArrayList<>();
    final int[] codePoints = IntStream.concat(
      passwordData.getPassword().codePoints(), IntStream.of('\uffff')).toArray();
    final UnicodeString password = new UnicodeString(codePoints);
    try {
      final StringBuilder match = new StringBuilder(password.codePointCount());
      for (CharacterSequence cs : sequenceData.getSequences()) {
        final int csLength = cs.length();
        int direction = 0;
        int prevPosition = -1;
        int i = 0;
        while (i < password.codePointCount()) {
          final int cp = password.codePointAt(i);
          final int position = indexOf(cs, cp);
          // set diff to +1 for increase in sequence, -1 for decrease, anything else for neither
          int diff = (position | prevPosition) < 0 ? 0 : position - prevPosition;
          if (wrapSequence && (diff == csLength - 1 || diff == 1 - csLength)) {
            diff -= Integer.signum(diff) * csLength;
          }
          // if we have a sequence and reached its end, add it to result
          if (diff != direction && match.length() >= sequenceLength) {
            addError(details, match.toString());
          }
          // update the current potential sequence
          if (diff == 1 || diff == -1) {
            if (diff != direction) {
              match.delete(0, match.length() - 1);
              direction = diff;
            }
          } else {
            match.setLength(0);
            direction = 0;
          }
          match.append(PassayUtils.toString(cp));
          prevPosition = position;
          i++;
        }
      }
      return details.isEmpty() ? new SuccessRuleResult() : new FailureRuleResult(details);
    } finally {
      PassayUtils.clear(codePoints);
      password.clear();
    }
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "sequenceData=" + sequenceData + ", " +
      "sequenceLength=" + sequenceLength + ", " +
      "wrapSequence=" + wrapSequence + ", " +
      "reportAllFailures=" + reportAllFailures;
  }


  /**
   * Returns the index of the given character within the given sequence,
   * or -1 if it is not found.
   *
   * @param  sequence  a sequence of characters
   * @param  cp  the code point to find in the character sequence
   *
   * @return  the index of the character within the sequence, or -1
   */
  private int indexOf(final CharacterSequence sequence, final int cp)
  {
    for (int i = 0; i < sequence.length(); i++) {
      if (sequence.matches(i, cp)) {
        return i;
      }
    }
    return -1;
  }


  /**
   * Adds a validation error to a result.
   *
   * @param  details  list of rule results to add a new rule result error
   * @param  match  the illegal sequence in the password that caused the error
   */
  private void addError(final List<RuleResultDetail> details, final String match)
  {
    if (reportAllFailures || details.isEmpty()) {
      final Map<String, Object> m = new LinkedHashMap<>();
      m.put("sequence", match);
      details.add(new RuleResultDetail(sequenceData.getErrorCode(), m));
    }
  }
}

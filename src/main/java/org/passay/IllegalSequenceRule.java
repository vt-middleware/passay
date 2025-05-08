/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

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
  protected int sequenceLength;

  /** Whether or not to wrap a sequence when searching for matches. */
  protected boolean wrapSequence;

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;


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
   * @param  sl  sequence length
   * @param  wrap  whether to wrap sequences
   */
  public IllegalSequenceRule(final SequenceData data, final int sl, final boolean wrap)
  {
    this(data, sl, wrap, true);
  }


  /**
   * Creates a new sequence rule with the supplied list of characters.
   *
   * @param  data  sequence data for this rule
   * @param  sl  sequence length
   * @param  wrap  whether to wrap sequences
   * @param  reportAll  whether to report all sequence matches or just the first
   */
  public IllegalSequenceRule(final SequenceData data, final int sl, final boolean wrap, final boolean reportAll)
  {
    if (sl < MINIMUM_SEQUENCE_LENGTH) {
      throw new IllegalArgumentException(String.format("sequence length must be >= %s", MINIMUM_SEQUENCE_LENGTH));
    }
    sequenceData = data;
    sequenceLength = sl;
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
    final RuleResult result = new RuleResult();
    final String password = passwordData.getPassword() + '\uffff';
    final StringBuilder match = new StringBuilder(password.length());
    for (CharacterSequence cs : sequenceData.getSequences()) {
      final int csLength = cs.length();
      int direction = 0;
      int prevPosition = -1;
      int i = 0;
      while (i < password.length()) {
        final int cp = password.codePointAt(i);
        final int position = indexOf(cs, cp);
        // set diff to +1 for increase in sequence, -1 for decrease, anything else for neither
        int diff = (position | prevPosition) < 0 ? 0 : position - prevPosition;
        if (wrapSequence && (diff == csLength - 1 || diff == 1 - csLength)) {
          diff -= Integer.signum(diff) * csLength;
        }
        // if we have a sequence and reached its end, add it to result
        if (diff != direction && match.length() >= sequenceLength) {
          addError(result, match.toString());
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
        match.append(UnicodeString.toString(cp));
        prevPosition = position;
        i += Character.charCount(cp);
      }
    }
    return result;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::length=%d,wrap=%s,reportAllFailures=%s",
        getClass().getName(),
        hashCode(),
        sequenceLength,
        wrapSequence,
        reportAllFailures);
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
   * @param  result  the rule result to which the error is added
   * @param  match  the illegal sequence in the password that caused the error
   */
  private void addError(final RuleResult result, final String match)
  {
    if (reportAllFailures || result.getDetails().isEmpty()) {
      final Map<String, Object> m = new LinkedHashMap<>();
      m.put("sequence", match);
      result.addError(sequenceData.getErrorCode(), m);
    }
  }

}

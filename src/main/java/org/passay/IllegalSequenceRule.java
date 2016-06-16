/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Password validation rule that prevents illegal sequences of characters, e.g. keyboard, alphabetical, numeric.
 *
 * @author  Middleware Services
 */
public final class IllegalSequenceRule implements Rule
{

  /** Default length of keyboard sequence, value is {@value}. */
  public static final int DEFAULT_SEQUENCE_LENGTH = 5;

  /** Minimum length of keyboard sequence, value is {@value}. */
  public static final int MINIMUM_SEQUENCE_LENGTH = 3;

  /** Sequence data for this rule. */
  protected final SequenceData sequenceData;

  /** Number of characters in sequence to match. */
  protected int sequenceLength = DEFAULT_SEQUENCE_LENGTH;

  /** Whether or not to wrap a sequence when searching for matches. */
  protected boolean wrapSequence;

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures = true;


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


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final String password = passwordData.getPassword();
    final int max = password.length() - sequenceLength + 1;
    SequenceIterator sequence;
    int position;
    char c;
    for (int i = 0; i < sequenceData.getSequences().length; i++) {
      for (int j = 0; j < max; j++) {
        sequence = newSequenceIterator(sequenceData.getSequences()[i], password.charAt(j));
        if (sequence != null) {
          position = j;
          while (sequence.forward()) {
            c = password.charAt(++position);
            if (sequence.matches(c)) {
              sequence.addMatchCharacter(c);
            } else {
              break;
            }
          }
          if (sequence.matchCount() == sequenceLength) {
            recordFailure(result, sequence.matchString());
          }
          sequence.reset();
          position = j;
          while (sequence.backward()) {
            c = password.charAt(++position);
            if (sequence.matches(c)) {
              sequence.addMatchCharacter(c);
            } else {
              break;
            }
          }
          if (sequence.matchCount() == sequenceLength) {
            recordFailure(result, sequence.matchString());
          }
        }
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
   * Creates an iterator that iterates over a character sequence positioned at the first matching character, if any, in
   * the given password.
   *
   * @param  sequence  defined sequence of illegal characters.
   * @param  first  first character to match in character sequence.
   *
   * @return  forward sequence iterator.
   */
  private SequenceIterator newSequenceIterator(final CharacterSequence sequence, final char first)
  {
    for (int i = 0; i < sequence.length(); i++) {
      if (sequence.matches(i, first)) {
        final SequenceIterator s = new SequenceIterator(sequence, i, sequenceLength, wrapSequence);
        s.addMatchCharacter(first);
        return s;
      }
    }
    return null;
  }


  /**
   * Records a validation failure.
   *
   * @param  result  rule result holding failure details.
   * @param  match  illegal string matched in the password that caused failure.
   */
  private void recordFailure(final RuleResult result, final String match)
  {
    if (reportAllFailures || result.getDetails().isEmpty()) {
      final Map<String, Object> m = new LinkedHashMap<>();
      m.put("sequence", match);
      result.setValid(false);
      result.getDetails().add(new RuleResultDetail(sequenceData.getErrorCode(), m));
    }
  }


  /**
   * Iterates over a {@link CharacterSequence} and stores matched characters.
   *
   * @author  Middleware Services
   */
  private class SequenceIterator
  {

    /** Defined illegal character sequence. */
    private final CharacterSequence illegal;

    /** 0-based iterator start position. */
    private final int start;

    /** Number of characters to iterate over. */
    private final int length;

    /** Index upper bound. */
    private int ubound;

    /** Index lower bound. */
    private int lbound;

    /** Current 0-based iterator position. */
    private int position;

    /** Stores matched characters. */
    private final StringBuilder matches;


    /**
     * Creates a new sequence.
     *
     * @param  sequence  Illegal sequence of characters.
     * @param  startIndex  in the characters array
     * @param  count  length of this sequence
     * @param  wrap  whether this sequence wraps
     */
    SequenceIterator(final CharacterSequence sequence, final int startIndex, final int count, final boolean wrap)
    {
      illegal = sequence;
      start = startIndex;
      length = count;
      lbound = start - length;
      ubound = start + length;
      if (lbound < -1 && !wrap) {
        lbound = -1;
      }
      if (ubound >= sequence.length() && !wrap) {
        ubound = sequence.length();
      }
      position = start;
      matches = new StringBuilder(length);
    }


    /**
     * Advances the iterator one unit in the forward direction.
     *
     * @return  true if characters remain, false otherwise.
     */
    public boolean forward()
    {
      return ++position < ubound;
    }


    /**
     * Advances the iterator one unit in the backward direction.
     *
     * @return  true if characters remain, false otherwise.
     */
    public boolean backward()
    {
      return --position > lbound;
    }


    /** Resets the sequence to its original position and discards all but the initial match character. */
    public void reset()
    {
      position = start;
      matches.delete(1, length);
    }


    /**
     * Determines whether the character at the current iterator position in the illegal sequence matches the given
     * character.
     *
     * @param  c  Character to check for.
     *
     * @return  True if character matches, false otherwise.
     */
    public boolean matches(final char c)
    {
      final int i;
      if (position < 0) {
        i = illegal.length() + position;
      } else if (position >= illegal.length()) {
        i = position - illegal.length();
      } else {
        i = position;
      }
      return illegal.matches(i, c);
    }


    /**
     * Adds the given character to the set of matched characters.
     *
     * @param  c  match character.
     */
    public void addMatchCharacter(final char c)
    {
      matches.append(c);
    }


    /**
     * Returns the number of matched characters.
     *
     * @return  matched character count.
     */
    public int matchCount()
    {
      return matches.length();
    }


    /**
     * Returns the string of matched characters.
     *
     * @return  match string.
     */
    public String matchString()
    {
      return matches.toString();
    }
  }
}

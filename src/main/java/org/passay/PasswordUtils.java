/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;

/**
 * Provides utility methods for this package.
 *
 * @author  Middleware Services
 */
public final class PasswordUtils
{


  /** Default constructor. */
  private PasswordUtils() {}

  /**
   * Returns all the characters in the input string that are also in the characters array.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(final String characters, final String input)
  {
    return getMatchingCharacters(characters, input, Integer.MAX_VALUE);
  }


  /**
   * Returns all the characters in the input string that are also in the characters array.
   *
   * @param  characters  that contains characters to match
   * @param  input  to search for matches
   * @param  maximumLength maximum length of matching characters
   *
   * @return  matching characters or empty string
   */
  public static String getMatchingCharacters(final String characters, final String input, final int maximumLength)
  {
    final StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      final char c = input.charAt(i);
      if (characters.indexOf(c) != -1) {
        if (sb.length() < maximumLength) {
          sb.append(c);
        } else {
          break;
        }
      }
    }
    return sb.toString();
  }


  /**
   * Returns the log base 2 of a given number.
   * <p>
   * @param number number to get the log2 of
   * <p>
   * @return log2 of number
   */
  public static Double log2(final Double number)
  {
    return Math.log(number) / Math.log(2);
  }


  /**
   * Returns the entropy bits of a randomly generated password given the size of the unique characters used
   * (alphabetSize) and the size of the password.
   * <p>
   * @param alphabetSize size of the alphabet used
   * @param passwordSize size of the password used
   * <p>
   * @see <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   * A1. "Randomly Selected Passwords"
   * <p>
   * @return log2 of number
   */
  public static Double getEntropyBitsForRandomlySelectedPassword(final int alphabetSize, final int passwordSize)
  {
    return log2(Math.pow(alphabetSize, passwordSize));
  }


  /**
   * Returns the entropy bits of a user selected password. See
   * {@link ShannonEntropyEstimate#getEntropyBits(boolean, boolean, int)}.
   * <p>
   * @param hasDictionaryCheck      whether or not a common passwords dictionary is enforced.
   * @param hasCompositionCheck     whether or not a password composition check is enforced.
   * @param passwordSize            size of the password used
   * <p>
   * @return Estimated entropy bits given password properties
   */
  public static Double getEntropyBitsForUserSelectedPassword(
          final boolean hasDictionaryCheck,
          final boolean hasCompositionCheck,
          final int passwordSize)
  {
    return ShannonEntropyEstimate.getEntropyBits(hasDictionaryCheck, hasCompositionCheck, passwordSize);
  }


  /**
   * Concatenates multiple character arrays together.
   *
   * @param  first  array to concatenate. Cannot be null.
   * @param  rest  of the arrays to concatenate. May be null.
   *
   * @return  array containing the concatenation of all parameters
   */
  public static char[] concatArrays(final char[] first, final char[]... rest)
  {
    int totalLength = first.length;
    for (char[] array : rest) {
      if (array != null) {
        totalLength += array.length;
      }
    }

    final char[] result = Arrays.copyOf(first, totalLength);

    int offset = first.length;
    for (char[] array : rest) {
      if (array != null) {
        System.arraycopy(array, 0, result, offset, array.length);
        offset += array.length;
      }
    }
    return result;
  }


  /**
   * Class which defines the static variables for the entropy bits estimate defined in NIST SP-800-63-1 User Selected
   * Passwords.
   * <p>
   * @see <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   * A1. "User Selected Passwords"
   */
  public static class ShannonEntropyEstimate
  {

    /**
     * Length of the first phase.
     */
    public static final int FIRST_PHASE_LENGTH = 1;
    /**
     * Length of the second phase.
     */
    public static final int SECOND_PHASE_LENGTH = 8;
    /**
     * Length of the second phase.
     */
    public static final int THIRD_PHASE_LENGTH = 20;

    /**
     * Entropy bonus of the first phase.
     */
    public static final double FIRST_PHASE_BONUS = 4.0;
    /**
     * Entropy bonus of the second phase.
     */
    public static final double SECOND_PHASE_BONUS = 2.0;
    /**
     * Entropy bonus of the third phase.
     */
    public static final double THIRD_PHASE_BONUS = 1.5;
    /**
     * Array used for determining dictionary entropy "bonus" for calculating the Shannon entropy estimate. *
     */
    public static final int[] SHANNON_DICTIONARY_SIEVE =
            new int[]{0, 0, 0, 4, 5, 6, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0};

    /**
     * Array used for determining composition "bonus" for calculating the Shannon entropy estimate. *
     */
    public static final int[] SHANNON_COMPOSITION_SIEVE =
            new int[]{0, 0, 0, 2, 3, 3, 5, 6};

    /**
     * Returns the entropy bits of a user selected password. This estimate is based on a 94 Character Alphabet, and is a
     * "ballpark" estimate based on Claude Shannon's observations.
     * <p>
     * @param hasDictionaryCheck  whether or not a common passwords dictionary is checked against the password (50,000
     *                            dictionary words are recommended)
     * @param hasCompositionCheck whether or not at least 1 uppercase and special/symbol character is enforced (not
     *                            using common substitutions such as s to $ are recommended)
     * @param passwordSize        size of the password used
     * <p>
     * @see <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
     * A1. "User Selected Passwords"
     * <p>
     * @return Estimated entropy bits given password properties
     */
    public static Double getEntropyBits(
            final boolean hasDictionaryCheck,
            final boolean hasCompositionCheck,
            final int passwordSize)
    {

      Double shannonEntropy = 0.0;
      if (passwordSize > 0) {
        shannonEntropy += FIRST_PHASE_BONUS;
        if (passwordSize > SECOND_PHASE_LENGTH) {
          shannonEntropy += (SECOND_PHASE_LENGTH - FIRST_PHASE_LENGTH) * SECOND_PHASE_BONUS;
          if (passwordSize > THIRD_PHASE_LENGTH) {
            //4th phase bonus is 1 point, so (passwordSize - THIRD_PHASE_LENGTH) will suffice
            shannonEntropy += (THIRD_PHASE_LENGTH - SECOND_PHASE_LENGTH) * THIRD_PHASE_BONUS +
                    (passwordSize - THIRD_PHASE_LENGTH);
          } else {
            shannonEntropy += (passwordSize - SECOND_PHASE_LENGTH) * THIRD_PHASE_BONUS;
          }
        } else {
          shannonEntropy += (passwordSize - FIRST_PHASE_LENGTH) * SECOND_PHASE_BONUS;
        }
        if (hasCompositionCheck) {
          shannonEntropy +=
                  SHANNON_COMPOSITION_SIEVE[passwordSize > SHANNON_COMPOSITION_SIEVE.length ?
                          SHANNON_COMPOSITION_SIEVE.length - 1 : passwordSize - 1];
        }
        if (hasDictionaryCheck) {
          shannonEntropy +=
                  SHANNON_DICTIONARY_SIEVE[passwordSize > SHANNON_DICTIONARY_SIEVE.length ?
                          SHANNON_DICTIONARY_SIEVE.length - 1 : passwordSize - 1];
        }
      }
      return shannonEntropy;
    }
  }
}

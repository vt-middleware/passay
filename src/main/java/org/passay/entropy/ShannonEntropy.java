/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

/**
 * Entropy bits estimate defined in NIST SP-800-63-1 User Selected Passwords.
 *
 * See <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a> A1.
 * "User Selected Passwords"
 *
 * @author  Middleware Services
 */
public class ShannonEntropy implements Entropy
{

  /** Length of the first phase. */
  public static final int FIRST_PHASE_LENGTH = 1;

  /** Length of the second phase. */
  public static final int SECOND_PHASE_LENGTH = 8;

  /** Length of the second phase. */
  public static final int THIRD_PHASE_LENGTH = 20;

  /** Entropy bonus of the first phase. */
  public static final double FIRST_PHASE_BONUS = 4.0;

  /** Entropy bonus of the second phase. */
  public static final double SECOND_PHASE_BONUS = 2.0;

  /** Entropy bonus of the third phase. */
  public static final double THIRD_PHASE_BONUS = 1.5;

  /** Array used for determining dictionary entropy "bonus" for calculating the Shannon entropy estimate. */
  public static final int[] SHANNON_DICTIONARY_SIEVE = {0, 0, 0, 4, 5, 6, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0};

  /** Array used for determining composition "bonus" for calculating the Shannon entropy estimate. **/
  public static final int[] SHANNON_COMPOSITION_SIEVE = {0, 0, 0, 2, 3, 3, 5, 6};

  /** Whether a dictionary was used to check the password. */
  private final boolean hasDictionaryCheck;

  /** Whether  at least 1 uppercase and special/symbol character is enforced. */
  private final boolean hasCompositionCheck;

  /** Size of the password. */
  private final int passwordSize;


  /**
   * Creates a new shannon entropy estimate.
   *
   * @param  dictionaryCheck   whether or not a common passwords dictionary is checked against the password (50,000
   *                           dictionary words are recommended)
   * @param  compositionCheck  whether or not at least 1 uppercase and special/symbol character is enforced (not
   *                           using common substitutions such as s to $ are recommended)
   * @param  pwordSize         size of the password
   */
  public ShannonEntropy(final boolean dictionaryCheck, final boolean compositionCheck, final int pwordSize)
  {
    hasDictionaryCheck = dictionaryCheck;
    hasCompositionCheck = compositionCheck;
    passwordSize = pwordSize;
  }


  /**
   * Returns the entropy bits of a user selected password. This estimate is based on a 94 Character Alphabet and is a
   * "ballpark" estimate based on Claude Shannon's observations.
   *
   * See <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   * A1. "User Selected Passwords"
   *
   * @return  estimated entropy bits given password properties
   */
  @Override
  public double estimate()
  {
    double shannonEntropy = 0.0;
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

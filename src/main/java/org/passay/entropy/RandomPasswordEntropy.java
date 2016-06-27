/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

/**
 * Entropy bits estimate defined in NIST SP-800-63-1 Randomly Selected Passwords.
 *
 * See <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
 * A1. "Randomly Selected Passwords"
 *
 * @author  Middleware Services
 */
public class RandomPasswordEntropy implements Entropy
{

  /** Size of the alphabet. */
  private final int alphabetSize;

  /** Size of the password. */
  private final int passwordSize;


  /**
   * Creates a new random entropy estimate.
   *
   * @param  alphaSize  size of the alphabet used
   * @param  pwordSize  size of the password
   */
  public RandomPasswordEntropy(final int alphaSize, final int pwordSize)
  {
    alphabetSize = alphaSize;
    passwordSize = pwordSize;
  }


  /**
   * Returns the entropy bits of a randomly generated password given the size of the unique characters used
   * (alphabetSize) and the size of the password.
   *
   * See <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   * A1. "Randomly Selected Passwords"
   *
   * @return  estimated entropy bits given password properties
   */
  @Override
  public double estimate()
  {
    return log2(Math.pow(alphabetSize, passwordSize));
  }


  /**
   * Returns the log base 2 of a given number.
   *
   * @param  number  to get the log2 of
   *
   * @return  log2  of number
   */
  public static double log2(final double number)
  {
    return Math.log(number) / Math.log(2);
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for {@link org.passay.CharacterSequence}.
 *
 * @author  Middleware Services
 */
public class CharacterSequenceTest
{

  /**
   * Tests no arg constructor.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNoSequencesFailure()
  {
    new CharacterSequence();
  }


  /**
   * Tests failure condition where constructor arguments are not same length.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUnequalSequenceFailure()
  {
    new CharacterSequence("12345", "!@#$");
  }


  /**
   * Tests the {@link org.passay.CharacterSequence#matches(int, char)} method.
   *
   * @throws Exception On errors.
   */
  @Test
  public void testMatches() throws Exception
  {
    final CharacterSequence sequence = new CharacterSequence(
      "12345", "ABCDE", "abcde");
    Assert.assertTrue(sequence.matches(0, '1'));
    Assert.assertTrue(sequence.matches(0, 'A'));
    Assert.assertTrue(sequence.matches(0, 'a'));
    Assert.assertFalse(sequence.matches(4, 'z'));
  }
}

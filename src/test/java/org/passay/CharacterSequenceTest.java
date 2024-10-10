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

  /** Tests no arg constructor. */
  @Test(groups = "passtest", expectedExceptions = IllegalArgumentException.class)
  public void testNoSequencesFailure()
  {
    new CharacterSequence();
  }


  /** Tests failure condition where constructor arguments are not same length. */
  @Test(groups = "passtest", expectedExceptions = IllegalArgumentException.class)
  public void testUnequalSequenceFailure()
  {
    new CharacterSequence("12345", "!@#$");
  }


  /**
   * Tests the {@link org.passay.CharacterSequence#matches(int, char)} method.
   */
  @Test(groups = "passtest")
  public void testMatches()
  {
    final CharacterSequence sequence = new CharacterSequence("12345", "ABCDE", "abcde");
    Assert.assertTrue(sequence.matches(0, '1'));
    Assert.assertTrue(sequence.matches(0, 'A'));
    Assert.assertTrue(sequence.matches(0, 'a'));
    Assert.assertFalse(sequence.matches(4, 'z'));
  }


  @Test(groups = "passtest")
  public void testValidSequences()
  {
    Assert.assertNotNull(PolishSequenceData.values());
    Assert.assertNotNull(CyrillicSequenceData.values());
    Assert.assertNotNull(CzechSequenceData.values());
    Assert.assertNotNull(GermanSequenceData.values());
    Assert.assertNotNull(CyrillicModernSequenceData.values());
    Assert.assertNotNull(EnglishSequenceData.values());
  }
}

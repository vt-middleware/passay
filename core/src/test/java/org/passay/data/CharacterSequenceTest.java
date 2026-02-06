/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;


/**
 * Unit test for {@link CharacterSequence}.
 *
 * @author  Middleware Services
 */
public class CharacterSequenceTest
{

  /** Tests no arg constructor. */
  @Test
  public void testNoSequencesFailure()
  {
    assertThatThrownBy(CharacterSequence::new).isInstanceOf(IllegalArgumentException.class);
  }


  /** Tests failure condition where constructor arguments are not same length. */
  @Test
  public void testUnequalSequenceFailure()
  {
    assertThatThrownBy(() -> new CharacterSequence("12345", "!@#$")).isInstanceOf(IllegalArgumentException.class);
  }


  /**
   * Tests the {@link CharacterSequence#matches(int, int)} method.
   */
  @Test
  public void testMatches()
  {
    final CharacterSequence sequence = new CharacterSequence("12345", "ABCDE", "abcde");
    assertThat(sequence.matches(0, '1')).isTrue();
    assertThat(sequence.matches(0, 'A')).isTrue();
    assertThat(sequence.matches(0, 'a')).isTrue();
    assertThat(sequence.matches(4, 'z')).isFalse();
  }


  @Test
  public void testValidSequences()
  {
    assertThat(PolishSequenceData.values()).isNotNull();
    assertThat(CyrillicSequenceData.values()).isNotNull();
    assertThat(CzechSequenceData.values()).isNotNull();
    assertThat(GermanSequenceData.values()).isNotNull();
    assertThat(CyrillicModernSequenceData.values()).isNotNull();
    assertThat(EnglishSequenceData.values()).isNotNull();
  }
}

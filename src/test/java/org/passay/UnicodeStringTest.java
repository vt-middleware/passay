/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link UnicodeString}.
 *
 * @author  Middleware Services
 */
public class UnicodeStringTest
{


  @Test
  public void constructor()
  {
    try {
      new UnicodeString((char[]) null);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class);
    }
    try {
      new UnicodeString((int[]) null);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class);
    }
    try {
      new UnicodeString((String) null);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class);
    }
    assertThat(new UnicodeString().codePoints()).isEqualTo(new int[0]);
    assertThat(new UnicodeString("").codePoints()).isEqualTo(new int[0]);
    assertThat(new UnicodeString(new int[0]).codePoints()).isEqualTo(new int[0]);
    assertThat(new UnicodeString('A', 'B', 'C').codePoints()).isEqualTo(new int[] {65, 66, 67});
    assertThat(new UnicodeString('A', '¢', 'C').codePoints()).isEqualTo(new int[] {65, 162, 67});
    assertThat(new UnicodeString('A', '\u16C8', 'C').codePoints()).isEqualTo(new int[] {65, 5832, 67});
    assertThat(new UnicodeString('A', '\uD808', '\uDF34', 'C').codePoints()).isEqualTo(new int[] {65, 74548, 67});
    assertThat(new UnicodeString('A', '\uD83C', '\uDDEE', '\uD83C', '\uDDF8', 'C').codePoints())
      .isEqualTo(new int[] {65, 127470, 127480, 67});
    assertThat(new UnicodeString("ABC").codePoints()).isEqualTo(new int[] {65, 66, 67});
    assertThat(new UnicodeString("A¢C").codePoints()).isEqualTo(new int[] {65, 162, 67});
    assertThat(new UnicodeString("A\u16C8C").codePoints()).isEqualTo(new int[] {65, 5832, 67});
    assertThat(new UnicodeString("A\uD808\uDF34C").codePoints()).isEqualTo(new int[] {65, 74548, 67});
    assertThat(new UnicodeString("A\uD83C\uDDEE\uD83C\uDDF8C").codePoints())
      .isEqualTo(new int[] {65, 127470, 127480, 67});
    assertThat(new UnicodeString(65, 66, 67).codePoints()).isEqualTo(new int[] {65, 66, 67});
  }


  @Test
  public void equals()
  {
    assertThat(new UnicodeString('A', 'B', 'C')).isEqualTo(new UnicodeString('A', 'B', 'C'));
    assertThat(new UnicodeString('A', '¢', 'C')).isEqualTo(new UnicodeString('A', '¢', 'C'));
    assertThat(new UnicodeString('A', '\u16C8', 'C')).isEqualTo(new UnicodeString('A', '\u16C8', 'C'));
    assertThat(new UnicodeString('A', '\uD808', '\uDF34', 'C'))
      .isEqualTo(new UnicodeString('A', '\uD808', '\uDF34', 'C'));
    assertThat(new UnicodeString('A', '\uD83C', '\uDDEE', '\uD83C', '\uDDF8', 'C'))
      .isEqualTo(new UnicodeString('A', '\uD83C', '\uDDEE', '\uD83C', '\uDDF8', 'C'));
    assertThat(new UnicodeString("ABC")).isEqualTo(new UnicodeString("ABC"));
    assertThat(new UnicodeString("A¢C")).isEqualTo(new UnicodeString("A¢C"));
    assertThat(new UnicodeString("A\u16C8C")).isEqualTo(new UnicodeString("A\u16C8C"));
    assertThat(new UnicodeString("A\uD808\uDF34C")).isEqualTo(new UnicodeString("A\uD808\uDF34C"));
    assertThat(new UnicodeString("A\uD83C\uDDEE\uD83C\uDDF8C"))
      .isEqualTo(new UnicodeString("A\uD83C\uDDEE\uD83C\uDDF8C"));
    assertThat(new UnicodeString(65, 66, 67)).isEqualTo(new UnicodeString(65, 66, 67));
    assertThat(new UnicodeString('A', 'B', 'C')).isNotEqualTo(new UnicodeString('A', 'D', 'C'));
  }


  @Test
  public void codePointAt()
  {
    try {
      new UnicodeString("ABC123").codePointAt(-1);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class);
    }
    assertThat(new UnicodeString("ABC123").codePointAt(0)).isEqualTo(65);
    assertThat(new UnicodeString("ABC123").codePointAt(1)).isEqualTo(66);
    assertThat(new UnicodeString("ABC123").codePointAt(2)).isEqualTo(67);
    assertThat(new UnicodeString("ABC123").codePointAt(3)).isEqualTo(49);
    assertThat(new UnicodeString("ABC123").codePointAt(4)).isEqualTo(50);
    assertThat(new UnicodeString("ABC123").codePointAt(5)).isEqualTo(51);
    try {
      new UnicodeString("ABC123").codePointAt(6);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class);
    }
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(2)).isEqualTo(67);
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(3)).isEqualTo(5832);
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(4)).isEqualTo(49);
  }


  @Test
  public void indexOf()
  {
    assertThat(new UnicodeString("ABC123").indexOf(new UnicodeString("XYZ"))).isEqualTo(-1);
    assertThat(new UnicodeString("ABC123").indexOf(new UnicodeString("0123"))).isEqualTo(-1);
    assertThat(new UnicodeString("ABC123").indexOf(new UnicodeString("123"))).isEqualTo(3);
    assertThat(new UnicodeString("ABC123").indexOf(new UnicodeString("C1"))).isEqualTo(2);
    assertThat(new UnicodeString("ABC\u16C8123").indexOf(new UnicodeString("\u16C8"))).isEqualTo(3);
    assertThat(new UnicodeString("ABC\u16C8123").indexOf(new UnicodeString("C\u16C81"))).isEqualTo(2);
    assertThat(new UnicodeString("ABC\u16C8123").indexOf(new UnicodeString("\u16C9"))).isEqualTo(-1);
  }


  @Test
  public void startsWith()
  {
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("A"))).isTrue();
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("AB"))).isTrue();
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("ABC"))).isTrue();
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("BC"))).isFalse();
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("123"))).isFalse();
    assertThat(new UnicodeString("ABC123").startsWith(new UnicodeString("XYZ"))).isFalse();
    assertThat(new UnicodeString("¢BC123").startsWith(new UnicodeString('¢'))).isTrue();
    assertThat(new UnicodeString("¢BC123").startsWith(new UnicodeString('¢', 'B'))).isTrue();
    assertThat(new UnicodeString("¢BC123").startsWith(new UnicodeString(162))).isTrue();
  }


  @Test
  public void endsWith()
  {
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("3"))).isTrue();
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("23"))).isTrue();
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("123"))).isTrue();
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("12"))).isFalse();
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("ABC"))).isFalse();
    assertThat(new UnicodeString("ABC123").endsWith(new UnicodeString("XYZ"))).isFalse();
    assertThat(new UnicodeString("ABC12¢").endsWith(new UnicodeString('¢'))).isTrue();
    assertThat(new UnicodeString("ABC12¢").endsWith(new UnicodeString('2', '¢'))).isTrue();
    assertThat(new UnicodeString("ABC12¢").endsWith(new UnicodeString(162))).isTrue();
  }
}

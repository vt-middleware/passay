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
    assertThat(new UnicodeString().codePoints().toArray()).isEqualTo(new int[0]);
    assertThat(new UnicodeString("").codePoints().toArray()).isEqualTo(new int[0]);
    assertThat(new UnicodeString(new int[0]).codePoints().toArray()).isEqualTo(new int[0]);
    assertThat(new UnicodeString('A', 'B', 'C').codePoints().toArray()).isEqualTo(new int[] {65, 66, 67});
    assertThat(new UnicodeString('A', '¢', 'C').codePoints().toArray()).isEqualTo(new int[] {65, 162, 67});
    assertThat(new UnicodeString('A', '\u16C8', 'C').codePoints().toArray()).isEqualTo(new int[] {65, 5832, 67});
    assertThat(new UnicodeString('A', '\uD808', '\uDF34', 'C').codePoints().toArray())
      .isEqualTo(new int[] {65, 74548, 67});
    assertThat(new UnicodeString('A', '\uD83C', '\uDDEE', '\uD83C', '\uDDF8', 'C').codePoints().toArray())
      .isEqualTo(new int[] {65, 127470, 127480, 67});
    assertThat(new UnicodeString("ABC").codePoints().toArray()).isEqualTo(new int[] {65, 66, 67});
    assertThat(new UnicodeString("A¢C").codePoints().toArray()).isEqualTo(new int[] {65, 162, 67});
    assertThat(new UnicodeString("A\u16C8C").codePoints().toArray()).isEqualTo(new int[] {65, 5832, 67});
    assertThat(new UnicodeString("A\uD808\uDF34C").codePoints().toArray()).isEqualTo(new int[] {65, 74548, 67});
    assertThat(new UnicodeString("A\uD83C\uDDEE\uD83C\uDDF8C").codePoints().toArray())
      .isEqualTo(new int[] {65, 127470, 127480, 67});
    assertThat(new UnicodeString(65, 66, 67).codePoints().toArray()).isEqualTo(new int[] {65, 66, 67});
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
  public void length()
  {
    assertThat(new UnicodeString("ABC123").length()).isEqualTo(6);
    assertThat(new UnicodeString("ABC\u16C8123").length()).isEqualTo(7);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").length()).isEqualTo(8);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").length()).isEqualTo(10);
  }


  @Test
  public void codePointCount()
  {
    assertThat(new UnicodeString("ABC123").codePointCount()).isEqualTo(6);
    assertThat(new UnicodeString("ABC\u16C8123").codePointCount()).isEqualTo(7);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").codePointCount()).isEqualTo(7);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").codePointCount()).isEqualTo(8);
  }


  @Test
  public void charAt()
  {
    try {
      new UnicodeString("ABC123").charAt(-1);
      fail("Should have thrown an ArrayIndexOutOfBoundsException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
    assertThat(new UnicodeString("ABC123").charAt(0)).isEqualTo('A');
    assertThat(new UnicodeString("ABC123").charAt(1)).isEqualTo('B');
    assertThat(new UnicodeString("ABC123").charAt(2)).isEqualTo('C');
    assertThat(new UnicodeString("ABC123").charAt(3)).isEqualTo('1');
    assertThat(new UnicodeString("ABC123").charAt(4)).isEqualTo('2');
    assertThat(new UnicodeString("ABC123").charAt(5)).isEqualTo('3');
    try {
      new UnicodeString("ABC123").charAt(6);
      fail("Should have thrown an ArrayIndexOutOfBoundsException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
    assertThat(new UnicodeString("ABC\u16C8123").charAt(2)).isEqualTo('C');
    assertThat(new UnicodeString("ABC\u16C8123").charAt(3)).isEqualTo('ᛈ');
    assertThat(new UnicodeString("ABC\u16C8123").charAt(4)).isEqualTo('1');
    assertThat(new UnicodeString("ABC\uD808\uDF34123").charAt(2)).isEqualTo('C');
    assertThat(new UnicodeString("ABC\uD808\uDF34123").charAt(3)).isEqualTo('\uD808');
    assertThat(new UnicodeString("ABC\uD808\uDF34123").charAt(4)).isEqualTo('\uDF34');
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").charAt(2)).isEqualTo('C');
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").charAt(3)).isEqualTo('\uD83C');
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").charAt(4)).isEqualTo('\uDDEE');
  }


  @Test
  public void codePointAt()
  {
    try {
      new UnicodeString("ABC123").codePointAt(-1);
      fail("Should have thrown an ArrayIndexOutOfBoundsException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
    assertThat(new UnicodeString("ABC123").codePointAt(0)).isEqualTo(65);
    assertThat(new UnicodeString("ABC123").codePointAt(1)).isEqualTo(66);
    assertThat(new UnicodeString("ABC123").codePointAt(2)).isEqualTo(67);
    assertThat(new UnicodeString("ABC123").codePointAt(3)).isEqualTo(49);
    assertThat(new UnicodeString("ABC123").codePointAt(4)).isEqualTo(50);
    assertThat(new UnicodeString("ABC123").codePointAt(5)).isEqualTo(51);
    try {
      new UnicodeString("ABC123").codePointAt(6);
      fail("Should have thrown an ArrayIndexOutOfBoundsException");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(2)).isEqualTo(67);
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(3)).isEqualTo(5832);
    assertThat(new UnicodeString("ABC\u16C8123").codePointAt(4)).isEqualTo(49);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").codePointAt(2)).isEqualTo(67);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").codePointAt(3)).isEqualTo(74548);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").codePointAt(4)).isEqualTo(49);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").codePointAt(3)).isEqualTo(127470);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123").codePointAt(4)).isEqualTo(127480);
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
    assertThat(new UnicodeString("ABC\uD808\uDF34123").indexOf(new UnicodeString("\uD808\uDF34"))).isEqualTo(3);
    assertThat(new UnicodeString("ABC\uD808\uDF34123").indexOf(new UnicodeString(74548))).isEqualTo(3);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123")
      .indexOf(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8"))).isEqualTo(3);
    assertThat(new UnicodeString("ABC\uD83C\uDDEE\uD83C\uDDF8123")
      .indexOf(new UnicodeString(127470, 127480))).isEqualTo(3);
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
    assertThat(new UnicodeString("\uD808\uDF34BC123").startsWith(new UnicodeString("\uD808\uDF34"))).isTrue();
    assertThat(new UnicodeString("\uD808\uDF34BC123").startsWith(new UnicodeString(74548))).isTrue();
    assertThat(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8BC123")
      .startsWith(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8"))).isTrue();
    assertThat(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8BC123")
      .startsWith(new UnicodeString(127470, 127480))).isTrue();
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
    assertThat(new UnicodeString("ABC12\uD808\uDF34").endsWith(new UnicodeString("\uD808\uDF34"))).isTrue();
    assertThat(new UnicodeString("ABC12\uD808\uDF34").endsWith(new UnicodeString(74548))).isTrue();
    assertThat(new UnicodeString("ABC12\uD83C\uDDEE\uD83C\uDDF8")
      .endsWith(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8"))).isTrue();
    assertThat(new UnicodeString("ABC12\uD83C\uDDEE\uD83C\uDDF8")
      .endsWith(new UnicodeString(127470, 127480))).isTrue();
  }


  @Test
  public void intersection()
  {
    assertThat(new UnicodeString().intersection(new UnicodeString()))
      .isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("ABC123").intersection(new UnicodeString()))
      .isEqualTo(new UnicodeString());
    assertThat(new UnicodeString().intersection(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("ABC123").intersection(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("abcdefg").intersection(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("efg"));
    assertThat(new UnicodeString("ABC123").intersection(new UnicodeString("ZBX920")))
      .isEqualTo(new UnicodeString("B2"));
    assertThat(new UnicodeString("AB¢123").intersection(new UnicodeString("ZBX920")))
      .isEqualTo(new UnicodeString("B2"));
    assertThat(new UnicodeString("AB\uD808\uDF34123").intersection(new UnicodeString("ZBX920")))
      .isEqualTo(new UnicodeString("B2"));
    assertThat(new UnicodeString("AB\uD83C\uDDEE\uD83C\uDDF8123").intersection(new UnicodeString("ZBX920")))
      .isEqualTo(new UnicodeString("B2"));
    assertThat(new UnicodeString("AB¢123").intersection(new UnicodeString("ZBX9¢0")))
      .isEqualTo(new UnicodeString("B¢"));
    assertThat(new UnicodeString("AB\uD808\uDF34123").intersection(new UnicodeString("ZBX9\uD808\uDF340")))
      .isEqualTo(new UnicodeString("B\uD808\uDF34"));
    assertThat(new UnicodeString("AB\uD83C\uDDEE\uD83C\uDDF8123")
      .intersection(new UnicodeString("ZBX9\uD83C\uDDEE\uD83C\uDDF80")))
      .isEqualTo(new UnicodeString("B\uD83C\uDDEE\uD83C\uDDF8"));
  }


  @Test
  public void union()
  {
    assertThat(new UnicodeString().union(new UnicodeString())).isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("ABC123").union(new UnicodeString()))
      .isEqualTo(new UnicodeString("ABC123"));
    assertThat(new UnicodeString().union(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString("XYZ098"));
    assertThat(new UnicodeString("ABC123").union(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString("ABC123XYZ098"));
    assertThat(new UnicodeString("abcdefg").union(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("abcdefghij"));
    assertThat(new UnicodeString("ab¢defg").union(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab¢defghij"));
    assertThat(new UnicodeString("ab\uD808\uDF34defg").union(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab\uD808\uDF34defghij"));
    assertThat(new UnicodeString("ab\uD83C\uDDEE\uD83C\uDDF8defg").union(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab\uD83C\uDDEE\uD83C\uDDF8defghij"));
    assertThat(new UnicodeString("abcdefg").union(new UnicodeString("¢fghij")))
      .isEqualTo(new UnicodeString("abcdefg¢hij"));
    assertThat(new UnicodeString("abcdefg").union(new UnicodeString("\uD808\uDF34fghij")))
      .isEqualTo(new UnicodeString("abcdefg\uD808\uDF34hij"));
    assertThat(new UnicodeString("abcdefg").union(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8fghij")))
      .isEqualTo(new UnicodeString("abcdefg\uD83C\uDDEE\uD83C\uDDF8hij"));
  }


  @Test
  public void difference()
  {
    assertThat(new UnicodeString().difference(new UnicodeString())).isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("ABC123").difference(new UnicodeString()))
      .isEqualTo(new UnicodeString("ABC123"));
    assertThat(new UnicodeString().difference(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString());
    assertThat(new UnicodeString("ABC123").difference(new UnicodeString("XYZ098")))
      .isEqualTo(new UnicodeString("ABC123"));
    assertThat(new UnicodeString("abcdefg").difference(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("abcd"));
    assertThat(new UnicodeString("ab¢defg").difference(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab¢d"));
    assertThat(new UnicodeString("ab\uD808\uDF34defg").difference(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab\uD808\uDF34d"));
    assertThat(new UnicodeString("ab\uD83C\uDDEE\uD83C\uDDF8defg").difference(new UnicodeString("efghij")))
      .isEqualTo(new UnicodeString("ab\uD83C\uDDEE\uD83C\uDDF8d"));
    assertThat(new UnicodeString("abcdefg").difference(new UnicodeString("¢fghij")))
      .isEqualTo(new UnicodeString("abcde"));
    assertThat(new UnicodeString("abcdefg").difference(new UnicodeString("\uD808\uDF34fghij")))
      .isEqualTo(new UnicodeString("abcde"));
    assertThat(new UnicodeString("abcdefg").difference(new UnicodeString("\uD83C\uDDEE\uD83C\uDDF8fghij")))
      .isEqualTo(new UnicodeString("abcde"));
  }
}

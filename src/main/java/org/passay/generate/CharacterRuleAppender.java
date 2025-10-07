/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.generate;

import java.nio.CharBuffer;
import java.util.Random;
import org.passay.PassayUtils;
import org.passay.UnicodeString;
import org.passay.rule.CharacterRule;

/**
 * Character appender for {@link CharacterRule}.
 *
 * @author  Middleware Services
 */
public class CharacterRuleAppender implements CharacterAppender
{
  /** Number of characters. */
  private final int numberOfCharacters;

  /** Valid characters. */
  private final UnicodeString characters;

  /** To select random characters. */
  private final Random random;


  /**
   * Creates a new character rule appender.
   *
   * @param  rule  to build appender from
   * @param  allowedChars  characters allowed for appending
   * @param  illegalChars  characters not allowed for appending
   * @param  rand  to randomize character selection
   */
  public CharacterRuleAppender(
    final CharacterRule rule, final UnicodeString allowedChars, final UnicodeString illegalChars, final Random rand)
  {
    numberOfCharacters = rule.getNumberOfCharacters();
    UnicodeString validChars = new UnicodeString(rule.getValidCharacters());
    if (!allowedChars.isEmpty()) {
      validChars = validChars.intersection(allowedChars);
    }
    if (!illegalChars.isEmpty()) {
      validChars = validChars.difference(illegalChars);
    }
    characters = validChars;
    random = rand;
  }


  @Override
  public UnicodeString getCharacters()
  {
    return characters;
  }


  @Override
  public void append(final CharBuffer target, final int count)
  {
    PassayUtils.appendRandomCharacters(characters, target, Math.min(count, numberOfCharacters), random);
  }
}

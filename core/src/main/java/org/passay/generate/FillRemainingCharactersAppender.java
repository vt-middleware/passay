/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.generate;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.passay.PassayUtils;
import org.passay.UnicodeString;
import org.passay.rule.CharacterCharacteristicsRule;
import org.passay.rule.CharacterRule;
import org.passay.rule.Rule;

/**
 * Character appender for filling a password with valid characters. This appender should be the last to execute for
 * password generation.
 *
 * @author  Middleware Services
 */
public class FillRemainingCharactersAppender implements CharacterAppender
{
  /** Valid characters. */
  private final UnicodeString characters;

  /** To select random characters. */
  private final Random random;


  /**
   * Creates a new fill appender.
   *
   * @param  rules  to derive characters from
   * @param  allowedChars  characters allowed for appending
   * @param  illegalChars  characters not allowed for appending
   * @param  rand  to randomize character selection
   */
  public FillRemainingCharactersAppender(
    final List<? extends Rule> rules,
    final UnicodeString allowedChars,
    final UnicodeString illegalChars,
    final Random rand)
  {
    if (!allowedChars.isEmpty()) {
      if (!illegalChars.isEmpty()) {
        characters = allowedChars.difference(illegalChars);
      } else {
        characters = allowedChars;
      }
    } else {
      // characters defined in all CharacterRules
      final UnicodeString chars = getCharacters(rules);
      if (!chars.isEmpty()) {
        if (!illegalChars.isEmpty()) {
          characters = chars.difference(illegalChars);
        } else {
          characters = chars;
        }
      } else {
        characters = new UnicodeString();
      }
    }
    random = rand;
  }


  /**
   * Return a unicode string that contains the unique characters from all {@link CharacterRule} or
   * {@link CharacterCharacteristicsRule} contained in the supplied list.
   *
   * @param  rules  to extract unique characters from
   *
   * @return  unicode string containing unique characters or empty string
   */
  private UnicodeString getCharacters(final List<? extends Rule> rules)
  {
    final List<CharacterRule> characterRules = new ArrayList<>();
    for (Rule rule : rules) {
      if (rule instanceof CharacterRule) {
        characterRules.add((CharacterRule) rule);
      } else if (rule instanceof CharacterCharacteristicsRule) {
        characterRules.addAll(((CharacterCharacteristicsRule) rule).getRules());
      }
    }
    UnicodeString chars = null;
    if (!characterRules.isEmpty()) {
      chars = new UnicodeString(characterRules.get(0).getValidCharacters());
      for (int i = 1; i < characterRules.size(); i++) {
        chars = chars.union(new UnicodeString(characterRules.get(i).getValidCharacters()));
      }
    }
    return chars != null ? chars : new UnicodeString();
  }


  @Override
  public UnicodeString getCharacters()
  {
    return characters;
  }


  @Override
  public void append(final CharBuffer target, final int count)
  {
    PassayUtils.appendRandomCharacters(characters, target, count, random);
  }
}

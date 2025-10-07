/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.generate;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.passay.UnicodeString;
import org.passay.rule.CharacterCharacteristicsRule;

/**
 * Character appender for {@link CharacterCharacteristicsRule}.
 *
 * @author  Middleware Services
 */
public class CharacterCharacteristicsAppender implements CharacterAppender
{
  /** Number of characteristics. */
  private final int numberOfCharacteristics;

  /** Character rule appenders. */
  private final List<CharacterRuleAppender> appenders;

  /** To select random characters. */
  private final Random random;


  /**
   * Creates a new character characteristics appender.
   *
   * @param  rule  to build appender from
   * @param  allowedChars  characters allowed in password generation
   * @param  illegalChars  characters not allowed in password generation
   * @param  rand  to randomize character selection
   */
  public CharacterCharacteristicsAppender(
    final CharacterCharacteristicsRule rule,
    final UnicodeString allowedChars,
    final UnicodeString illegalChars,
    final Random rand)
  {
    numberOfCharacteristics = rule.getNumberOfCharacteristics();
    appenders = rule.getRules().stream()
      .map(r -> new CharacterRuleAppender(r, allowedChars, illegalChars, rand))
      .collect(Collectors.toList());
    random = rand;
  }


  @Override
  public UnicodeString getCharacters()
  {
    UnicodeString characters = appenders.get(0).getCharacters();
    for (int i = 1; i < appenders.size(); i++) {
      characters = characters.union(appenders.get(i).getCharacters());
    }
    return characters;
  }


  @Override
  public void append(final CharBuffer target, final int count)
  {
    final List<CharacterRuleAppender> randomAppender = new ArrayList<>(appenders);
    Collections.shuffle(randomAppender, random);
    for (int i = 0; i < numberOfCharacteristics; i++) {
      randomAppender.get(i).append(target, count);
    }
  }
}

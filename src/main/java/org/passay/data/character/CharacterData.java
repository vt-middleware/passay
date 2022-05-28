/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.data.character;

import org.passay.rule.CharacterRule;

/**
 * Input data used by {@link CharacterRule}.
 *
 * @author  Middleware Services
 */
public interface CharacterData
{


  /**
   * Return the error code used for message resolution.
   *
   * @return  error code
   */
  String getErrorCode();


  /**
   * Returns the characters.
   *
   * @return  characters
   */
  String getCharacters();
}

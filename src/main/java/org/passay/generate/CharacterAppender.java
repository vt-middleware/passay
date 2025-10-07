/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.generate;

import java.nio.CharBuffer;
import org.passay.UnicodeString;

/**
 * Interface for appending characters to a character buffer.
 *
 * @author  Middleware Services
 */
public interface CharacterAppender
{


  /**
   * Returns the characters that may be used in by this appender.
   *
   * @return  valid characters
   */
  UnicodeString getCharacters();


  /**
   * Fills the target buffer with at most count characters from this appender.
   *
   * @param  target  buffer to add characters to
   * @param  count  maximum number of characters to add to buffer
   */
  void append(CharBuffer target, int count);
}

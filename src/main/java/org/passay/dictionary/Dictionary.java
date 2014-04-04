/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

/**
 * Base interface for all dictionaries.
 *
 * @author  Middleware Services
 */
public interface Dictionary
{


  /**
   * Returns whether the supplied word exists in the dictionary.
   *
   * @param  word  to search for
   *
   * @return  whether word was found
   */
  boolean search(String word);
}

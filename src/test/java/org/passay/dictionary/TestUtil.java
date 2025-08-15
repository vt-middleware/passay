/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Common methods for dictionary tests.
 *
 * @author  Middleware Services
 */
public final class TestUtil
{


  /** Private constructor of utility class. */
  private TestUtil() {}


  /**
   * Returns an array of every line in the supplied file.
   *
   * @param  file  To read
   *
   * @return  Array of lines
   *
   * @throws  IOException  if an error occurs reading the file
   */
  public static String[] fileToArray(final String file) throws IOException
  {
    final List<String> words = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String word;
      while ((word = br.readLine()) != null) {
        words.add(word);
      }
    }
    return words.toArray(new String[0]);
  }
}

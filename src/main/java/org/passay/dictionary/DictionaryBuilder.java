/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.passay.dictionary.sort.ArraySorter;
import org.passay.dictionary.sort.ArraysSort;

/**
 * Builder for common dictionary usage. Creates a {@link WordListDictionary} backed by an {@link ArrayWordList} from one
 * or more files containing a list of words, one per line.
 *
 * @author  Middleware Services
 */
public class DictionaryBuilder
{

  /** Singleton array sorter, must be thread safe. */
  private static final ArraySorter SORTER = new ArraysSort();

  /** List of word list files. */
  private final List<Reader> sources = new ArrayList<>();

  /** Dictionary case sensitivity flag. */
  private boolean caseSensitive;


  /**
   * Adds a word list to the dictionary to be built.
   * The file is read using the platform default charset.
   *
   * @param  path  Path to word list, one word per line.
   *
   * @return  This builder.
   */
  public DictionaryBuilder addFile(final String path)
  {
    try {
      return addReader(new FileReader(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(path + " does not exist", e);
    }
  }

  /**
   * Adds a word list to the dictionary to be built.
   *
   * @param  reader  Reader which returns a word list, one word per line.
   *
   * @return  This builder.
   */
  public DictionaryBuilder addReader(final Reader reader)
  {
    sources.add(reader);
    return this;
  }

  /**
   * Sets the case sensitivity flag on the dictionary to be built. Dictionaries are built case <em>in</em>sensitive by
   * default.
   *
   * @param  flag  True for case sensitive, false otherwise.
   *
   * @return  This builder.
   */
  public DictionaryBuilder setCaseSensitive(final boolean flag)
  {
    caseSensitive = flag;
    return this;
  }


  /**
   * Builds a dictionary from the configured properties.
   *
   * @return  New {@link WordListDictionary} instance.
   */
  public Dictionary build()
  {
    try {
      return new WordListDictionary(
        WordLists.createFromReader(sources.toArray(new Reader[0]), caseSensitive, SORTER));
    } catch (IOException e) {
      throw new RuntimeException("IO error building dictionary", e);
    }
  }
}

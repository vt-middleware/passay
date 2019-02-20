/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.passay.dictionary.sort.ArraysSort;

/**
 * Provides fast searching for dictionary words using a word list. It's critical that the word list provided to this
 * dictionary be sorted according to the natural ordering of {@link java.lang.String}.
 *
 * @author  Middleware Services
 */
public class WordListDictionary implements Dictionary
{

  /** list used for searching. */
  protected final WordList wordList;


  /**
   * Creates a new dictionary instance from the supplied {@link WordList}.
   *
   * @param  wl  list of words sorted according to {@link WordList#getComparator()}.
   *
   *             <p><strong>NOTE</strong> Failure to provide a sorted word list will produce incorrect results.</p>
   */
  public WordListDictionary(final WordList wl)
  {
    wordList = wl;
  }


  /**
   * Returns the word list used for searching.
   *
   * @return  word list
   */
  public WordList getWordList()
  {
    return wordList;
  }


  @Override
  public long size()
  {
    return wordList == null ? 0 : wordList.size();
  }


  @Override
  public boolean search(final String word)
  {
    return WordLists.binarySearch(wordList, word) >= 0;
  }


  @Override
  public String toString()
  {
    return String.format("%s@%h::wordList=%s", getClass().getName(), hashCode(), wordList);
  }


  /**
   * Provides command line access to this word list dictionary.
   *
   * @param  args  command line arguments
   *
   * @throws  Exception  if an error occurs
   */
  public static void main(final String[] args)
    throws Exception
  {
    final List<FileReader> files = new ArrayList<>();
    try {
      if (args.length == 0) {
        throw new ArrayIndexOutOfBoundsException();
      }

      // dictionary operations
      boolean caseSensitive = true;
      boolean search = false;
      boolean print = false;

      // operation parameters
      String word = null;

      for (int i = 0; i < args.length; i++) {
        if ("-ci".equals(args[i])) {
          caseSensitive = false;
        } else if ("-s".equals(args[i])) {
          search = true;
          word = args[++i];
        } else if ("-p".equals(args[i])) {
          print = true;
        } else if ("-h".equals(args[i])) {
          throw new ArrayIndexOutOfBoundsException();
        } else {
          files.add(new FileReader(args[i]));
        }
      }

      // insert data
      final ArrayWordList awl = WordLists.createFromReader(
        files.toArray(new FileReader[files.size()]),
        caseSensitive,
        new ArraysSort());
      final WordListDictionary dict = new WordListDictionary(awl);

      // perform operation
      if (search) {
        if (dict.search(word)) {
          System.out.println(String.format("%s was found in this dictionary", word));
        } else {
          System.out.println(String.format("%s was not found in this dictionary", word));
        }
      } else if (print) {
        System.out.println(dict.getWordList());
      } else {
        throw new ArrayIndexOutOfBoundsException();
      }

    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Usage: java " + WordListDictionary.class.getName());
      System.out.println("            <dict1> [... <dictN>] [options] <operation>");
      System.out.println();
      System.out.println("Where <dict1>...<dictN> are files containing dictionary words.");
      System.out.println();
      System.out.println("Options:");
      System.out.println("    -ci make search case-insensitive");
      System.out.println();
      System.out.println("Operations:");
      System.out.println("    -s <word>");
      System.out.println("       search for a word");
      System.out.println("    -p print the entire dictionary");
      System.out.println("    -h print this help message");
      System.exit(1);
    }
  }
}

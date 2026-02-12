/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.passay.PassayUtils;
import org.passay.dictionary.sort.ArraysSort;

/**
 * Provides fast searching for dictionary words using a ternary tree. The entire dictionary is stored in memory, so heap
 * size may need to be adjusted to accommodate large dictionaries. It is highly recommended that sorted word lists be
 * inserted using their median. This helps to produce a balanced ternary tree which improves search time. This class
 * inherits the lower case property of the supplied word list.
 *
 * @author  Middleware Services
 */

public class TernaryTreeDictionary implements Dictionary
{

  /** Ternary tree used for searching. */
  protected final TernaryTree tree;


  /**
   * Creates a new balanced tree dictionary from the supplied {@link WordList}. This constructor creates a balanced tree
   * by inserting from the median of the word list, which may require additional work depending on the {@link WordList}
   * implementation.
   *
   * <p><strong>NOTE</strong> While using an unsorted word list produces correct results, it may dramatically reduce
   * search efficiency. Using a sorted word list is recommended.</p>
   *
   * @param  wordList  list of words used to back the dictionary. This list is used exclusively to initialize the
   *                   internal {@link TernaryTree} used by the dictionary, and may be safely discarded after dictionary
   *                   creation.
   */
  public TernaryTreeDictionary(final WordList wordList)
  {
    this(wordList, true);
  }


  /**
   * Creates a new dictionary instance from the given {@link WordList}.
   *
   * @param  wordList  list of words used to back the dictionary. This list is used exclusively to initialize the
   *                   internal {@link TernaryTree} used by the dictionary, and may be safely discarded after dictionary
   *                   creation.
   *
   *                   <p><strong>NOTE</strong> While using an unsorted word list produces correct results, it may
   *                   dramatically reduce search efficiency. Using a sorted word list is recommended.</p>
   * @param  useMedian  set to true to force creation of a balanced tree by inserting into the tree from the median of
   *                    the {@link WordList} outward. Depending on the word list implementation, this may require
   *                    additional work to access the median element on each insert.
   */
  public TernaryTreeDictionary(final WordList wordList, final boolean useMedian)
  {
    // Respect case sensitivity of word list in ternary tree
    final boolean caseSensitive = wordList.getComparator().compare("A", "a") != 0;
    tree = new TernaryTree(caseSensitive);

    final Iterator<String> iterator = useMedian ? wordList.medianIterator() : wordList.iterator();
    while (iterator.hasNext()) {
      tree.insert(iterator.next());
    }
  }


  /**
   * Creates a dictionary that uses the supplied ternary tree for dictionary searches.
   *
   * @param  tree  ternary tree used to back dictionary.
   */
  public TernaryTreeDictionary(final TernaryTree tree)
  {
    this.tree = PassayUtils.assertNotNullArg(tree, "Ternary tree cannot be null");
  }


  @Override
  public long size()
  {
    return tree == null ? 0 : tree.getWords().size();
  }


  @Override
  public boolean search(final CharSequence word)
  {
    return tree.search(word);
  }


  /**
   * Returns an array of strings which partially match the supplied word. This search is case-sensitive by default. See
   * {@link TernaryTree#partialSearch}.
   *
   * @param  word  to search for
   *
   * @return  array of matching words
   */
  public CharSequence[] partialSearch(final CharSequence word)
  {
    return tree.partialSearch(word);
  }


  /**
   * Returns an array of strings which are near to the supplied word by the supplied distance. This search is
   * case-sensitive by default. See {@link TernaryTree#nearSearch}.
   *
   * @param  word  to search for
   * @param  distance  for valid match
   *
   * @return  array of matching words
   */
  public CharSequence[] nearSearch(final CharSequence word, final int distance)
  {
    return tree.nearSearch(word, distance);
  }


  /**
   * Returns the underlying ternary tree used by this dictionary.
   *
   * @return  ternary tree
   */
  public TernaryTree getTernaryTree()
  {
    return tree;
  }


  /**
   * Provides command line access to a ternary tree dictionary.
   *
   * @param  args  command line arguments
   *
   * @throws  Exception  if an error occurs
   */
  public static void main(final String[] args) throws Exception
  {
    final List<FileReader> files = new ArrayList<>();
    try {
      if (args.length == 0) {
        throw new ArrayIndexOutOfBoundsException();
      }

      // dictionary operations
      boolean useMedian = false;
      boolean caseSensitive = true;
      boolean search = false;
      boolean partialSearch = false;
      boolean nearSearch = false;
      boolean print = false;
      boolean printPath = false;
      boolean stats = false;

      // operation parameters
      String word = null;
      int distance = 0;

      for (int i = 0; i < args.length; i++) {
        if ("-m".equals(args[i])) {
          useMedian = true;
        } else if ("-ci".equals(args[i])) {
          caseSensitive = false;
        } else if ("-s".equals(args[i])) {
          search = true;
          word = args[++i];
        } else if ("-ps".equals(args[i])) {
          partialSearch = true;
          word = args[++i];
        } else if ("-ns".equals(args[i])) {
          nearSearch = true;
          word = args[++i];
          distance = Integer.parseInt(args[++i]);
        } else if ("-p".equals(args[i])) {
          print = true;
        } else if ("-pp".equals(args[i])) {
          printPath = true;
        } else if ("-st".equals(args[i])) {
          stats = true;
        } else if ("-h".equals(args[i])) {
          throw new ArrayIndexOutOfBoundsException();
        } else {
          files.add(new FileReader(args[i]));
        }
      }

      // insert data
      final ArrayWordList awl = WordLists.createFromReader(
        files.toArray(new FileReader[0]),
        caseSensitive,
        new ArraysSort());
      final TernaryTreeDictionary dict = new TernaryTreeDictionary(awl, useMedian);

      // perform operation
      if (search) {
        if (dict.search(word)) {
          System.out.printf("%s was found in this dictionary%n", word);
        } else {
          System.out.printf("%s was not found in this dictionary%n", word);
        }
      } else if (partialSearch) {
        final CharSequence[] matches = dict.partialSearch(word);
        System.out.printf(
          "Found %s matches for %s in this dictionary : %s%n",
          matches.length,
          word,
          Arrays.asList(matches));
      } else if (nearSearch) {
        final CharSequence[] matches = dict.nearSearch(word, distance);
        System.out.printf(
          "Found %s matches for %s in this dictionary at a distance of %s " +
            ": %s%n",
          matches.length,
          word,
          distance,
          Arrays.asList(matches));
      } else if (print || printPath) {
        dict.getTernaryTree().print(new PrintWriter(System.out, true), printPath);
      } else if (stats) {
        System.out.println("word path depths histogram:");
        System.out.println(dict.getTernaryTree().getNodeStats());
      } else {
        throw new ArrayIndexOutOfBoundsException();
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Usage: java " + TernaryTreeDictionary.class.getName());
      System.out.println("            <dict1> [... <dictN>] [options] <operation>");
      System.out.println();
      System.out.println("Where <dict1>...<dictN> are files containing dictionary words.");
      System.out.println();
      System.out.println("Options:");
      System.out.println("    -m  insert dictionary using its median");
      System.out.println("    -ci make search case-insensitive");
      System.out.println();
      System.out.println("Operations:");
      System.out.println("    -s  <word>");
      System.out.println("        search for a word");
      System.out.println("    -ps <word>");
      System.out.println("        partial search for a word (with wildcards, e.g. '.a.a.a')");
      System.out.println("    -ns <word> <distance>");
      System.out.println("        near search for a word");
      System.out.println("    -p  print the entire dictionary in tree form, path suffixes only");
      System.out.println("    -pp print the entire dictionary in tree form, full paths");
      System.out.println("    -st print the tree node depth statistics");
      System.out.println("    -h  print this help message");
      System.exit(1);
    }
  }
}

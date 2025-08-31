/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.passay.UnicodeString;

/**
 * Implementation of a ternary tree. Methods are provided for inserting strings and searching for strings. The
 * algorithms in this class are all recursive, and have not been optimized for any particular purpose. Data which is
 * inserted is not sorted before insertion, however data can be inserted beginning with the median of the supplied data.
 *
 * @author  Middleware Services
 */

public class TernaryTree
{

  /** Case sensitive comparator. */
  protected static final Comparator<String> CASE_SENSITIVE_COMPARATOR = String::compareTo;

  /** Case insensitive comparator. */
  protected static final Comparator<String> CASE_INSENSITIVE_COMPARATOR = String::compareToIgnoreCase;

  /** File system line separator. */
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /**
   * An empty results array.
   */
  private static final String[] EMPTY_ARRAY = new String[0];

  /** Character comparator. */
  protected final Comparator<String> comparator;

  /** root node of the ternary tree. */
  private TernaryNode root;


  /** Creates an empty case sensitive ternary tree. */
  public TernaryTree()
  {
    this(true);
  }


  /**
   * Creates an empty ternary tree with the given case sensitivity.
   *
   * @param  caseSensitive  whether this ternary tree should be case sensitive.
   */
  public TernaryTree(final boolean caseSensitive)
  {
    comparator = caseSensitive ? CASE_SENSITIVE_COMPARATOR : CASE_INSENSITIVE_COMPARATOR;
  }


  /**
   * Inserts the supplied word into this tree.
   *
   * @param  word  to insert
   */
  public void insert(final CharSequence word)
  {
    if (word != null) {
      root = insertNode(root, word, 0);
    }
  }


  /**
   * Inserts the supplied array of words into this tree.
   *
   * @param  words  to insert
   */
  public void insert(final CharSequence[] words)
  {
    if (words != null) {
      for (CharSequence s : words) {
        insert(s);
      }
    }
  }


  /**
   * Returns whether the supplied word has been inserted into this ternary tree.
   *
   * @param  word  to search for
   *
   * @return  whether the word was found
   */
  public boolean search(final CharSequence word)
  {
    return searchNode(root, word.codePoints().toArray(), 0);
  }


  /**
   * Returns an array of strings which partially match the supplied word. word should be of the format '.e.e.e' Where
   * the '.' character represents any valid character. Possible results from this query include: Helene, delete, or
   * severe Note that no substring matching occurs, results only include strings of the same length. If the supplied
   * word does not contain the '.' character, then a regular search is performed.
   *
   * <p><strong>NOTE</strong> This method is not supported for case insensitive ternary trees. Since the tree is built
   * without regard to case any words returned from the tree may or may not match the case of the supplied word.</p>
   *
   * @param  word  to search for
   *
   * @return  array of matching words
   *
   * @throws  UnsupportedOperationException  if this is a case insensitive ternary tree
   */
  public CharSequence[] partialSearch(final CharSequence word)
  {
    if (comparator == CASE_INSENSITIVE_COMPARATOR) {
      throw new UnsupportedOperationException("Partial search is not supported for case insensitive ternary trees");
    }

    final List<CharSequence> matches = partialSearchNode(root, null, "", word, 0);
    return matches == null ? EMPTY_ARRAY : matches.toArray(new CharSequence[0]);
  }


  /**
   * Return an array of strings which are near to the supplied word by the supplied distance. For the query
   * nearSearch("fisher", 2): Possible results include: cipher, either, fishery, kosher, sister. If the supplied
   * distance is not &gt; 0, then a regular search is performed.
   *
   * <p><strong>NOTE</strong> This method is not supported for case insensitive ternary trees. Since the tree is built
   * without regard to case any words returned from the tree may or may not match the case of the supplied word.</p>
   *
   * @param  word  to search for
   * @param  distance  for valid match
   *
   * @return  array of matching words
   *
   * @throws  UnsupportedOperationException  if this is a case insensitive ternary tree
   */
  public CharSequence[] nearSearch(final CharSequence word, final int distance)
  {
    if (comparator == CASE_INSENSITIVE_COMPARATOR) {
      throw new UnsupportedOperationException("Near search is not supported for case insensitive ternary trees");
    }

    final List<CharSequence> matches = nearSearchNode(root, distance, null, "", word, 0);
    return matches == null ? EMPTY_ARRAY : matches.toArray(new CharSequence[0]);
  }


  /**
   * Returns a list of all the words in this ternary tree. This is a very expensive operation, every node in the tree is
   * traversed. The returned list cannot be modified.
   *
   * @return  unmodifiable list of words
   */
  public List<CharSequence> getWords()
  {
    final List<CharSequence> words = traverseNode(root, "", new ArrayList<>());
    return Collections.unmodifiableList(words);
  }


  /**
   * Prints an ASCII representation of this ternary tree to the supplied writer. This is a very expensive operation,
   * every node in the tree is traversed. The output produced is hard to read, but it should give an indication of
   * whether or not your tree is balanced.
   *
   * @param  out  to print to
   * @param  fullPath  specifies whether each line should show the full path from root or only the suffix
   *
   * @throws  IOException  if an error occurs
   */
  public void print(final Writer out, final boolean fullPath) throws IOException
  {
    final StringBuilder buffer = new StringBuilder();
    printNode(root, "", 0, fullPath, buffer);
    out.write(buffer.toString());
    out.flush();
  }


  /**
   * Prints an ASCII representation of this ternary tree to the supplied writer. This is a very expensive operation,
   * every node in the tree is traversed. The output produced is hard to read, but it should give an indication of
   * whether or not your tree is balanced.
   *
   * @param  out  to print to
   *
   * @throws  IOException  if an error occurs
   */
  public void print(final Writer out) throws IOException
  {
    print(out, false);
  }


  /**
   * Recursively inserts a word into the ternary tree one node at a time beginning at the supplied node.
   *
   * @param  node  to put character in
   * @param  word  to be inserted
   * @param  index  of character in word
   *
   * @return  ternary node to insert
   */
  // CheckStyle:FinalParametersCheck OFF
  private TernaryNode insertNode(TernaryNode node, final CharSequence word, final int index)
  // CheckStyle:FinalParametersCheck ON
  {
    if (index < word.length()) {
      final int[] codePoints = word.codePoints().toArray();
      final int cp = codePoints[index];
      if (node == null) {
        // CheckStyle:ParameterAssignmentCheck OFF
        node = new TernaryNode(cp);
        // CheckStyle:ParameterAssignmentCheck ON
      }

      final String split = UnicodeString.toString(node.getSplitChar());
      final int cmp = comparator.compare(UnicodeString.toString(cp), split);
      if (cmp < 0) {
        node.setLokid(insertNode(node.getLokid(), word, index));
      } else if (cmp > 0) {
        node.setHikid(insertNode(node.getHikid(), word, index));
      } else if (index == word.length() - 1) {
        node.setEndOfWord(true);
      } else {
        node.setEqkid(insertNode(node.getEqkid(), word, index + Character.charCount(cp)));
      }
    }
    return node;
  }


  /**
   * Recursively searches for a word in the ternary tree one node at a time beginning at the supplied node.
   *
   * @param  node  to search in
   * @param  word  to search for
   * @param  index  of character in word
   *
   * @return  whether the word was found
   */
  // CheckStyle:ReturnCount OFF
  private boolean searchNode(final TernaryNode node, final int[] word, final int index)
  {
    if (node != null && index < word.length) {
      final int cp = word[index];
      final String split = UnicodeString.toString(node.getSplitChar());
      final int cmp = comparator.compare(UnicodeString.toString(cp), split);
      if (cmp < 0) {
        return searchNode(node.getLokid(), word, index);
      } else if (cmp > 0) {
        return searchNode(node.getHikid(), word, index);
      } else if (index == word.length - 1) {
        return node.isEndOfWord();
      } else {
        return searchNode(node.getEqkid(), word, index + Character.charCount(cp));
      }
    }
    return false;
  }
  // CheckStyle:ReturnCount ON


  /**
   * Recursively searches for a partial word in the ternary tree one node at a time beginning at the supplied node.
   *
   * @param  node  to search in
   * @param  matches  of partial matches
   * @param  match  the current word being examined
   * @param  word  to search for
   * @param  index  of character in word
   *
   * @return  list of matches
   */
  // CheckStyle:FinalParametersCheck OFF
  private List<CharSequence> partialSearchNode(final TernaryNode node, List<CharSequence> matches,
    final CharSequence match, final CharSequence word, final int index)
  // CheckStyle:FinalParametersCheck ON
  {
    final int[] codePoints = word.codePoints().toArray();
    if (node != null && index < word.length()) {
      final int cp = codePoints[index];
      final String split = UnicodeString.toString(node.getSplitChar());
      final int cmp = comparator.compare(UnicodeString.toString(cp), split);
      if (cp == '.' || cmp < 0) {
        // CheckStyle:ParameterAssignmentCheck OFF
        matches = partialSearchNode(node.getLokid(), matches, match, word, index);
        // CheckStyle:ParameterAssignmentCheck ON
      }
      if (cp == '.' || cmp == 0) {
        if (index == word.length() - 1) {
          if (node.isEndOfWord()) {
            if (matches == null) {
              // CheckStyle:ParameterAssignmentCheck OFF
              matches = new ArrayList<>();
              // CheckStyle:ParameterAssignmentCheck ON
            }
            matches.add(match + split);
          }
        } else {
          // CheckStyle:ParameterAssignmentCheck OFF
          matches = partialSearchNode(node.getEqkid(), matches, match + split, word, index + Character.charCount(cp));
          // CheckStyle:ParameterAssignmentCheck ON
        }
      }
      if (cp == '.' || cmp > 0) {
        // CheckStyle:ParameterAssignmentCheck OFF
        matches = partialSearchNode(node.getHikid(), matches, match, word, index);
        // CheckStyle:ParameterAssignmentCheck ON
      }
    }
    return matches;
  }


  /**
   * Recursively searches for a near match word in the ternary tree one node at a time beginning at the supplied node.
   *
   * @param  node  to search in
   * @param  distance  of a valid match, must be > 0
   * @param  matches  list of near matches
   * @param  match  the current word being examined
   * @param  word  to search for
   * @param  index  of character in word
   *
   * @return  list of matches
   */
  // CheckStyle:FinalParametersCheck OFF
  private List<CharSequence> nearSearchNode(final TernaryNode node, final int distance, List<CharSequence> matches,
    final CharSequence match, final CharSequence word, final int index)
  // CheckStyle:FinalParametersCheck ON
  {
    if (node != null && distance >= 0) {

      final int[] codePoints = word.codePoints().toArray();
      final int cp = index < word.length() ? codePoints[index] : Character.MAX_VALUE;
      final String split = UnicodeString.toString(node.getSplitChar());
      final int cmp = comparator.compare(UnicodeString.toString(cp), split);

      if (distance > 0 || cmp < 0) {
        // CheckStyle:ParameterAssignmentCheck OFF
        matches = nearSearchNode(node.getLokid(), distance, matches, match, word, index);
        // CheckStyle:ParameterAssignmentCheck ON
      }

      final String newMatch = match + split;
      if (cmp == 0) {
        if (node.isEndOfWord() && newMatch.length() + distance >= word.length()) {
          if (matches == null) {
            // CheckStyle:ParameterAssignmentCheck OFF
            matches = new ArrayList<>();
            // CheckStyle:ParameterAssignmentCheck ON
          }
          matches.add(newMatch);
        }

        // CheckStyle:ParameterAssignmentCheck OFF
        matches = nearSearchNode(node.getEqkid(), distance, matches, newMatch, word, index + Character.charCount(cp));
        // CheckStyle:ParameterAssignmentCheck ON
      } else {
        if (node.isEndOfWord() && distance - 1 >= 0 && newMatch.length() + distance - 1 >= word.length()) {
          if (matches == null) {
            // CheckStyle:ParameterAssignmentCheck OFF
            matches = new ArrayList<>();
            // CheckStyle:ParameterAssignmentCheck ON
          }
          matches.add(newMatch);
        }

        // CheckStyle:ParameterAssignmentCheck OFF
        matches = nearSearchNode(
          node.getEqkid(), distance - 1, matches, newMatch, word, index + Character.charCount(cp));
        // CheckStyle:ParameterAssignmentCheck ON
      }

      if (distance > 0 || cmp > 0) {
        // CheckStyle:ParameterAssignmentCheck OFF
        matches = nearSearchNode(node.getHikid(), distance, matches, match, word, index);
        // CheckStyle:ParameterAssignmentCheck ON
      }
    }
    return matches;
  }


  /**
   * Recursively traverses every node in the ternary tree one node at a time beginning at the supplied node. The result
   * is a string representing every word, which is delimited by the LINE_SEPARATOR character.
   *
   * @param  node  to begin traversing
   * @param  s  string of words found at the supplied node
   * @param  words  which will be returned (recursive function)
   *
   * @return  list of strings containing all words from the supplied node
   */
  // CheckStyle:FinalParametersCheck OFF
  private List<CharSequence> traverseNode(final TernaryNode node, final String s, List<CharSequence> words)
  // CheckStyle:FinalParametersCheck ON
  {
    if (node != null) {

      // CheckStyle:ParameterAssignmentCheck OFF
      words = traverseNode(node.getLokid(), s, words);
      // CheckStyle:ParameterAssignmentCheck ON

      final String split = UnicodeString.toString(node.getSplitChar());
      if (node.getEqkid() != null) {
        // CheckStyle:ParameterAssignmentCheck OFF
        words = traverseNode(node.getEqkid(), s + split, words);
        // CheckStyle:ParameterAssignmentCheck ON
      }

      if (node.isEndOfWord()) {
        words.add(s + split);
      }

      // CheckStyle:ParameterAssignmentCheck OFF
      words = traverseNode(node.getHikid(), s, words);
      // CheckStyle:ParameterAssignmentCheck ON
    }
    return words;
  }


  /**
   * Recursively traverses every node in the ternary tree rooted at the supplied node.
   * The result is an ASCII string representation of the tree rooted at the supplied node.
   *
   * @param  node  to begin traversing
   * @param  s  the string representation of the current chain of equal kid nodes
   * @param  depth  depth of the current chain of nodes
   * @param  fullPath  specifies whether each line should show the full path from root or only the suffix
   * @param  buffer  the buffer to which the output is printed
   */
  private void printNode(
    final TernaryNode node, final String s, final int depth,
    final boolean fullPath, final StringBuilder buffer)
  {
    if (node != null) {
      printNode(node.getLokid(), s + "  /", depth + 1, fullPath, buffer);

      final int cp = node.getSplitChar();
      if (node.getEqkid() != null) {
        final String suffix = node.isEndOfWord() ? "=" : "-";
        printNode(node.getEqkid(), s + '-' + UnicodeString.toString(cp) + suffix, depth + 1, fullPath, buffer);
      } else {
        final int i = fullPath ? -1 : Math.max(s.lastIndexOf("  /"), s.lastIndexOf("  \\"));
        final String line = i < 0 ? s : s.substring(0, i).replaceAll("\\.", " ") + s.substring(i);
        buffer.append(line).append('-').append(UnicodeString.toString(cp)).append(TernaryTree.LINE_SEPARATOR);
      }

      printNode(node.getHikid(), s + "  \\", depth + 1, fullPath, buffer);
    }
  }

  /**
   * Returns a histogram of how many words end at each depth.
   *
   * @param node the node at the root of the tree to count
   * @param depth the depth of the node from root
   * @param histogram the depth count histogram to update
   * @return a histogram of how many words end at each depth
   */
  private Map<Integer, Integer> getNodeStats(
    final TernaryNode node, final int depth,
    final Map<Integer, Integer> histogram)
  {
    if (node != null) {
      if (node.isEndOfWord()) {
        histogram.put(depth, histogram.getOrDefault(depth, 0) + 1);
      }
      getNodeStats(node.getLokid(), depth + 1, histogram);
      getNodeStats(node.getEqkid(), depth + 1, histogram);
      getNodeStats(node.getHikid(), depth + 1, histogram);
    }
    return histogram;
  }

  /**
   * Returns a histogram of how many words end at each depth.
   *
   * @return a histogram of how many words end at each depth
   */
  protected Map<Integer, Integer> getNodeStats()
  {
    return getNodeStats(root, 0, new HashMap<>());
  }
}

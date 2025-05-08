/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

/**
 * Implementation of a node contained in a ternary tree.
 *
 * @author  Middleware Services
 */

public class TernaryNode
{

  /** code point of this node. */
  private int splitchar;

  /** whether this character is the end of a word. */
  private boolean endOfWord;

  /** low child of this node. */
  private TernaryNode lokid;

  /** equal child of this node. */
  private TernaryNode eqkid;

  /** high child of this node. */
  private TernaryNode hikid;


  /**
   * Create a new ternary node with the supplied code point.
   *
   * @param  cp  code point
   */
  public TernaryNode(final int cp)
  {
    splitchar = cp;
  }


  /**
   * Returns the split character.
   *
   * @return  code point
   */
  public int getSplitChar()
  {
    return splitchar;
  }


  /**
   * Sets the split character.
   *
   * @param  cp  code point
   */
  public void setSplitChar(final int cp)
  {
    splitchar = cp;
  }


  /**
   * Returns whether this node is at the end of a word.
   *
   * @return  whether this node is at the end of a word
   */
  public boolean isEndOfWord()
  {
    return endOfWord;
  }


  /**
   * Sets whether this node is at the end of a word.
   *
   * @param  b  whether this node is at the end of a word
   */
  public void setEndOfWord(final boolean b)
  {
    endOfWord = b;
  }


  /**
   * Returns the lokid node in relation to this node.
   *
   * @return  ternary node
   */
  public TernaryNode getLokid()
  {
    return lokid;
  }


  /**
   * Sets the lokid node in relation to this node.
   *
   * @param  node  ternary node
   */
  public void setLokid(final TernaryNode node)
  {
    lokid = node;
  }


  /**
   * Returns the eqkid node in relation to this node.
   *
   * @return  ternary node
   */
  public TernaryNode getEqkid()
  {
    return eqkid;
  }


  /**
   * Sets the eqkid node in relation to this node.
   *
   * @param  node  ternary node
   */
  public void setEqkid(final TernaryNode node)
  {
    eqkid = node;
  }


  /**
   * Returns the hikid node in relation to this node.
   *
   * @return  ternary node
   */
  public TernaryNode getHikid()
  {
    return hikid;
  }


  /**
   * Sets the hikid node in relation to this node.
   *
   * @param  node  ternary node
   */
  public void setHikid(final TernaryNode node)
  {
    hikid = node;
  }
}

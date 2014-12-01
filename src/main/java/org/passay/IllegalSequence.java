/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Describes an illegal sequence of characters as strings where each
 * character may have a variant form.
 *
 * @author  Middleware Services
 */
public class IllegalSequence
{
  /** String of characters. */
  private final String original;

  /** String of character variants. */
  private final String variant;


  /**
   * Creates a new instance of illegal characters.
   *
   * @param  characters  Sequence of characters.
   */
  public IllegalSequence(final String characters)
  {
    this(characters, null);
  }


  /**
   * Creates a new instance with original and variant character forms.
   *
   * @param  originalCharacters  String of original characters.
   * @param  variantCharacters Corresponding string of variant characters.
   */
  public IllegalSequence(
    final String originalCharacters,
    final String variantCharacters)
  {
    if (originalCharacters == null) {
      throw new IllegalArgumentException("Characters cannot be null");
    }
    if (variantCharacters != null &&
      originalCharacters.length() != variantCharacters.length()) {
      throw new IllegalArgumentException(
        "Original and variant sequences must have same length");
    }
    this.original = originalCharacters;
    this.variant = variantCharacters;
  }


  /** @return  String of original characters. */
  public String getOriginal()
  {
    return original;
  }


  /** @return  String of variant characters, null if no variant form. */
  public String getVariant()
  {
    return variant;
  }


  /**
   * Determines whether the character at the given index of the sequence
   * matches the given value. Both original and variant forms are considered.
   *
   * @param  index  Character sequence index.
   * @param  c  Character to check for.
   * @return  True if sequence contains given character, false otherwise.
   */
  public boolean matches(final int index, final char c)
  {
    return original.charAt(index) == c ||
      variant != null && variant.charAt(index) == c;
  }


  /** @return  Length of character sequence. */
  public int length()
  {
    return this.original.length();
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Class for determining what type and what quantity of characters a password
 * contains.
 *
 * @author  Middleware Services
 */
public class Password
{

  /** Stores the password. */
  private final String password;

  /** Digits in the password [0-9]. */
  private final StringBuilder digits;

  /** Non-Digits in the password ![0-9]. */
  private final StringBuilder nonDigits;

  /** Alphabetical characters in the password [a-zA-Z]. */
  private final StringBuilder alphabetical;

  /** Non-Alphabetical characters in the password ![a-zA-Z]. */
  private final StringBuilder nonAlphabetical;

  /** Alphanumeric characters in the password [a-zA-Z0-9]. */
  private final StringBuilder alphanumeric;

  /** Non-Alphanumeric characters in the password ![a-zA-Z0-9]. */
  private final StringBuilder nonAlphanumeric;

  /** Uppercase characters in the password [A-Z]. */
  private final StringBuilder uppercase;

  /** Lowercase characters in the password [a-z]. */
  private final StringBuilder lowercase;

  /** Whitespace characters in the password [\s]. */
  private final StringBuilder whitespace;


  /**
   * Create a new password with the supplied password text.
   *
   * @param  text  password
   */
  public Password(final String text)
  {
    password = text;

    digits = new StringBuilder(password.length());
    nonDigits = new StringBuilder(password.length());
    alphabetical = new StringBuilder(password.length());
    nonAlphabetical = new StringBuilder(password.length());
    alphanumeric = new StringBuilder(password.length());
    nonAlphanumeric = new StringBuilder(password.length());
    uppercase = new StringBuilder(password.length());
    lowercase = new StringBuilder(password.length());
    whitespace = new StringBuilder(password.length());

    for (int i = 0; i < password.length(); i++) {
      final char c = password.charAt(i);
      if (Character.isDigit(c)) {
        digits.append(c);
        alphanumeric.append(c);
        nonAlphabetical.append(c);
      } else if (Character.isLetter(c)) {
        nonDigits.append(c);
        alphanumeric.append(c);
        alphabetical.append(c);
        if (Character.isUpperCase(c)) {
          uppercase.append(c);
        } else if (Character.isLowerCase(c)) {
          lowercase.append(c);
        }
      } else {
        if (Character.isWhitespace(c)) {
          whitespace.append(c);
        }
        nonDigits.append(c);
        nonAlphanumeric.append(c);
        nonAlphabetical.append(c);
      }
    }
  }


  /**
   * Returns the text of this password.
   *
   * @return  password
   */
  public String getText()
  {
    return password;
  }


  /**
   * Returns the length of this password.
   *
   * @return  password length
   */
  public int length()
  {
    return password.length();
  }


  /**
   * Returns whether or not this password contains digits.
   *
   * @return  whether or not the password contains digits
   */
  public boolean containsDigits()
  {
    return digits.length() > 0;
  }


  /**
   * Returns the number of digits in this password.
   *
   * @return  number of digits in the password
   */
  public int getNumberOfDigits()
  {
    return digits.length();
  }


  /**
   * Returns the digits in this password.
   *
   * @return  digits in this password
   */
  public char[] getDigits()
  {
    char[] array = null;
    if (digits != null && digits.length() > 0) {
      array = digits.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains non-digits.
   *
   * @return  whether or not the password contains non-digits
   */
  public boolean containsNonDigits()
  {
    return nonDigits.length() > 0;
  }


  /**
   * Returns the number of non-digits in this password.
   *
   * @return  number of non-digits in this password
   */
  public int getNumberOfNonDigits()
  {
    return nonDigits.length();
  }


  /**
   * Returns the non-digits in this password.
   *
   * @return  non-digits in this password
   */
  public char[] getNonDigits()
  {
    char[] array = null;
    if (nonDigits != null && nonDigits.length() > 0) {
      array = nonDigits.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains alphabetical characters.
   *
   * @return  whether or not the password contains alphabetical characters
   */
  public boolean containsAlphabetical()
  {
    return alphabetical.length() > 0;
  }


  /**
   * Returns the number of alphabetical characters in this password.
   *
   * @return  number of alphabetical characters in this password
   */
  public int getNumberOfAlphabetical()
  {
    return alphabetical.length();
  }


  /**
   * Returns the alphabetical characters in this password.
   *
   * @return  alphabetical characters in this password
   */
  public char[] getAlphabetical()
  {
    char[] array = null;
    if (alphabetical != null && alphabetical.length() > 0) {
      array = alphabetical.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains non-alphabetical characters.
   *
   * @return  whether or not the password contains non-alphabetical characters
   */
  public boolean containsNonAlphabetical()
  {
    return nonAlphabetical.length() > 0;
  }


  /**
   * Returns the number of non-alphabetical characters in this password.
   *
   * @return  number of non-alphabetical characters in this password
   */
  public int getNumberOfNonAlphabetical()
  {
    return nonAlphabetical.length();
  }


  /**
   * Returns the non-alphabetical characters in this password.
   *
   * @return  non-alphabetical characters in this password
   */
  public char[] getNonAlphabetical()
  {
    char[] array = null;
    if (nonAlphabetical != null && nonAlphabetical.length() > 0) {
      array = nonAlphabetical.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains alphanumeric characters.
   *
   * @return  whether or not the password contains alphanumeric characters
   */
  public boolean containsAlphanumeric()
  {
    return alphanumeric.length() > 0;
  }


  /**
   * Returns the number of alphanumeric characters in this password.
   *
   * @return  number of alphanumeric characters in this password
   */
  public int getNumberOfAlphanumeric()
  {
    return alphanumeric.length();
  }


  /**
   * Returns the alphanumeric characters in this password.
   *
   * @return  alphanumeric characters in this password
   */
  public char[] getAlphanumeric()
  {
    char[] array = null;
    if (alphanumeric != null && alphanumeric.length() > 0) {
      array = alphanumeric.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains non-alphanumeric characters.
   *
   * @return  whether or not the password contains non-alphanumeric characters
   */
  public boolean containsNonAlphanumeric()
  {
    return nonAlphanumeric.length() > 0;
  }


  /**
   * Returns the number of non-alphanumeric characters in this password.
   *
   * @return  number of non-alphanumeric characters in this password
   */
  public int getNumberOfNonAlphanumeric()
  {
    return nonAlphanumeric.length();
  }


  /**
   * Returns the non-alphanumeric characters in this password.
   *
   * @return  non-alphanumeric characters in this password
   */
  public char[] getNonAlphanumeric()
  {
    char[] array = null;
    if (nonAlphanumeric != null && nonAlphanumeric.length() > 0) {
      array = nonAlphanumeric.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains uppercase characters.
   *
   * @return  whether or not the password contains uppercase characters
   */
  public boolean containsUppercase()
  {
    return uppercase.length() > 0;
  }


  /**
   * Returns the number of uppercase characters in this password.
   *
   * @return  number of uppercase characters in this password
   */
  public int getNumberOfUppercase()
  {
    return uppercase.length();
  }


  /**
   * Returns the uppercase characters in this password.
   *
   * @return  uppercase characters in this password
   */
  public char[] getUppercase()
  {
    char[] array = null;
    if (uppercase != null && uppercase.length() > 0) {
      array = uppercase.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this password contains lowercase characters.
   *
   * @return  whether or not the password contains uppercase characters
   */
  public boolean containsLowercase()
  {
    return lowercase.length() > 0;
  }


  /**
   * Returns the number of lowercase characters in this password.
   *
   * @return  number of lowercase characters in this password
   */
  public int getNumberOfLowercase()
  {
    return lowercase.length();
  }


  /**
   * Returns the lowercase characters in this password.
   *
   * @return  lowercase characters in this password
   */
  public char[] getLowercase()
  {
    char[] array = null;
    if (lowercase != null && lowercase.length() > 0) {
      array = lowercase.toString().toCharArray();
    }
    return array;
  }


  /**
   * Returns whether or not this Password contains whitespace characters.
   *
   * @return  whether or not the password contains whitespace characters
   */
  public boolean containsWhitespace()
  {
    return whitespace.length() > 0;
  }


  /**
   * Returns the number of whitespace characters in this password.
   *
   * @return  number of whitespace characters in this password
   */
  public int getNumberOfWhitespace()
  {
    return whitespace.length();
  }


  /**
   * Returns the whitespace characters in this password.
   *
   * @return  whitespace characters in this password
   */
  public char[] getWhitespace()
  {
    char[] array = null;
    if (whitespace != null && whitespace.length() > 0) {
      array = whitespace.toString().toCharArray();
    }
    return array;
  }


  @Override
  public String toString()
  {
    return password;
  }
}

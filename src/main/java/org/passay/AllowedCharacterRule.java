/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Rule for determining if a password contains allowed characters. Validation will fail unless the password contains
 * only allowed characters.
 *
 * @author  Middleware Services
 */
public class AllowedCharacterRule implements Rule
{

  /** Error code for allowed character failures. */
  public static final String ERROR_CODE = "ALLOWED_CHAR";

  /** Whether to report all sequence matches or just the first. */
  protected boolean reportAllFailures;

  /** Stores the characters that are allowed. */
  private final char[] allowedCharacters;

  /** Where to match whitespace. */
  private final StringMatch stringMatch;


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   */
  public AllowedCharacterRule(final char[] c)
  {
    this(c, StringMatch.Contains, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  match  how to match allowed characters
   */
  public AllowedCharacterRule(final char[] c, final StringMatch match)
  {
    this(c, match, true);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final char[] c, final boolean reportAll)
  {
    this(c, StringMatch.Contains, reportAll);
  }


  /**
   * Create a new allowed character rule.
   *
   * @param  c  allowed characters
   * @param  match  how to match allowed characters
   * @param  reportAll  whether to report all matches or just the first
   */
  public AllowedCharacterRule(final char[] c, final StringMatch match, final boolean reportAll)
  {
    if (c.length > 0) {
      allowedCharacters = c;
    } else {
      throw new IllegalArgumentException("allowed characters length must be greater than zero");
    }
    Arrays.sort(allowedCharacters);
    stringMatch = match;
    reportAllFailures = reportAll;
  }


  /**
   * Returns the allowed characters for this rule.
   *
   * @return  allowedCharacters
   */
  public char[] getAllowedCharacters()
  {
    return allowedCharacters;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final RuleResult result = new RuleResult(true);
    final Set<Character> matches = new HashSet<>();
    final String text = passwordData.getPassword();
    for (char c : text.toCharArray()) {
      if (Arrays.binarySearch(allowedCharacters, c) < 0 && !matches.contains(c)) {
        if (StringMatch.Contains.equals(stringMatch) || stringMatch.match(text, c)) {
          result.setValid(false);
          result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(c)));
          if (!reportAllFailures) {
            break;
          }
          matches.add(c);
        }
      }
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  c  illegal character
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final char c)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("illegalCharacter", c);
    m.put("stringMatch", stringMatch);
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format("%s@%h::reportAllFailures=%s,stringMatch=%s,allowedCharacters=%s",
        getClass().getName(),
        hashCode(),
        reportAllFailures,
        stringMatch,
        allowedCharacters != null ? Arrays.toString(allowedCharacters) : null);
  }
}

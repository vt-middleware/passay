/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.nio.charset.StandardCharsets;

/**
 * Rule for determining if a password contains whitespace characters.
 *
 * @author  Middleware Services
 */
public class WhitespaceRule implements Rule
{

  /** Characters: TAB,LF,VT,FF,CR,Space. */
  public static final String CHARS =
    new String(
      new byte[] {
        (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D,
        (byte) 0x20,
      },
      StandardCharsets.UTF_8);

  /** Error code for whitespace rule violation. */
  public static final String ERROR_CODE = "ILLEGAL_WHITESPACE";


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final int charCount = PasswordUtils.getMatchingCharacterCount(
      CHARS, passwordData.getPassword());
    if (charCount == 0) {
      return new RuleResult(true);
    } else {
      return new RuleResult(false, new RuleResultDetail(ERROR_CODE, null));
    }
  }
}

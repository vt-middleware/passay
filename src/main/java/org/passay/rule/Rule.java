/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.logic.PasswordData;
import org.passay.rule.result.RuleResult;

/**
 * Interface for password strength rules.
 *
 * @author  Middleware Services
 */
public interface Rule
{


  /**
   * Validates the supplied password data per the requirements of this rule.
   *
   * @param  passwordData  to verify (not null).
   *
   * @return  details on password verification
   *
   * @throws  NullPointerException  if the rule data is null.
   */
  RuleResult validate(PasswordData passwordData);
}

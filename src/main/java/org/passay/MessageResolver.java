/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Strategy pattern interface for resolving messages from password validation
 * failures described by a {@link RuleResultDetail} object.
 *
 * @author  Middleware Services
 */
public interface MessageResolver
{


  /**
   * Resolves the message for the supplied rule result detail.
   *
   * @param  detail  rule result detail
   *
   * @return  message for the detail error code
   */
  String resolve(RuleResultDetail detail);
}

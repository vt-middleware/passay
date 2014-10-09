/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

/**
 * Resolves messages from rule result details in order to provide a facility for
 * customizing messages such as password rule validation failures.
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

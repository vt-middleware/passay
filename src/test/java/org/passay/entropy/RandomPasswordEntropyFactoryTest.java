/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import org.passay.PasswordData;
import org.passay.Rule;
import org.testng.annotations.Test;

/**
 * Unit test for {@link RandomPasswordEntropyFactory}.
 *
 * @author  Middleware Services
 */
public class RandomPasswordEntropyFactoryTest extends AbstractEntropyFactoryTest
{


  /**
   * @param  rules  to create entropy with
   * @param  data  to create entropy with
   *
   * @throws Exception On test failure.
   */
  @Test(groups = {"entrpytest"}, dataProvider = "randomGeneratedData")
  public void createEntropy(final List<Rule> rules, final PasswordData data)
    throws Exception
  {
    RandomPasswordEntropyFactory.createEntropy(rules, data);
  }


  /**
   * @param  rules  to create entropy with
   * @param  data  to create entropy with
   *
   * @throws Exception On test failure.
   */
  @Test(
    groups = {"entrpytest"},
    dataProvider = "userGeneratedData",
    expectedExceptions = IllegalArgumentException.class)
  public void invalidPasswordData(final List<Rule> rules, final PasswordData data)
    throws Exception
  {
    RandomPasswordEntropyFactory.createEntropy(rules, data);
  }
}

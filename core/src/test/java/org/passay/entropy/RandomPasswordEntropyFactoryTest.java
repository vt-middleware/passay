/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.List;
import org.passay.PasswordData;
import org.passay.rule.Rule;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
   */
  @Test(dataProvider = "randomGeneratedData")
  public void createEntropy(final List<Rule> rules, final PasswordData data)
  {
    RandomPasswordEntropyFactory.createEntropy(rules, data);
  }


  /**
   * @param  rules  to create entropy with
   * @param  data  to create entropy with
   */
  @Test(dataProvider = "userGeneratedData")
  public void invalidPasswordData(final List<Rule> rules, final PasswordData data)
  {
    assertThatThrownBy(() -> RandomPasswordEntropyFactory.createEntropy(rules, data))
      .isInstanceOf(IllegalArgumentException.class);
  }
}

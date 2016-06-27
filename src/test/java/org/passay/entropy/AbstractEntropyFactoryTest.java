/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.entropy;

import java.util.ArrayList;
import java.util.List;
import org.passay.AllowedCharacterRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.Rule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

/**
 * Base class for all entropy factory tests.
 *
 * @author  Middleware Services
 */
public abstract class AbstractEntropyFactoryTest
{
  /** Test checker. */
  private final List<Rule> rules = new ArrayList<>();


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"entrpytest"})
  public void createRules()
    throws Exception
  {
    final AllowedCharacterRule allowedRule = new AllowedCharacterRule(
      new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'L', });

    final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    charRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
    charRule.setNumberOfCharacteristics(3);

    rules.add(charRule);
    rules.add(allowedRule);
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "randomGeneratedData")
  public Object[][] randomPasswordData()
    throws Exception
  {
    return
      new Object[][] {
        {
          rules,
          new PasswordData("heLlo", PasswordData.Origin.Generated),
        },
      };
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "userGeneratedData")
  public Object[][] userPasswordData()
    throws Exception
  {
    return
      new Object[][] {
        {
          rules,
          new PasswordData("heLlo", PasswordData.Origin.User),
        },
      };
  }
}

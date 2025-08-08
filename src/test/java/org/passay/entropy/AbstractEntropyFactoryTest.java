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
import org.passay.UnicodeString;
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


  /**
   * Setup test resources.
   */
  @BeforeClass(groups = "entrpytest")
  public void createRules()
  {
    final AllowedCharacterRule allowedRule = new AllowedCharacterRule(new UnicodeString(
      new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'L', }));

    final CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule(
      3,
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1));

    rules.add(charRule);
    rules.add(allowedRule);
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "randomGeneratedData")
  public Object[][] randomPasswordData()
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
   */
  @DataProvider(name = "userGeneratedData")
  public Object[][] userPasswordData()
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

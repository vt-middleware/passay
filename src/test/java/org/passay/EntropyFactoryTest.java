/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.passay.entropy.RandomPasswordEntropy;
import org.passay.entropy.RandomPasswordEntropyFactory;
import org.passay.entropy.ShannonEntropy;
import org.passay.entropy.ShannonEntropyFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link RandomPasswordEntropyFactory} and {@link ShannonEntropyFactory}.
 *
 * @author  Middleware Services
 */
public class EntropyFactoryTest extends AbstractRuleTest
{

  /** Test checker. */
  private final List<Rule> rules = new ArrayList<>();

  /** Password matching AllowedCharacterRules defined under createChecker. */
  private final PasswordData matchingPasswordUser = new PasswordData("heLlo", PasswordData.Origin.USER_GENERATED);

  /** Password matching AllowedCharacterRules defined under createChecker. */
  private final PasswordData matchingPasswordRnd = new PasswordData("heLlo", PasswordData.Origin.RANDOM_GENERATED);

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
   * @throws Exception On test failure.
   */
  @Test(groups = {"entrpytest"})
  public void estimateEntropyFactories() throws Exception
  {

    //RandomPasswordEntropy should not generate a RandomPasswordEntropy from a given USER_GENERATED password origin.
    RandomPasswordEntropy randomMatchingByUser = null;
    try {
      randomMatchingByUser =
              RandomPasswordEntropyFactory.createEntropy(rules, matchingPasswordUser);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Throwable e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }

    //Positive test
    final RandomPasswordEntropy randomMatchingByRandom =
            RandomPasswordEntropyFactory.createEntropy(rules, matchingPasswordRnd);

    //ShannonEntropyFactory should not generate a ShannonEntropy from a given RANDOM_GENERATED password origin.
    ShannonEntropy shannonMatchingByRandom = null;
    try {
      shannonMatchingByRandom =
              ShannonEntropyFactory.createEntropy(rules, matchingPasswordRnd);
      AssertJUnit.fail("Should have thrown IllegalArgumentException");
    } catch (Throwable e) {
      AssertJUnit.assertEquals(IllegalArgumentException.class, e.getClass());
    }

    //Positive test
    final ShannonEntropy shannonMatchingByUser = ShannonEntropyFactory.createEntropy(rules, matchingPasswordUser);

  }

}

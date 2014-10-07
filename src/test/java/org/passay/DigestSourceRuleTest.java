/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DigestSourceRule}.
 *
 * @author  Middleware Services
 */
public class DigestSourceRuleTest extends AbstractRuleTest
{

  /** Test password. */
  private static final String VALID_PASS = "t3stUs3r01";

  /** Test password. */
  private static final String SOURCE_PASS = "t3stUs3r04";

  /** Test username. */
  private static final String USER = "testuser";

  /** For testing. */
  private final List<PasswordData.Reference> sourceRefs = new ArrayList<>();

  /** For testing. */
  private final DigestSourceRule digestRule = new DigestSourceRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1));

  /** For testing. */
  private final DigestSourceRule emptyRule = new DigestSourceRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1));


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    sourceRefs.add(
      new PasswordData.SourceReference(
        "System B", "CJGTDMQRP+rmHApkcijC80aDV0o="));
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
    throws Exception
  {
    return
      new Object[][] {

        {
          digestRule,
          PasswordData.newInstance(VALID_PASS, USER, sourceRefs),
          null,
        },
        {
          digestRule,
          PasswordData.newInstance(SOURCE_PASS, USER, sourceRefs),
          codes(SourceRule.ERROR_CODE),
        },

        {
          emptyRule,
          PasswordData.newInstance(VALID_PASS, USER, null),
          null,
        },
        {
          emptyRule,
          PasswordData.newInstance(SOURCE_PASS, USER, null),
          null,
        },
      };
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"passtest"})
  public void resolveMessage()
    throws Exception
  {
    final RuleResult result = digestRule.validate(
      PasswordData.newInstance(SOURCE_PASS, USER, sourceRefs));
    for (RuleResultDetail detail : result.getDetails()) {
      AssertJUnit.assertEquals(
        String.format(
          "Password cannot be the same as your %s password.",
          "System B"),
        DEFAULT_RESOLVER.resolve(detail));
      AssertJUnit.assertNotNull(EMPTY_RESOLVER.resolve(detail));
    }
  }
}

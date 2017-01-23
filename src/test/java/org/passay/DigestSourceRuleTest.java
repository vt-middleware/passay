/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.List;
import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

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
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));

  /** For testing. */
  private final DigestSourceRule emptyRule = new DigestSourceRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));


  /** Initialize rules for this test. */
  @BeforeClass(groups = {"passtest"})
  public void createRules()
  {
    sourceRefs.add(new PasswordData.SourceReference("System B", "CJGTDMQRP+rmHApkcijC80aDV0o="));
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
          TestUtils.newPasswordData(VALID_PASS, USER, null, sourceRefs),
          null,
        },
        {
          digestRule,
          TestUtils.newPasswordData(SOURCE_PASS, USER, null, sourceRefs),
          codes(SourceRule.ERROR_CODE),
        },

        {
          emptyRule,
          TestUtils.newPasswordData(VALID_PASS, USER),
          null,
        },
        {
          emptyRule,
          TestUtils.newPasswordData(SOURCE_PASS, USER),
          null,
        },
      };
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
    throws Exception
  {
    return
      new Object[][] {
        {
          digestRule,
          TestUtils.newPasswordData(SOURCE_PASS, USER, null, sourceRefs),
          new String[] {String.format("Password cannot be the same as your %s password.", "System B"), },
        },
      };
  }
}

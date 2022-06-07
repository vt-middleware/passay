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

  /** For testing. */
  private final List<PasswordData.Reference> sourceRefs = new ArrayList<>();

  /** For testing. */
  private final DigestSourceRule digestRule = new DigestSourceRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));

  /** For testing. */
  private final DigestSourceRule emptyRule = new DigestSourceRule(
    new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA1"), 1, false));


  /** Initialize rules for this test. */
  @BeforeClass(groups = "passtest")
  public void createRules()
  {
    sourceRefs.add(new PasswordData.SourceReference("System B", "CJGTDMQRP+rmHApkcijC80aDV0o="));
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        {digestRule, new PasswordData("testuser", "t3stUs3r01", sourceRefs), null, },
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r04", sourceRefs),
          codes(SourceRule.ERROR_CODE),
        },
        {emptyRule, new PasswordData("testuser", "t3stUs3r01"), null, },
        {emptyRule, new PasswordData("testuser", "t3stUs3r04"), null, },
      };
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          digestRule,
          new PasswordData("testuser", "t3stUs3r04", sourceRefs),
          new String[] {String.format("Password cannot be the same as your %s password.", "System B"), },
        },
      };
  }
}

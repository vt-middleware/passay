/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.RuleResultMetadata;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link HaveIBeenPwnedRule}.
 *
 * @author Wolfgang Jung (post@wolfgang-jung.net)
 */
public class HaveIBeenPwnedRuleTest extends AbstractRuleTest
{

  /**
   * 'trustno1' is a common password, that is listed in the API.
   * echo -n 'trustno1' | shasum | tr '[:lower:]' '[:upper:]' => E68E11BE8B70E435C65AEF8BA9798FF7775C361E
   * echo -n 'trustno1' | shasum | cut -b1-5 | tr '[:lower:]' '[:upper:]' => E68E1
   * match from the API => 1BE8B70E435C65AEF8BA9798FF7775C361E
   */
  private static final String INVALID_PASSWORD = "trustno1";

  /** Password not found in the API (because we are mocking the data). */
  private static final String VALID_PASSWORD = "trustevery1";

  /** Password that is not mocked and results in an exception. */
  private static final String EXCEPTION_PASSWORD = "THROWS_IOEXCEPTION";

  /** Wiremock server. */
  private WireMockServer wireMockServer;

  /** API URL for testing. */
  private String apiUrl;

  /** Test rule. */
  private HaveIBeenPwnedRule defaultRule;

  /** Test rule. */
  private HaveIBeenPwnedRule allowExposedRule;

  /** Test rule. */
  private HaveIBeenPwnedRule allowOnExceptionRule;

  @BeforeTest(groups = "passtest")
  public void beforeTest()
  {
    wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
    wireMockServer.start();
    WireMock.configureFor("localhost", wireMockServer.port());
    apiUrl = "http://localhost:" + wireMockServer.port() + "/range/";
    // 'trustno1' API data
    WireMock.stubFor(
      WireMock
        .get(WireMock.urlEqualTo("/range/E68E1"))
        .willReturn(
          WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "text/plain")
            .withBody(
              "1A53B48D62A232833A22AB2E52E4E0DD996:1\n" +
              "1B0855C166AF5BB3ABD7F9C3B2D3672D24F:3\n" +
              "1B0A09BDD71D472FBF4CF3DB43AFC64A2A1:1\n" +
              "1BA5C680A9A4222B95AB51147CCDB1273B6:2\n" +
              "1BDE22104AB530DA76A3EBECE72A58FC5BE:6\n" +
              "1BE8B70E435C65AEF8BA9798FF7775C361E:351295\n" +
              "1C5E8919625639E8133CCDACBD5B3892B98:1\n" +
              "1C659110154B6E7CF09F90B57BCF22AE515:2\n" +
              "1E4E4B2C09CF46EFEA05A36617BC9654508:14\n" +
              "1E87340EF0036CBD8C694436488564B558F:1\n" +
              "1EE608C7988BEDC47F7FB2654D9FE6AE07A:3\n" +
              "1F2EDC4A4F8063BBCE2FF9BF1310B1E829C:3")
        )
    );
    // 'trustevery1' API data
    WireMock.stubFor(
      WireMock
        .get(WireMock.urlEqualTo("/range/98712"))
        .willReturn(
          WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "text/plain")
            .withBody(
              "0061939B65B0030C80CFD917E53EDA27DB8:10\n" +
              "0125D372731F262A582F49D9C1FD0B2013D:1\n" +
              "013D14A087271266A4177150E2D55A2074E:1\n" +
              "016B6D4E09D97E3DFCE2A5988EFC0B5FAA2:5\n" +
              "018C8BE0560280997B6603B8CECBC7DB78B:3\n" +
              "0198694E1F0F4DBAFE12758C408BF74553C:2\n" +
              "019D03F52A09D3B92420A2500B1760DD186:1\n" +
              "01B4CAEBDA5D08718B9817A522EC2E953C0:2\n" +
              "01F40E8D7316B3348A35645570F9ACCD71F:3\n" +
              "0228F893865765F84C623C80E216FE2F61A:1\n" +
              "022D6FD19F372E3E1EC6B8ADE429E63DF04:1")
        )
    );
    defaultRule = new HaveIBeenPwnedRule("org.passay", apiUrl);
    allowExposedRule = new HaveIBeenPwnedRule("org.passay", apiUrl, true, false);
    allowOnExceptionRule = new HaveIBeenPwnedRule("org.passay", apiUrl, false, true);
  }


  @AfterTest(groups = "passtest")
  public void tearDown()
  {
    wireMockServer.stop();
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {
        // Test valid password
        {
          defaultRule,
          new PasswordData(VALID_PASSWORD),
          null,
        },
        // Test invalid password, with allow exposed
        {
          allowExposedRule,
          new PasswordData(INVALID_PASSWORD),
          null,
        },
        // Test invalid password, with allow exception
        {
          allowOnExceptionRule,
          new PasswordData(EXCEPTION_PASSWORD),
          null,
        },
        // Test invalid password
        {
          defaultRule,
          new PasswordData(INVALID_PASSWORD),
          codes(HaveIBeenPwnedRule.ERROR_CODE),
        },
        // Test IO exception
        {
          defaultRule,
          new PasswordData(EXCEPTION_PASSWORD),
          codes(HaveIBeenPwnedRule.IO_ERROR_CODE),
        },
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
          defaultRule,
          new PasswordData(INVALID_PASSWORD),
          new String[] {
            String.format(
              "Password is exposed from previous leaks, seen %1$s times before. Consider this password unusable.",
              "351295"),
          },
        },
        {
          defaultRule,
          new PasswordData(EXCEPTION_PASSWORD),
          new String[] {
            String.format("Error communicating with %1$s", apiUrl),
          },
        },
      };
  }


  @Test(groups = "passtest")
  public void testInvalidPwdAllowExposed()
  {
    final HaveIBeenPwnedRule rule = new HaveIBeenPwnedRule("org.passay", apiUrl, true, false);
    final PasswordValidator validator = new PasswordValidator(rule);
    final RuleResult result = validator.validate(new PasswordData(INVALID_PASSWORD));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCount(RuleResultMetadata.CountCategory.Pwned)).isEqualTo(351295);
    assertThat(validator.getMessages(result).size()).isEqualTo(0);
  }


  @Test(groups = "passtest")
  public void testInvalidPwdAllowException()
  {
    final HaveIBeenPwnedRule rule = new HaveIBeenPwnedRule("org.passay", apiUrl, false, true);
    final PasswordValidator validator = new PasswordValidator(rule);
    final RuleResult result = validator.validate(new PasswordData(EXCEPTION_PASSWORD));
    assertThat(result.isValid()).isTrue();
    assertThat(result.getMetadata().getCounts().size()).isEqualTo(0);
    assertThat(validator.getMessages(result).size()).isEqualTo(0);
  }
}

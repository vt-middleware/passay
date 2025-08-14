/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.spring;

import java.util.ArrayList;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.rule.Rule;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for Spring integration.
 *
 * @author  Middleware Services
 */
public class SpringTest
{


  /**
   * Attempts to load all Spring application context XML files to verify proper wiring.
   */
  @Test(groups = "passtest")
  public void testSpringWiring()
  {
    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
      new String[] {"/spring-context.xml", });
    assertThat(context.getBeanDefinitionCount()).isGreaterThan(0);

    final PasswordValidator validator = new PasswordValidator(
      new ArrayList<>(context.getBeansOfType(Rule.class).values()));
    final PasswordData pd = new PasswordData("springuser", "springtest");
    final RuleResult result = validator.validate(pd);
    assertThat(result).isNotNull();
  }
}

/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Rule for determining if a password contains the desired mix of character types. In order to meet the criteria of this
 * rule, passwords must meet any number of supplied character rules.
 *
 * @author  Middleware Services
 */
public class CharacterCharacteristicsRule implements Rule
{

  /** Error code for insufficient number of characteristics. */
  public static final String ERROR_CODE = "INSUFFICIENT_CHARACTERISTICS";

  /** Rules to apply when checking a password. */
  private List<CharacterRule> rules = new ArrayList<>();

  /** Number of rules to enforce. Default value is 1. */
  private int numCharacteristics = 1;

  /** Whether to report the details of each character rule failure. */
  private boolean reportRuleFailures = true;


  /**
   * Returns the character rules used by this rule.
   *
   * @return  list of character rules
   */
  public List<CharacterRule> getRules()
  {
    return rules;
  }


  /**
   * Sets the character rules used by this rule.
   *
   * @param  l  list of rules
   */
  public void setRules(final List<CharacterRule> l)
  {
    rules = l;
  }


  /**
   * Sets the number of characteristics which must be satisfied in order for a password to meet the requirements of this
   * rule. The default is one. i.e. you may wish to enforce any three of five supplied character rules.
   *
   * @param  n  number of characteristics to enforce, where n &gt; 0
   */
  public void setNumberOfCharacteristics(final int n)
  {
    if (n > 0) {
      numCharacteristics = n;
    } else {
      throw new IllegalArgumentException("argument must be greater than zero");
    }
  }


  /**
   * Returns the number of characteristics which currently must be satisfied in order for a password to meet the
   * requirements of this rule.
   *
   * @return  number of characteristics to enforce
   */
  public int getNumberOfCharacteristics()
  {
    return numCharacteristics;
  }


  /**
   * Returns whether to add the rule result detail for each character rule that fails to validate to the rule result.
   *
   * @return  whether to add character rule result details
   */
  public boolean getReportRuleFailures()
  {
    return reportRuleFailures;
  }


  /**
   * Sets whether to add the rule result detail for each character rule that fails to validate to the rule result.
   *
   * @param  b  whether to add character rule result details
   */
  public void setReportRuleFailures(final boolean b)
  {
    reportRuleFailures = b;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    if (numCharacteristics > rules.size()) {
      throw new IllegalStateException("Number of characteristics must be <= to the number of rules");
    }

    int successCount = 0;
    final RuleResult result = new RuleResult(true);
    for (CharacterRule rule : rules) {
      final RuleResult rr = rule.validate(passwordData);
      if (!rr.isValid()) {
        if (reportRuleFailures) {
          result.getDetails().addAll(rr.getDetails());
        }
      } else {
        successCount++;
      }
    }
    if (successCount < numCharacteristics) {
      result.setValid(false);
      result.getDetails().add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(successCount)));
    }
    return result;
  }


  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  success  number of successful rules
   *
   * @return  map of parameter name to value
   */
  protected Map<String, Object> createRuleResultDetailParameters(final int success)
  {
    final Map<String, Object> m = new LinkedHashMap<>();
    m.put("successCount", success);
    m.put("minimumRequired", numCharacteristics);
    m.put("ruleCount", rules.size());
    return m;
  }


  @Override
  public String toString()
  {
    return
      String.format(
        "%s@%h::numberOfCharacteristics=%s,rules=%s",
        getClass().getName(),
        hashCode(),
        numCharacteristics,
        rules);
  }
}

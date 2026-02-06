/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;

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
  private final List<CharacterRule> rules = new ArrayList<>();

  /** Number of rules to enforce. Default value is 1. */
  private final int numCharacteristics;

  /** Whether to report the details of this rule failure. */
  private final boolean reportFailure;

  /** Whether to report the details of each character rule failure. */
  private final boolean reportRuleFailures;


  /**
   * Creates a new character characteristics rule.
   *
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(final CharacterRule... rules)
  {
    this(1, rules);
  }


  /**
   * Creates a new character characteristics rule.
   *
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(final List<CharacterRule> rules)
  {
    this(1, rules);
  }


  /**
   * Creates a new character characteristics rule.
   *
   * @param  numCharacteristics  number of characteristics to enforce, where n &gt; 0
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(final int numCharacteristics, final CharacterRule... rules)
  {
    this(true, true, numCharacteristics, Arrays.asList(rules));
  }


  /**
   * Creates a new character characteristics rule.
   *
   * @param  numCharacteristics  number of characteristics to enforce, where n &gt; 0
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(final int numCharacteristics, final List<CharacterRule> rules)
  {
    this(true, true, numCharacteristics, rules);
  }


  /**
   * Creates a new character characteristics rule.
   *
   * @param  reportFailure  whether to report failures
   * @param  reportRuleFailures  whether to report rule failures
   * @param  numCharacteristics  number of characteristics to enforce, where n &gt; 0
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(
    final boolean reportFailure,
    final boolean reportRuleFailures,
    final int numCharacteristics,
    final CharacterRule... rules)
  {
    this(reportFailure, reportRuleFailures, numCharacteristics, Arrays.asList(rules));
  }


  /**
   * Creates a new character characteristics rule.
   *
   * @param  reportFailure  whether to report failures
   * @param  reportRuleFailures  whether to report rule failures
   * @param  numCharacteristics  number of characteristics to enforce, where n &gt; 0
   * @param  rules  character rules to set
   */
  public CharacterCharacteristicsRule(
    final boolean reportFailure,
    final boolean reportRuleFailures,
    final int numCharacteristics,
    final List<CharacterRule> rules)
  {
    if (numCharacteristics <= 0) {
      throw new IllegalArgumentException("Number of characteristics must be greater than zero");
    }
    PassayUtils.assertNotNullArgOr(
      rules,
      v -> v.isEmpty() || v.stream().anyMatch(Objects::isNull),
      "Rules cannot be empty, null or contain null");
    this.reportFailure = reportFailure;
    this.reportRuleFailures = reportRuleFailures;
    this.numCharacteristics = numCharacteristics;
    this.rules.addAll(rules);
  }


  /**
   * Returns the character rules used by this rule.
   *
   * @return  list of character rules
   */
  public List<CharacterRule> getRules()
  {
    return Collections.unmodifiableList(rules);
  }


  /**
   * Returns whether to add the rule result detail of this rule to the rule result.
   *
   * @return  whether to add rule result detail of this rule
   */
  public boolean getReportFailure()
  {
    return reportFailure;
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


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    if (numCharacteristics > rules.size()) {
      throw new IllegalStateException("Number of characteristics must be <= to the number of rules");
    }

    int successCount = 0;
    final List<RuleResultDetail> details = new ArrayList<>();
    final List<RuleResultMetadata> metadata = new ArrayList<>();
    for (CharacterRule rule : rules) {
      final RuleResult rr = rule.validate(passwordData);
      if (rr.isValid()) {
        successCount++;
      }
      if (reportRuleFailures) {
        details.addAll(rr.getDetails());
      }
      metadata.add(rr.getMetadata());
    }
    final boolean valid = successCount >= numCharacteristics;
    if (!valid && reportFailure) {
      details.add(new RuleResultDetail(ERROR_CODE, createRuleResultDetailParameters(successCount)));
    }
    return valid ?
      new SuccessRuleResult(new RuleResultMetadata(metadata)) :
      new FailureRuleResult(new RuleResultMetadata(metadata), details);
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
    return getClass().getName() + "@" + hashCode() + "::" +
      "numCharacteristics=" + numCharacteristics + ", " +
      "rules=" + rules + ", " +
      "reportFailure=" + reportFailure + ", " +
      "reportRuleFailures=" + reportRuleFailures;
  }
}

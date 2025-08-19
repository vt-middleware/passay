/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.passay.entropy.Entropy;
import org.passay.entropy.RandomPasswordEntropyFactory;
import org.passay.entropy.ShannonEntropyFactory;
import org.passay.resolver.MessageResolver;
import org.passay.resolver.PropertiesMessageResolver;
import org.passay.rule.Rule;

/**
 * The central component for evaluating multiple password rules against a candidate password.
 *
 * @author  Middleware Services
 */
public class DefaultPasswordValidator implements PasswordValidator
{

  /**
   * Calculates the entropy of the given {@link PasswordData} based on the password rules specified. <em>It's important
   * to note that this function does NOT take into account {@link org.passay.rule.IllegalRegexRule} or
   * {@link org.passay.rule.AllowedRegexRule}</em> as the regular expressions driving the rules may be negative matches.
   * The supplied password data must have an origin of {@link PasswordData.Origin#Generated} or
   * {@link PasswordData.Origin#User}.
   *
   * @see <a href="http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf">PDF Publication</a>
   */
  public static final BiFunction<List<Rule>, PasswordData, Entropy> DEFAULT_ENTROPY_PROVIDER =
    new BiFunction<List<Rule>, PasswordData, Entropy>()
    {
      @Override
      public Entropy apply(final List<Rule> rules, final PasswordData passwordData)
      {
        if (passwordData.getOrigin().equals(PasswordData.Origin.Generated)) {
          return RandomPasswordEntropyFactory.createEntropy(rules, passwordData);
        } else if (passwordData.getOrigin().equals(PasswordData.Origin.User)) {
          return ShannonEntropyFactory.createEntropy(rules, passwordData);
        }
        throw new IllegalArgumentException("Unknown password origin: " + passwordData);
      }

      @Override
      public String toString()
      {
        return "DEFAULT_ENTROPY_PROVIDER";
      }
    };

  /** Message resolver that returns null for any rule result detail. */
  private static final MessageResolver NULL_MESSAGE_RESOLVER = new MessageResolver()
  {
    @Override
    public String resolve(final RuleResultDetail detail)
    {
      return null;
    }

    @Override
    public String toString()
    {
      return "NULL_MESSAGE_RESOLVER";
    }
  };

  /** Entropy provider that returns -1. */
  private static final BiFunction<List<Rule>, PasswordData, Entropy> NO_ENTROPY_PROVIDER =
    new BiFunction<List<Rule>, PasswordData, Entropy>()
    {
      @Override
      public Entropy apply(final List<Rule> rules, final PasswordData passwordData)
      {
        return () -> -1;
      }

      @Override
      public String toString()
      {
        return "NO_ENTROPY_PROVIDER";
      }
    };

  /** Password rules. */
  private final List<Rule> passwordRules = new ArrayList<>();

  /** Message resolver. */
  private final MessageResolver messageResolver;

  /** Entropy provider. */
  private final BiFunction<List<Rule>, PasswordData, Entropy> entropyProvider;


  /**
   * See {@link #DefaultPasswordValidator(List)}.
   *
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(final Rule... rules)
  {
    this(NULL_MESSAGE_RESOLVER, NO_ENTROPY_PROVIDER, Arrays.asList(rules));
  }


  /**
   * Creates a new password validator with a {@link PropertiesMessageResolver}.
   *
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(final List<? extends Rule> rules)
  {
    this(NULL_MESSAGE_RESOLVER, NO_ENTROPY_PROVIDER, rules);
  }


  /**
   * See {@link #DefaultPasswordValidator(MessageResolver, List)}.
   *
   * @param  resolver  message resolver.
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(final MessageResolver resolver, final Rule... rules)
  {
    this(resolver, NO_ENTROPY_PROVIDER, Arrays.asList(rules));
  }


  /**
   * Creates a new password validator.
   *
   * @param  resolver  message resolver.
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(final MessageResolver resolver, final List<? extends Rule> rules)
  {
    this(resolver, NO_ENTROPY_PROVIDER, rules);
  }


  /**
   * Creates a new password validator.
   *
   * @param  entropyProvider  to calculate entropy estimate
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(
    final BiFunction<List<Rule>, PasswordData, Entropy> entropyProvider, final Rule... rules)
  {
    this(NULL_MESSAGE_RESOLVER, entropyProvider, Arrays.asList(rules));
  }


  /**
   * Creates a new password validator.
   *
   * @param  entropyProvider  to calculate entropy estimate
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(
    final BiFunction<List<Rule>, PasswordData, Entropy> entropyProvider,
    final List<? extends Rule> rules)
  {
    this(NULL_MESSAGE_RESOLVER, entropyProvider, rules);
  }


  /**
   * Creates a new password validator.
   *
   * @param  resolver  message resolver.
   * @param  entropyProvider  to calculate entropy estimate
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(
    final MessageResolver resolver,
    final BiFunction<List<Rule>, PasswordData, Entropy> entropyProvider,
    final Rule... rules)
  {
    this(resolver, entropyProvider, Arrays.asList(rules));
  }


  /**
   * Creates a new password validator.
   *
   * @param  resolver  message resolver.
   * @param  entropyProvider  to calculate entropy estimate
   * @param  rules  to validate
   */
  public DefaultPasswordValidator(
    final MessageResolver resolver,
    final BiFunction<List<Rule>, PasswordData, Entropy> entropyProvider,
    final List<? extends Rule> rules)
  {
    this.messageResolver = PassayUtils.assertNotNullArg(resolver, "Message resolver cannot be null");
    this.entropyProvider = PassayUtils.assertNotNullArg(entropyProvider, "Entropy supplier cannot be null");
    this.passwordRules.addAll(
      PassayUtils.assertNotNullArgOr(
        rules,
        v -> v.stream().anyMatch(Objects::isNull),
        "Password rules cannot be null or contain null"));
  }


  @Override
  public List<? extends Rule> getRules()
  {
    return Collections.unmodifiableList(passwordRules);
  }


  @Override
  public MessageResolver getMessageResolver()
  {
    return messageResolver;
  }


  @Override
  public BiFunction<List<Rule>, PasswordData, Entropy> getEntropyProvider()
  {
    return entropyProvider;
  }


  @Override
  public ValidationResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    boolean success = true;
    final List<RuleResultDetail> details = new ArrayList<>();
    final List<RuleResultMetadata> metadata = new ArrayList<>();
    for (Rule rule : passwordRules) {
      final RuleResult result = rule.validate(passwordData);
      if (success && !result.isValid()) {
        success = false;
      }
      details.addAll(result.getDetails());
      metadata.add(result.getMetadata());
    }
    final double entropy = entropyProvider.apply(passwordRules, passwordData).estimate();
    final List<String> messages = success ?
      Collections.emptyList() :
      details.stream().map(messageResolver::resolve).filter(Objects::nonNull).collect(Collectors.toList());
    return success ?
      new SuccessValidationResult(entropy, new RuleResultMetadata(metadata)) :
      new FailureValidationResult(entropy, new RuleResultMetadata(metadata), details, messages);
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "passwordRules=" + passwordRules + ", " +
      "messageResolver=" + messageResolver + ", " +
      "entropyProvider=" + entropyProvider;
  }
}

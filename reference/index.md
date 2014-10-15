---
layout: default
title: Reference manual
---
The Passay API consists of 3 core components:

1. [`Rule`](javadocs/org/passay/Rule.html) - one or more rules define a password policy rule set
2. [`PasswordValidator`](javadocs/org/passay/PasswordValidator.html) - validates a password against a rule set
3. [`PasswordGenerator`](javadocs/org/passay/PasswordGenerator.html) - produces passwords that satisfy a given rule set

# Password validation
Password validation involves creating a `PasswordValidator` from a rule set, which is simply a `List` of `Rule` objects.
Consider the following simple password policy:

1. Length of 8 to 16 characters
2. Must contain at least one of the following: upper-case, lower-case, digit, and symbol
3. No whitespace characters

The following code excerpt constructs a validator that enforces the policy:

{% highlight java %}
PasswordValidator validator = new PasswordValidator(Arrays.asList(
  new LengthRule(8, 16),
  new UppercaseCharacterRule(1),
  new LowercaseCharacterRule(1),
  new DigitCharacterRule(1),
  new SpecialCharacterRule(1),
  new WhitespaceRule()));
{% endhighlight %}

Note that _special character_ has the meaning _symbol_:
{% raw %}
!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
{% endraw %}



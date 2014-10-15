---
layout: default
title: Reference manual
---
The Passay API consists of 3 core components:

1. [`Rule`](../javadocs/org/passay/Rule.html) - one or more rules define a password policy rule set
2. [`PasswordValidator`](../javadocs/org/passay/PasswordValidator.html) - validates a password against a rule set
3. [`PasswordGenerator`](../javadocs/org/passay/PasswordGenerator.html) - produces passwords that satisfy a given rule set

# Password validation
Password validation involves creating a `PasswordValidator` from a rule set, which is simply a list of `Rule` objects.
Consider the following simple password policy:

1. Length of 8 to 16 characters
2. Must contain at least one of the following: upper case, lower case, digit, and symbol
3. No whitespace characters

The following code excerpt constructs a validator that enforces the policy.

{% highlight java %}
PasswordValidator validator = new PasswordValidator(Arrays.asList(
  // length between 8 and 16 characters
  new LengthRule(8, 16),

  // at least one upper-case character
  new UppercaseCharacterRule(1),

  // at least one lower-case character
  new LowercaseCharacterRule(1),

  // at least one digit character
  new DigitCharacterRule(1),

  // at least one symbol (special character)
  new SpecialCharacterRule(1),

  // no whitespace
  new WhitespaceRule()));

final char[] password = System.console().readPassword("Password: ");
RuleResult result = validator.validate(new String(password));
if (result.isValid()) {
  System.out.println("Password is valid");
} else {
  System.out.println("Invalid password:");
  for (String msg : validator.getMessages(result)) {
    System.out.println(msg);
  }
}
{% endhighlight %}

## Advanced validation: M of N rules
Many password policies contain a rule of the form _password must contain at least M of the following N_.
The [`CharacterCharacteristicsRule`](../javadocs/org/passay/CharacterCharacteristicsRule.html) component supports
this use case. Consider the following policy:

1. Length of 8 to 16 characters
2. Must contain characters from at least 3 of the following: upper, lower, digit, symbol
3. No whitespace characters

This policy is implemented in the following code excerpt.

{% highlight java %}
LengthRule r1 = new LengthRule(8, 16);
CharacterCharacteristicsRule r2 = new CharacterCharacteristicsRule();
// Define M (3 in this case)
r2.setNumberOfCharacteristics(3);
// Define elements of N (upper, lower, digit, symbol)
r2.getRules().add(new UppercaseCharacterRule(1));
r2.getRules().add(new LowercaseCharacterRule(1));
r2.getRules().add(new DigitCharacterRule(1));
r2.getRules().add(new SpecialCharacterRule(1));
WhiteSpaceRule r3 = new WhiteSpaceRule();
PasswordValidator validator = new PasswordValidator(Arrays.asList(r1, r2, r3));
{% endhighlight %}

## Advanced validation: dictionary rules
Many password policies seek to prevent common words (e.g. _password_) from appearing in passwords. Passay ships with
two rules that can be used with arbitrary word lists to enforce dictionary policies:

1. [`DictionaryRule`](../javadocs/org/passay/DictionaryRule.html) - exact matching semantics
2. [`DictionarySubstringRule`](../javadocs/org/passay/DictionarySubstringRule.html) - contains matching semantics

`DictionarySubstringRule` should be used judiciously since a configuration with a sizeable common word list would
prevent strong passwords like [_correcthorsebatterystaple_](http://xkcd.com/936/) and _random.words@31415_.
A reasonable use case might be a relatively short list of reserved words forbidden to appear in passwords.

`DictionaryRule`, on the other hand, has a valuable common use case: preventing known weak passwords.
Configuring this rule with a published list of popular passwords, such as those from the
[Adobe breach](http://stricture-group.com/files/adobe-top100.txt), can dramatically increase password security by
preventing common, and therefore insecure, passwords.

{% highlight java %}
DictionaryRule rule = new DictionaryRule(
	new WordListDictionary(WordLists.createFromReader(
	  // Reader around the word list file
  	new FileReader[] {new FileReader("path/to/top100.txt")},
  	// True for case sensitivity, false otherwise
  	false,
  	// Dictionaries must be sorted
  	new ArraysSort())));
{% endhighlight %}

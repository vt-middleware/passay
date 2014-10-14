---
layout: default
title: Passay Reference Manual
---

# Reference manual

The Passay API consists of 3 core components:

1. [`Rule`](javadocs/org/passay/Rule.html) - one or more rules define a password policy rule set
2. [`PasswordValidator`](javadocs/org/passay/PasswordValidator.html) - validates a password against a rule set
3. [`PasswordGenerator`](javadocs/org/passay/PasswordGenerator.html) - produces passwords that satisfy a given rule set

## Password validation
Password validation involves creating a `PasswordValidator` from a rule set, which is simply a `List` of `Rule` objects.
The following constructs a validator that enforces the following password policy:

1. Passwords must be between 8 and 16 characters
2. Passwords must contain at least one upper-case letter, one lower-case letter, one digit, and one special character
3. Passwords may not contain whitespace
4. Passwords may not contain any trivial sequences of 3 or more characters (e.g. _asdf_, _abc_, _123_)
5. Passwords may not contain the same character repeated 3 or more times

    List<Rule> rules = Arrays.asList(
      new LengthRule(8, 16),
      new UppercaseCharacterRule(1),
      new LowercaseCharacterRule(1),
      new DigitCharacterRule(1));

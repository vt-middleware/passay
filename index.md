---
layout: default
title: Password policy enforcement for Java
---
Passay builds on the success of [vt-password](https://code.google.com/p/vt-middleware/wiki/vtpassword) and provides
a comprehensive and extensible feature set.

## Password validation
Enforce password policy by validating candidate passwords against a configurable rule set.
Passay provides a comprehensive set of rules for common cases and supports extension through a simple
[rule interface](javadocs/org/passay/Rule.html).

## Password generation
Generate passwords using a configurable rule set. The [password generator](javadocs/org/passay/PasswordGenerator.html)
is extensible like all Passay components.

## Command line tools
Automate password policy enforcement and support tooling scenarios using the command line interface.

# Using
Passay artifacts are available in Maven Central. If you would like to use this project in your maven build,
include the following in your pom.xml:

    <dependencies>
      <dependency>
          <groupId>org.passay</groupId>
          <artifactId>passay</artifactId>
          <version>1.0</version>
      </dependency>
    <dependencies>

# History
Passay is the descendant of the venerable [vt-password](https://code.google.com/p/vt-middleware/wiki/vtpassword) Java
library produced by the Middleware group at Virginia Tech. Passay builds on the lessons learned from vt-password,
which was [well-regarded](http://stackoverflow.com/questions/3200292/password-strength-checking-library) in its own
right. Passay is more convenient, more extensible, and ready for internationalization.

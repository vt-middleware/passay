---
layout: default
title: Passay
---
# Password validation and generation for Java

Features at a glance:

- PasswordValidator: password policy enforcement with comprehensive built-in rule set
- PasswordGenerator: password generation according to policy
- PasswordCli: command line tools
- Rule: simple interface to facilitate custom policies

See the [reference manual](reference/) for complete documentation and usage examples.

## Using
Passay artifacts are available in Maven Central. If you would like to use this project in your maven build,
include the following in your pom.xml:

    <dependencies>
      <dependency>
          <groupId>org.passay</groupId>
          <artifactId>passay</artifactId>
          <version>1.0</version>
      </dependency>
    <dependencies>

## History
Passay is the descendant of the venerable [vt-password](https://code.google.com/p/vt-middleware/wiki/vtpassword) Java
library produced by the Middleware group at Virginia Tech. Passay builds on the lessons learned from vt-password,
which was [well-regarded](http://stackoverflow.com/questions/3200292/password-strength-checking-library) in its own
right. Passay is more convenient, more extensible, and ready for internationalization.

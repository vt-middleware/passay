---
layout: default
title: Password policy enforcement for Java
---
Passay provides a comprehensive and extensible feature set for password validation and generation.

## Password validation
Enforce password policy by validating candidate passwords against a configurable rule set.
Passay provides a comprehensive set of rules for common cases and supports extension through a simple
[rule interface](javadocs/org/passay/rule/Rule.html).

## Password generation
Generate passwords using a configurable rule set. The [password generator](javadocs/org/passay/generate/PasswordGenerator.html)
is extensible like all Passay components.

# Using
Passay artifacts are available in Maven Central. If you would like to use this project in your maven build,
include the following in your pom.xml:

    <dependencies>
      <dependency>
          <groupId>org.passay</groupId>
          <artifactId>passay</artifactId>
          <version>{{ site.version }}</version>
      </dependency>
    </dependencies>


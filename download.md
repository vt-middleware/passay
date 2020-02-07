---
layout: default
title: Download Passay
---
Download the latest version which includes source code, classes, javadocs, and tests in zip or tar.gz format.

* [passay-{{ site.version }}-dist.tar.gz](/downloads/{{ site.version }}/passay-{{ site.version }}-dist.tar.gz)   [[PGP](downloads/{{ site.version }}/passay-{{ site.version }}-dist.tar.gz.asc)]
* [passay-{{ site.version }}-dist.zip](/downloads/{{ site.version }}/passay-{{ site.version }}-dist.zip)   [[PGP](downloads/{{ site.version }}/passay-{{ site.version }}-dist.tar.gz.asc)]

Individual artifacts are available in the [Maven Central Repository](http://repo1.maven.org/maven2/org/passay/passay).
If you would like to use this project in your maven build, include the following in your pom.xml:
{% highlight xml %}
<dependencies>
  <dependency>
    <groupId>org.passay</groupId>
    <artifactId>passay</artifactId>
    <version>{{ site.version }}</version>
  </dependency>
</dependencies>
{% endhighlight %}

## Release Notes

### Version 1.5.0 - 12Apr2019

Issue | Description
:---- | :----------
[passay-98]({{ site.issueURL }}98) | NoSuchMethodErrors using Buffer when compiling on JDK9+
[passay-94]({{ site.issueURL }}94) | Salted references
[passay-92]({{ site.issueURL }}92) | Add RepeatCharactersRule
[passay-91]({{ site.issueURL }}91) | Add CharacterOccurrencesRule
[passay-89]({{ site.issueURL }}89) | Simplify and optimize IllegalSequenceRule
[passay-84]({{ site.issueURL }}84),  [passay-85]({{ site.issueURL }}85), [passay-88]({{ site.issueURL }}88) | Ternary Tree improvements

### Version 1.4.0 - 09Jan2019

Issue | Description
:---- | :----------
[passay-80]({{ site.issueURL }}80) | Add BloomFilter dictionary
[passay-78]({{ site.issueURL }}78) | Add support for digested dictionaries
[passay-76]({{ site.issueURL }}76) | Support BCryptHashBean
[passay-72]({{ site.issueURL }}72) | BufferOverflowException for certain cachePercent and file sizes

### Version 1.3.1 - 22Feb2018

Issue | Description
:---- | :----------
[passay-71]({{ site.issueURL }}71) | Add RuleResultMetadata
[passay-68]({{ site.issueURL }}68) | Always return only one result for HistoryRule
[passay-67]({{ site.issueURL }}67) | JDBCDictionary size throws exception
[passay-64]({{ site.issueURL }}64) | Bug in PasswordData#newPasswordData
[passay-63]({{ site.issueURL }}63) | Support Spring MessageResolver
[passay-58]({{ site.issueURL }}58) | PasswordValidator should use PECS generic

### Version 1.3.0 - 18Jul2017 (Requires Java 8)

Note that cryptacular is now an optional dependency. If you use the DigestHistoryRule or DigestSourceRule, you must add an explicit dependency on org.cryptacular.

Issue | Description
:---- | :----------
[passay-55]({{ site.issueURL }}55) | Change PasswordValidator constructor to comply with PECS
[passay-47]({{ site.issueURL }}47) | Improve FileWordList to support large files
[passay-43]({{ site.issueURL }}43) | Variable ruleset based on password length
[passay-42]({{ site.issueURL }}42) | NPE check in UsernameRule#validate
[passay-41]({{ site.issueURL }}41) | Make cryptacular an optional dependency

### Version 1.2.0 - 11Aug2016 (Requires Java 8)

Issue | Description
:---- | :----------
[passay-32]({{ site.issueURL }}32) | Tilda character missing as a special character
[passay-18]({{ site.issueURL }}18) | Develop Entropy-Based Password Strength Evaluation

### Version 1.1.0 - 22Jul2015 (Requires Java 7)

Issue | Description
:---- | :----------
[passay-24]({{ site.issueURL }}24) | Add OSGi manifest entries
[passay-23]({{ site.issueURL }}23) | SequenceRule refactoring

### Version 1.0 - 01Dec2014 (Requires Java 6)
Initial Release


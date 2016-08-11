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

### Version 1.2.0 - 11Aug2016 (Requires Java 8)

Issue | Description
:---- | :----------
[passay-32]({{ site.issueURL }}32) | Tilda character missing as a special character
[passay-18]({{ site.issueURL }}18) | Develop Entropy-Based Password Strength Evaluation

### Version 1.1.0 - 22Jul2015 (Requires Java 7)

Issue | Description
:---- | :----------
[passay-24]({{ site.issueURL }}24) | Add OSGi manifest entries

### Version 1.0 - 01Dec2014 (Requires Java 6)
Initial Release


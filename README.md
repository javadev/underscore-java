underscore-java 
===============

[![Maven Central](https://img.shields.io/maven-central/v/com.github.javadev/underscore.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.javadev%22%20AND%20a%3A%22underscore%22)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/javadev/underscore-java/blob/master/LICENSE.txt)
[![Build Status](https://secure.travis-ci.org/javadev/underscore-java.svg)](https://travis-ci.org/javadev/underscore-java)
[![Coverage Status](https://coveralls.io/repos/javadev/underscore-java/badge.svg?branch=master)](https://coveralls.io/r/javadev/underscore-java)
[![codecov.io](http://codecov.io/github/javadev/underscore-java/coverage.svg?branch=master)](http://codecov.io/github/javadev/underscore-java?branch=master)
[![CircleCI](https://circleci.com/gh/javadev/underscore-java.svg?style=svg)](https://circleci.com/gh/javadev/underscore-java)
[![Codeship Status for javadev/underscore-java](https://codeship.com/projects/c989fef0-f3ab-0132-7ca5-16cf317d1634/status?branch=master)](https://codeship.com/projects/85467)
[![wercker status](https://app.wercker.com/status/d1130226089a5bd54d205e1901cbef3b/s "wercker status")](https://app.wercker.com/project/bykey/d1130226089a5bd54d205e1901cbef3b)
[![Build status](https://ci.appveyor.com/api/projects/status/tx7icv3i08qowv6r?svg=true)](https://ci.appveyor.com/project/javadev/underscore-java)
[![Build Status](https://semaphoreci.com/api/v1/projects/2fc09de1-52a9-4f88-8023-9da1223e64f6/532523/shields_badge.svg)](https://semaphoreci.com/javadev/underscore-java)
[![Run Status](https://api.shippable.com/projects/55def20f1895ca44741030a2/badge?branch=master)](https://app.shippable.com/projects/55def20f1895ca44741030a2)
[![Known Vulnerabilities](https://snyk.io/test/github/javadev/underscore-java/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/javadev/underscore-java?targetFile=pom.xml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6e30751dc17c452f8524ae7eff474ce1)](https://www.codacy.com/app/javadev75/underscore-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=javadev/underscore-java&amp;utm_campaign=Badge_Grade)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/javadev/underscore-java)
[![Code Scene](https://img.shields.io/badge/codescene-analyzed-brightgreen.svg)](https://codescene.io/projects/1173/jobs/latest-successful/results)
[![BCH compliance](https://bettercodehub.com/edge/badge/javadev/underscore-java?branch=master)](https://bettercodehub.com/)
[![](http://javadoc-badge.appspot.com/com.github.javadev/underscore.svg?label=JavaDocs)](http://www.javadoc.io/doc/com.github.javadev/underscore/)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=javadev_underscore-java&metric=alert_status)](https://sonarcloud.io/dashboard/index/javadev_underscore-java)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=javadev_underscore-java&metric=sqale_rating)](https://sonarcloud.io/dashboard/index/javadev_underscore-java)
[![Scrutinizer](https://img.shields.io/scrutinizer/g/javadev/underscore-java.svg)](https://scrutinizer-ci.com/g/javadev/underscore-java/)
[![Build Status](https://dev.azure.com/javadevazure/underscore-java/_apis/build/status/javadev.underscore-java)](https://dev.azure.com/javadevazure/underscore-java/_build/latest?definitionId=1)
[![Hits-of-Code](https://hitsofcode.com/github/javadev/underscore-java)](https://hitsofcode.com/view/github/javadev/underscore-java)
![Java Version](https://img.shields.io/badge/java-%3E%3D%201.6-success)

[![Join the chat at https://gitter.im/javadev/underscore-java](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/javadev/underscore-java?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Requirements
============

Java 1.6 and later, [Java 1.8](https://github.com/javadev/underscore-java8) or [Java 11](https://github.com/javadev/underscore-java11).


## Installation

Include the following in your `pom.xml` for Maven:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.javadev</groupId>
    <artifactId>underscore</artifactId>
    <version>1.56</version>
  </dependency>
  ...
</dependencies>
```

Gradle:

```groovy
compile 'com.github.javadev:underscore:1.56'
```

### Usage

```java
U.chain(/* array | list | set | map | anything based on Iterable interface */)
    .filter(..)
    .map(..)
    ...
    .sortWith()
    .forEach(..);
U.chain(value1, value2, value3)...
U.range(0, 10)...

U.chain(1, 2, 3) // or java.util.Arrays.asList(1, 2, 3) or new Integer[] {1, 2, 3}
    .filter(v -> v > 1)
    // 2, 3
    .map(v -> v + 1)
    // 3, 4
    .sortWith((a, b) -> b.compareTo(a))
    // 4, 3
    .forEach(System.out::println);
    // 4, 3
    
U.formatXml("<a><b>data</b></a>");
    // <a>
    //    <b>data</b>
    // </a>

U.formatJson("{\"a\":{\"b\":\"data\"}}");
    // {
    //    "a": {
    //      "b": "data"
    //    }
    // }

U.xmlToJson("<a><b>data</b></a>");
    // {
    //   "a": {
    //     "b": "data"
    //   },
    //   "#omit-xml-declaration": "yes"
    // }

U.jsonToXml("{\"a\":{\"b\":\"data\"}}");
    // <?xml version="1.0" encoding="UTF-8"?>
    // <a>
    //   <b>data</b>
    // </a>

Map<String, Object> value = U.objectBuilder()
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            .add("address", U.arrayBuilder()
                .add(U.objectBuilder()
                    .add("streetAddress", "21 2nd Street")
                    .add("city", "New York")
                    .add("state", "NY")
                    .add("postalCode", "10021")))
            .add("phoneNumber", U.arrayBuilder()
                .add(U.objectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(U.objectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567")))
            .build();
        // {firstName=John, lastName=Smith, age=25, address=[{streetAddress=21 2nd Street,
        // city=New York, state=NY, postalCode=10021}], phoneNumber=[{type=home, number=212 555-1234},
        // {type=fax, number=646 555-4567}]}
```

Underscore-java is a java port of [Underscore.js](http://underscorejs.org/).

In addition to porting Underscore's functionality, Underscore-java includes matching unit tests.

For docs, license, tests, and downloads, see:
http://javadev.github.io/underscore-java

Thanks to Jeremy Ashkenas and all contributors to Underscore.js.

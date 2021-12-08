underscore-java 
===============

[![Maven Central](https://img.shields.io/maven-central/v/com.github.javadev/underscore.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.javadev%22%20AND%20a%3A%22underscore%22)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/javadev/underscore-java/blob/main/LICENSE.txt)
[![Java CI](https://github.com/javadev/underscore-java/actions/workflows/maven.yml/badge.svg)](https://github.com/javadev/underscore-java/actions/workflows/maven.yml)
[![CodeQL](https://github.com/javadev/underscore-java/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/javadev/underscore-java/actions/workflows/codeql-analysis.yml)
[![Coverage Status](https://coveralls.io/repos/javadev/underscore-java/badge.svg?branch=main)](https://coveralls.io/r/javadev/underscore-java)
[![codecov](https://codecov.io/gh/javadev/underscore-java/branch/master/graph/badge.svg?token=IZXYx3kg5y)](https://codecov.io/gh/javadev/underscore-java)
[![CircleCI](https://circleci.com/gh/javadev/underscore-java.svg?style=svg)](https://circleci.com/gh/javadev/underscore-java)
[![Codeship Status for javadev/underscore-java](https://codeship.com/projects/c989fef0-f3ab-0132-7ca5-16cf317d1634/status?branch=main)](https://codeship.com/projects/85467)
[![Build status](https://ci.appveyor.com/api/projects/status/tx7icv3i08qowv6r?svg=true)](https://ci.appveyor.com/project/javadev/underscore-java)
[![Build Status](https://semaphoreci.com/api/v1/javadev/underscore-java/branches/main/badge.svg)](https://semaphoreci.com/javadev/underscore-java)
[![Known Vulnerabilities](https://snyk.io/test/github/javadev/underscore-java/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/javadev/underscore-java?targetFile=pom.xml)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/javadev/underscore-java)
[![Code Scene](https://img.shields.io/badge/codescene-analyzed-brightgreen.svg)](https://codescene.io/projects/1173/jobs/latest-successful/results)
[![javadoc](https://javadoc.io/badge2/com.github.javadev/underscore/javadoc.svg)](https://javadoc.io/doc/com.github.javadev/underscore)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=javadev_underscore-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=javadev_underscore-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=javadev_underscore-java&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=javadev_underscore-java)
[![Scrutinizer](https://img.shields.io/scrutinizer/g/javadev/underscore-java.svg)](https://scrutinizer-ci.com/g/javadev/underscore-java/)
[![Build Status](https://dev.azure.com/javadevazure/underscore-java/_apis/build/status/javadev.underscore-java?branchName=main)](https://dev.azure.com/javadevazure/underscore-java/_build/latest?definitionId=1&branchName=main)
[![Hits-of-Code](https://hitsofcode.com/github/javadev/underscore-java)](https://hitsofcode.com/view/github/javadev/underscore-java)
![Java Version](https://img.shields.io/badge/java-%3E%3D%201.8-success)

[![Join the chat at https://gitter.im/javadev/underscore-java](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/javadev/underscore-java?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Requirements
============

Java 1.8 and later or [Java 11](https://github.com/javadev/underscore-java11).


## Installation

Include the following in your `pom.xml` for Maven:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.javadev</groupId>
    <artifactId>underscore</artifactId>
    <version>1.72</version>
  </dependency>
  ...
</dependencies>
```

Gradle:

```groovy
implementation 'com.github.javadev:underscore:1.72'
```

### Usage

```java
U.of(/* array | list | set | map | anything based on Iterable interface */)
    .filter(..)
    .map(..)
    ...
    .sortWith()
    .forEach(..);
U.of(value1, value2, value3)...
U.range(0, 10)...

U.of(1, 2, 3) // or java.util.Arrays.asList(1, 2, 3) or new Integer[] {1, 2, 3}
    .filter(v -> v > 1)
    // 2, 3
    .map(v -> v + 1)
    // 3, 4
    .sortWith((a, b) -> b.compareTo(a))
    .forEach(System.out::println);
    // 4, 3
    
U.of(1, 2, 3) // or java.util.Arrays.asList(1, 2, 3) or new Integer[] {1, 2, 3}
    .mapMulti((num, consumer) -> {
        for (int i = 0; i < num; i++) {
            consumer.accept("a" + num);
        }
    })
    .forEach(System.out::println);
    // "a1", "a2", "a2", "a3", "a3", "a3"
    
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

U.xmlToJson("<a attr=\"c\"><b>data</b></a>");
    // {
    //   "a": {
    //     "-attr": "c",
    //     "b": "data"
    //   },
    //   "#omit-xml-declaration": "yes"
    // }

U.jsonToXml("{\"a\":{\"-attr\":\"c\",\"b\":\"data\"}}");
    // <?xml version="1.0" encoding="UTF-8"?>
    // <a attr="c">
    //   <b>data</b>
    // </a>

U.Builder builder = U.objectBuilder()
    .add("firstName", "John")
    .add("lastName", "Smith")
    .add("age", 25)
    .add("address", U.arrayBuilder()
        .add(U.objectBuilder()
            .add("streetAddress", "21 2nd Street")
            .add("city", "New York")
            .addNull("cityId")
            .add("state", "NY")
            .add("postalCode", "10021")))
    .add("phoneNumber", U.arrayBuilder()
        .add(U.objectBuilder()
            .add("type", "home")
            .add("number", "212 555-1234"))
        .add(U.objectBuilder()
            .add("type", "fax")
            .add("number", "646 555-4567")));
System.out.println(builder.toJson());
System.out.println(builder.toXml());
```
```javascript
{
  "firstName": "John",
  "lastName": "Smith",
  "age": 25,
  "address": [
    {
      "streetAddress": "21 2nd Street",
      "city": "New York",
      "cityId": null,
      "state": "NY",
      "postalCode": "10021"
    }
  ],
  "phoneNumber": [
    {
      "type": "home",
      "number": "212 555-1234"
    },
    {
      "type": "fax",
      "number": "646 555-4567"
    }
  ]
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<root>
  <firstName>John</firstName>
  <lastName>Smith</lastName>
  <age number="true">25</age>
  <address array="true">
    <streetAddress>21 2nd Street</streetAddress>
    <city>New York</city>
    <cityId null="true"/>
    <state>NY</state>
    <postalCode>10021</postalCode>
  </address>
  <phoneNumber>
    <type>home</type>
    <number>212 555-1234</number>
  </phoneNumber>
  <phoneNumber>
    <type>fax</type>
    <number>646 555-4567</number>
  </phoneNumber>
</root>
```

Underscore-java is a java port of [Underscore.js](https://underscorejs.org/).

In addition to porting Underscore's functionality, Underscore-java includes matching unit tests.

For docs, license, tests, and downloads, see:
https://javadev.github.io/underscore-java

Thanks to Jeremy Ashkenas and all contributors to Underscore.js.

sbt-javacpp4s: A sbt plugin using JavaCPP for Scala
============================

[![Build Status](https://travis-ci.org/y-yu/sbt-javacpp4s.svg?branch=master)](https://travis-ci.org/y-yu/sbt-javacpp4s)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.y-yu/sbt-javacpp4s/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.y-yu/sbt-javacpp4s)

A sbt plugin to to use [JavaCPP](https://github.com/bytedeco/javacpp) from Scala.

## How to Use

1. `./project/plugins.sbt`

    ```
    addSbtPlugin(com.github.y-yu" % "sbt-javacpp4s" % "0.1.4")
    ```
    - if you want to use a SNAPSHOT version, you write following.
        ```
        addSbtPlugin(com.github.y-yu" % "sbt-javacpp4s" % "0.1.4-SNAPSHOT")
        ```
    
2. `build.sbt`

    ```
    enablePlugins(SbtJavaCPP4S)
    ```

See also `./example` and `./src/sbt-test` directories.

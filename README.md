sbt-javacpp4s: A sbt plugin using JavaCPP for Scala
============================

[![Build Status](https://travis-ci.org/y-yu/sbt-javacpp4s.svg?branch=master)](https://travis-ci.org/y-yu/sbt-javacpp4s)

## How to Use

1. `./project/plugins.sbt`

    ```
    addSbtPlugin("com.github.y-yu" % "sbt-javacpp4s" % "0.1.0")
    ```
    - if you want to use a SNAPSHOT version, you write following.
        ```
        addSbtPlugin("com.github.y-yu" % "sbt-javacpp4s" % "0.1.0-SNAPSHOT")
        ```
    
2. `build.sbt`

    ```
    enablePlugins(SbtJavaCPP4S)
    ```

See also `./example` and `./src/sbt-test` directories.
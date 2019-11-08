import javacpp4s.DynamicLibraryMeta

scalaVersion := "2.12.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

currentLibraryMeta := DynamicLibraryMeta.Linux

libraryName := "libTest"

includePath := (baseDirectory in Compile).value

nativeJavaClassPath := ""

makeLibraryCommands := Nil

test := (test in Test).value

enablePlugins(SbtJavaCPP4S)
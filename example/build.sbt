import javacpp4s.SbtJavaCPP4S._

scalaVersion := "2.12.10"

cppSourcePath := (baseDirectory in Compile).value / "cpp_src" / "HelloWorld.cpp"

includePath := (baseDirectory in Compile).value / "cpp_src"

libraryName := "libHelloWorld"

nativeJavaClassPath := "javacpp.sbt.*"

enablePlugins(SbtJavaCPP4S)
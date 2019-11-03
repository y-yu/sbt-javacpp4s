scalaVersion := "2.12.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

cppSourcePath := (baseDirectory in Compile).value / "cpp_src" / "ReturnInput.cpp"

includePath := (baseDirectory in Compile).value / "cpp_src"

libraryName := "libReturnInput"

nativeJavaClassPath := "returninput.ReturnInput"

enablePlugins(SbtJavaCPP4S)

InputKey[Unit]("checkFileInlibjni") := {
  val log = streams.value.log
  val args = sbt.complete.Parsers.spaceDelimited("<arg>").parsed
  val expectFileNameRegex = args.head.r
  assert((target.value / "libjni" * "*").get.exists { file =>
    expectFileNameRegex.findFirstIn(file.getName).isDefined
  })
}
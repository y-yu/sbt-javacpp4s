scalaVersion := "2.12.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

includePath := (baseDirectory in Compile).value / "cpp_src"

libraryName := "libReturnInput"

nativeJavaClassPath := "returninput.ReturnInput"

makeLibraryCommands := Seq(
  gppCompilerPath.value,
  "-I", includePath.value.toString,
  currentLibraryMeta.value.option,
  "-o",
  (libraryDestinationPath.value / s"${libraryName.value}.${currentLibraryMeta.value.extension}").toString,
  ((baseDirectory in Compile).value / "cpp_src" / "ReturnInput.cpp").toString
)

enablePlugins(SbtJavaCPP4S)

InputKey[Unit]("checkFileInlibjni") := {
  val log = streams.value.log
  val args = sbt.complete.Parsers.spaceDelimited("<arg>").parsed
  val expectFileNameRegex = args.head.r
  assert((target.value / "libjni" * "*").get.exists { file =>
    expectFileNameRegex.findFirstIn(file.getName).isDefined
  })
}
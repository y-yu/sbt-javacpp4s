import javacpp4s.SbtJavaCPP4S._

includePath := (baseDirectory in Compile).value / "cpp_src"

libraryName := "libHelloWorld"

makeLibraryCommands := Seq(
  gppCompilerPath.value,
  "-I", includePath.value.toString,
  currentLibraryMeta.value.option,
  "-o",
  (libraryDestinationPath.value / s"${libraryName.value}.${currentLibraryMeta.value.extension}").toString,
  ((baseDirectory in Compile).value / "cpp_src" / "HelloWorld.cpp").toString
)

nativeJavaClassPath := "javacpp.sbt.*"

enablePlugins(SbtJavaCPP4S)
package javacpp4s

import sbt.Keys._
import sbt._
import scala.sys.process.ProcessLogger
import scala.sys.process._
import DynamicLibraryMeta.createDynamicLibraryMetaTask

/**
  * Generate JNI dynamic link libraries.
  */
object SbtJavaCPP4S extends AutoPlugin {
  object autoImport {
    lazy val gppCompilerPath = settingKey[String](
      "g++ runtime path. (default: clang++)"
    )

    lazy val makeLibraryCommands = settingKey[Seq[String]](
      "Commands to build the library used by Java."
    )

    lazy val includePath = settingKey[File](
      "The directory in which C++ header files are."
    )

    lazy val libraryName = settingKey[String](
      "The library name without its extension created by C++ source code."
    )

    lazy val libraryDestinationPath = settingKey[File](
      "The output directory for built dynamic libraries."
    )

    lazy val nativeJavaClassPath = settingKey[String](
      "Native implemented Java class path."
    )

    lazy val currentLibraryMeta = settingKey[DynamicLibraryMeta](
      "Extension and g++ option of the environment."
    )

    lazy val generateJNILibrary =
      TaskKey[Unit](
        "generateJNILibrary",
        "Make JNI Library files."
      )
  }

  override def trigger: PluginTrigger = noTrigger

  override val projectSettings: Seq[Setting[_]] = {
    import autoImport._

    val libraryMeta = if (System.getProperty("os.name").contains("Mac"))
      DynamicLibraryMeta.Mac
    else
      DynamicLibraryMeta.Linux

    Seq(
      gppCompilerPath := "clang++",
      libraryDestinationPath := (target in Compile).value / "libjni",
      generateJNILibrary in Compile :=
        generateJNILibraryTask.dependsOn(compile in Compile).value,
      currentLibraryMeta := libraryMeta,
      fork := true,
      fork in Test := true,
      javaOptions ++= Seq(
        s"-Djava.library.path=${libraryDestinationPath.value.toString}",
        s"-Dplatform.linkpath=${libraryDestinationPath.value.toString}"
      ),
      libraryDependencies += "org.bytedeco" % "javacpp" % "1.5.1",
      sourceGenerators in Compile += createDynamicLibraryMetaTask.taskValue,
      run := (run in Runtime).dependsOn(generateJNILibrary in Compile).evaluated,
      test := (test in Test).dependsOn(generateJNILibrary in Compile).value
    )
  }

  private def generateJNILibraryTask: Def.Initialize[Task[Unit]] = Def.task {
    val log = streams.value.log
    val gppCompilerPath = (autoImport.gppCompilerPath).value
    val includePath = (autoImport.includePath).value
    val destinationPath = (autoImport.libraryDestinationPath).value
    val nativeJavaClassPath = (autoImport.nativeJavaClassPath).value
    val makeLibraryCommands = (autoImport.makeLibraryCommands).value

    val processLogger = ProcessLogger(
      log.info(_),
      log.error(_)
    )

    try {
      IO.createDirectory(destinationPath)

      // Make a base dynamic link library.
      if (makeLibraryCommands.mkString(" ") ! processLogger == 0) {
        log.info("Success!")
      } else {
        sys.error("Fail to make the library!")
      }

      // It's a poor method.... I want to fix this if it's possible.
      val javaCPPClassPath = (dependencyClasspath in Compile).value.files.filter { f =>
        f.getPath.contains("javacpp")
      }

      // Make a JNI library.
      (runner in Compile).value.run(
        mainClass = "org.bytedeco.javacpp.tools.Builder",
        classpath = javaCPPClassPath,
        options = Seq(
          s"-cp", (fullClasspath in Compile).value.files.mkString(":"),
          s"-Dplatform.compiler=$gppCompilerPath",
          s"-Dplatform.includepath=${includePath.toString}",
          s"-Dplatform.linkpath=${destinationPath.toString}",
          "-d", destinationPath.toString,
          nativeJavaClassPath
        ),
        log = log
      )
    } catch {
      case e: Exception =>
        e.printStackTrace()
        throw e
    }
  }
}

package javacpp4s

import sbt.Keys._
import sbt._
import scala.sys.process.ProcessLogger
import scala.sys.process._

/**
  * Generate JNI dynamic link libraries.
  */
object SbtJavaCPP4S extends AutoPlugin {
  object autoImport {
    lazy val gppPath = settingKey[String](
      "g++ runtime path. (default: clang++)"
    )

    lazy val cppSourcePath = settingKey[File](
      "A directory in which C++ source code files are."
    )

    lazy val includePath = settingKey[File](
      "A directory in which C++ header files are."
    )

    lazy val libraryName = settingKey[String](
      "The library name created by C++ source code."
    )

    lazy val libraryDestinationPath = settingKey[File](
      "An output directory for built dynamic libraries."
    )

    lazy val nativeJavaClassPath = settingKey[String](
      "Native implemented Java class path."
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

    Seq(
      gppPath := "clang++",
      libraryDestinationPath := (target in Compile).value / "libjni",
      generateJNILibrary in Compile :=
        generateJNILibraryTask.dependsOn(compile in Compile).value,
      fork := true,
      fork in Test := true,
      javaOptions ++= Seq(
        s"-Djava.library.path=${libraryDestinationPath.value.toString}",
        s"-Dplatform.linkpath=${libraryDestinationPath.value.toString}"
      ),
      libraryDependencies += "org.bytedeco" % "javacpp" % "1.5.1",
      run := (run in Runtime).dependsOn(generateJNILibrary in Compile).evaluated,
      test := (test in Test).dependsOn(generateJNILibrary in Compile).value
    )
  }

  sealed abstract class DynamicLibraryMeta(
    val option: String,
    val extension: String
  )

  object DynamicLibraryMeta {
    case object Mac extends DynamicLibraryMeta("-dynamiclib", "dylib")
    case object Linux extends DynamicLibraryMeta("-shared", "so")
  }

  private def generateJNILibraryTask: Def.Initialize[Task[Unit]] = Def.task {
    val log = streams.value.log
    val includePath = (autoImport.includePath in Compile).value
    val cppSourcePath = (autoImport.cppSourcePath in Compile).value
    val destinationPath = (autoImport.libraryDestinationPath in Compile).value
    val libraryName = (autoImport.libraryName in Compile).value
    val nativeJavaClassPath = (autoImport.nativeJavaClassPath in Compile).value
    val gppPath = autoImport.gppPath.value
    val processLogger = ProcessLogger(
      log.info(_),
      log.error(_)
    )
    val currentLibraryMeta: DynamicLibraryMeta =
      if (System.getProperty("os.name").contains("Mac"))
        DynamicLibraryMeta.Mac
      else
        DynamicLibraryMeta.Linux

    val makeLibCommand = Seq(
      gppPath,
      "-I", includePath,
      currentLibraryMeta.option,
      "-o", destinationPath / s"$libraryName.${currentLibraryMeta.extension}",
      cppSourcePath.toString
    ).mkString(" ")

    try {
      IO.createDirectory(destinationPath)

      // Make a base dynamic link library.
      if (makeLibCommand ! processLogger == 0) {
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
          s"-Dplatform.compiler=$gppPath",
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

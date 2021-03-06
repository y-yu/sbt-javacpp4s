package javacpp4s

import sbt._
import Keys._

sealed abstract class DynamicLibraryMeta(
  val option: String,
  val extension: String
)

object DynamicLibraryMeta {
  case object Mac extends DynamicLibraryMeta("-dynamiclib", "dylib")
  case object Linux extends DynamicLibraryMeta("-shared", "so")

  lazy val createDynamicLibraryMetaTask: Def.Initialize[Task[Seq[File]]] =
    Def.task {
      import SbtJavaCPP4S.autoImport._

      val output = (sourceManaged in Compile).value / "javacpp4s" / "DynamicLibraryMeta.scala"
      val nameOfSetting: String = currentLibraryMeta.value match {
        case Mac => "Mac"
        case Linux => "Linux"
      }
      val libraryNameString = libraryName.value

      val outputBody =
        s"""/**
           |  * This file was generated by javacpp4s.
           |  * So you must NOT edit this file directly.
           |  */
           |
           |package javacpp4s
           |
           |sealed abstract class DynamicLibraryMeta(
           |  val option: String,
           |  val extension: String
           |)
           |
           |object DynamicLibraryMeta {
           |  case object Mac extends DynamicLibraryMeta("-dynamiclib", "dylib")
           |  case object Linux extends DynamicLibraryMeta("-shared", "so")
           |
           |  val currentLibraryMeta: DynamicLibraryMeta = $nameOfSetting
           |
           |  val libraryName: String = "$libraryNameString"
           |}
           |""".stripMargin

      IO.write(output, outputBody)
      Seq(output)
    }
}

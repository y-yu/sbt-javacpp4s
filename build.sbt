import sbt.Keys._
import sbt._
import ReleaseTransformations._
import UpdateReadme.updateReadme

lazy val root = (project in file("."))
  .settings(
    sbtPlugin := true,
    scalaVersion := "2.12.10",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-Xlint",
      "-language:implicitConversions", "-language:higherKinds", "-language:existentials",
      "-unchecked"
    ),
    organization := "com.github.y-yu",
    name := "sbt-javacpp4s",
    description := "A sbt plugin using JavaCPP for Scala",
    homepage := Some(url("https://github.com/y-yu")),
    licenses := Seq("MIT" -> url(s"https://github.com/y-yu/sbt-javacpp4s/blob/master/LICENSE")),
    libraryDependencies += "org.bytedeco" % "javacpp" % "1.5.1",
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )
  .settings(publishSettings)
  .enablePlugins(SbtPlugin)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  ),
  publishArtifact in Test := false,
  pomExtra :=
    <developers>
      <developer>
        <id>y-yu</id>
        <name>Hikaru Yoshimura</name>
        <url>https://github.com/y-yu</url>
      </developer>
    </developers>
      <scm>
        <url>git@github.com:y-yu/sbt-javacpp4s.git</url>
        <connection>scm:git:git@github.com:y-yu/sbt-javacpp4s.git</connection>
        <tag>{tagOrHash.value}</tag>
      </scm>,
  releaseTagName := tagName.value,
  releaseCrossBuild := true,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    releaseStepCommandAndRemaining("^ scripted"),
    setReleaseVersion,
    updateReadme,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("^ publishSigned"),
    setNextVersion,
    commitNextVersion,
    updateReadme,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  )
)

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}

val tagOrHash = Def.setting {
  if (isSnapshot.value) sys.process.Process("git rev-parse HEAD").lineStream_!.head
  else tagName.value
}
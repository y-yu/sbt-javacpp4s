lazy val root = Project("plugins", file(".")) dependsOn sbtJavaCPP4S

lazy val sbtJavaCPP4S = ClasspathDependency(RootProject(file("..").getAbsoluteFile.toURI), None)
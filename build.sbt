lazy val scala212 = "2.12.10"
lazy val scala211 = "2.11.12"
lazy val scala213 = "2.13.3"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)

organization := "com.github.thesortedchaos"
scalaVersion := scala213
publishMavenStyle := true

lazy val root = (project in file("."))
  .settings(
    name := "wavefront-reader",
    crossScalaVersions := supportedScalaVersions
  )

libraryDependencies ++= Seq(
  // Mathematics
  "org.joml" % "joml" % "1.9.25",
  //Logging
  "ch.qos.logback"       % "logback-classic" % "1.2.3",
  "ch.qos.logback"       % "logback-core"    % "1.2.3",
  "org.fusesource.jansi" % "jansi"           % "1.18",
  "org.slf4j"            % "slf4j-api"       % "1.7.30",
  // Testing
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

addCommandAlias(
  "build-complete",
  "; scalafmtSbtCheck ; scalafmtCheck ; scalastyle ; compile ; test ; coverageReport"
)

// use a separate scalasytle config for tests
(scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml"

// code coverage
coverageEnabled := true
coverageMinimum := 80
coverageFailOnMinimum := true

homepage := Some(url("https://github.com/TheSortedChaos/wavefront-reader"))
licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ =>
  false
}
publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "content/repositories/snapshots")
  } else {
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}
scmInfo := Some(
  ScmInfo(
    url("https://github.com/TheSortedChaos/wavefront-reader"),
    "scm:git:git@github.com:TheSortedChaos/wavefront-reader.git"
  )
)
developers := List(
  Developer(
    "SortedChaos",
    "Marco Wittig",
    "the.sorted.chaos@gmail.com",
    url("https://github.com/TheSortedChaos/wavefront-reader")
  )
)

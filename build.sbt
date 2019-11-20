ThisBuild / organization := "org.sorted.chaos"
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "wavefront-reader"
  )

libraryDependencies ++= Seq(
  //Logging
  "ch.qos.logback"       % "logback-classic" % "1.3.0-alpha5",
  "ch.qos.logback"       % "logback-core"    % "1.3.0-alpha5",
  "org.fusesource.jansi" % "jansi"           % "1.18",
  "org.slf4j"            % "slf4j-api"       % "2.0.0-alpha1",
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

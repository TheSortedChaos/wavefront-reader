ThisBuild / organization := "org.sorted.chaos"
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "wavefront-reader"
  )

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

addCommandAlias(
  "build-complete",
  "; scalafmtSbtCheck ; scalafmtCheck ; scalastyle ; compile ; test"
)

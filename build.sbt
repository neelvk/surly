name := """surly"""
organization := "com.terway"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += jdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += jdbc % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.terway.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.terway.binders._"

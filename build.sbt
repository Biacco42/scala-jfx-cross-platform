/**
 * This file is part of scala-jfx-cross-platform.
 *
 * Copyright (c) 2020 Biacco42
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

import java.nio.file.Paths

import sbt._

ThisBuild / scalaVersion      := "2.13.1"
ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / organization      := "info.biacco42"
ThisBuild / organizationName  := "biacco42"
ThisBuild / scalacOptions     ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8")
run / fork                    := true
run / javaOptions             += "-Djava.library.path=lib"

// Release Task
lazy val release = taskKey[Unit]("Publishes release package")
lazy val javaHome = Paths.get(sys.env("JAVA_HOME"))

// For JavaFX platform dependency
lazy val targetPlatform = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

lazy val jreModulePath = Paths.get(javaHome.toString, "jmods")

lazy val javaFXModuleIdentifiers = Seq("base", "controls", "graphics", "fxml", "media", "swing", "web").map { m =>
  "org.openjfx" % s"javafx-$m" % "14"
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = javaFXModuleIdentifiers.map { id =>
  id classifier targetPlatform
}

lazy val root = (project in file("."))
  .settings(
    name := "scala-jfx-crossplatform",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    libraryDependencies ++= javaFXModules,
    release := {
      assembly.value
      ReleaseTask.buildReleasePackage(
        name.value, version.value, scalaVersion.value,
        targetPlatform, target.value.toPath,
        baseDirectory.value.toPath.resolve("lib"), baseDirectory.value.toPath.resolve("release"),
        jreModulePath, javaHome, targetPlatform
      )
    }
  )

// For sbt-assembly avoiding module system
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF", xs @ _*) => MergeStrategy.discard
  case "module-info.class" => MergeStrategy.discard
  case x => MergeStrategy.deduplicate
}


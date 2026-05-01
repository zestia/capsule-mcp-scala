name := "capsule-mcp-scala"

ThisBuild / scalaVersion := "3.8.3"
ThisBuild / javacOptions ++= Seq("--release", "17")
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / organization := "com.zestia"
ThisBuild / organizationName := "Zestia Ltd"
ThisBuild / startYear := Some(2026)
ThisBuild / licenses += (
  "Apache-2.0",
  url("https://www.apache.org/licenses/LICENSE-2.0.txt")
)

lazy val root = (project in file("."))
  .enablePlugins(ParadoxSitePlugin, GhpagesPlugin)
  .settings(
    name := "capsule-mcp-scala",
    scalacOptions ++= Seq("-experimental", "-Xmax-inlines:128", "-Wunused:imports"),
    libraryDependencies ++= Seq(
      // MCP Scala SDK
      "com.tjclp" %% "fast-mcp-scala" % "0.3.1",
      "tools.jackson.core" % "jackson-databind" % "3.0.3",

      // ZIO Core
      "dev.zio" %% "zio" % "2.1.22",
      "dev.zio" %% "zio-json" % "0.7.45",

      // HTTP Client
      "com.softwaremill.sttp.client3" %% "core" % "3.11.0",
      "com.softwaremill.sttp.client3" %% "zio-json" % "3.11.0",

      // Testing
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "com.networknt" % "json-schema-validator" % "2.0.0" % Test,
      "io.circe" %% "circe-parser" % "0.14.15" % Test,
      "dev.zio" %% "zio-test" % "2.1.22" % Test,
      "dev.zio" %% "zio-test-sbt" % "2.1.22" % Test
    ),
    git.remoteRepo := "git@github.com:zestia/capsule-mcp-scala.git"
  )

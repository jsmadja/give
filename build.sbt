name := """give"""

version := "1.0-SNAPSHOT"


scalaVersion := "2.11.1"

val appDependencies = Seq(
  "com.feth" %% "play-authenticate" % "0.6.8",
  "mysql" % "mysql-connector-java" % "5.1.17",
  javaCore,
  cache,
  javaWs,
  javaEbean
)

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    libraryDependencies ++= appDependencies
  )

fork in run := true
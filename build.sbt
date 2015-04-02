name := "give"

version := "1.0"

lazy val `give` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.feth" %% "play-authenticate" % "0.6.8",
  "mysql" % "mysql-connector-java" % "5.1.17",
  javaCore,
  javaJdbc,
  javaEbean,
  cache,
  javaWs)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
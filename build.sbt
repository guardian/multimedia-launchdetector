name := "multimedia-launchdetector"

version := "0.1"

val circeVersion = "0.8.0"

scalaVersion := "2.12.3"

// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
libraryDependencies ++= Seq(
  //"com.amazonaws" % "aws-java-sdk" % "1.11.207",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val app = (project in file(".")).settings(
  organization := "com.theguardian",
  assemblyJarName in assembly := "first_test_lambda.jar"
)
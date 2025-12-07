name := "functional-exercises"
version := "1.0"
scalaVersion := "2.13.12"

libraryDependencies += "com.lihaoyi" %% "utest" % "0.8.1" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint"
)
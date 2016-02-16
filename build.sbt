import ReleaseTransformations._

name := "memsub-common-play-auth"

organization := "com.gu"

scalaVersion := "2.11.7"

crossScalaVersions := Seq(scalaVersion.value)

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/memsub-common-play-auth"),
  "scm:git:git@github.com:guardian/memsub-common-play-auth.git"
))

description := "Common authentication code used by Membership & Subscriptions with Play framework"

licenses := Seq("Apache V2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

updateOptions := updateOptions.value.withCachedResolution(true)

resolvers += "Guardian Github Releases" at "http://guardian.github.io/maven/repo-releases"

libraryDependencies ++= Seq(
  "com.gu.identity" %% "identity-play-auth" % "0.14",
  "com.gu" %% "identity-test-users" % "0.5",
  "com.gu" %% "play-googleauth" % "0.3.5",
  "com.typesafe.play" %% "filters-helpers" % "2.4.6",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.10.50",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

lazy val root = project in file(".")

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)

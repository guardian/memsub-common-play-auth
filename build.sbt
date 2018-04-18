import ReleaseTransformations._

name := "memsub-common-play-auth"

organization := "com.gu"

scalaVersion := "2.11.12"

crossScalaVersions := Seq(scalaVersion.value)

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/memsub-common-play-auth"),
  "scm:git:git@github.com:guardian/memsub-common-play-auth.git"
))

description := "Common authentication code used by Membership & Subscriptions with Play framework"

licenses := Seq("Apache V2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

updateOptions := updateOptions.value.withCachedResolution(true)

resolvers ++= Seq(
  "Guardian Github Releases" at "http://guardian.github.io/maven/repo-releases",
  Resolver.sonatypeRepo("releases")
)


libraryDependencies ++= Seq(
  "com.gu.identity" %% "identity-play-auth" % "1.3",
  "com.gu" %% "identity-test-users" % "0.6",
  "com.gu" %% "play-googleauth" % "0.6.0",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.313",
  "org.scala-lang" % "scala-compiler" % "2.11.12",
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

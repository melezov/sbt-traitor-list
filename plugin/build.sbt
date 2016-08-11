sbtPlugin := true

name := "sbt-traitor-list"

scalaVersion := "2.10.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
//  "-Xfatal-warnings",
  "-Xlint",
  "-Xno-forwarders",
  "-Xverify",
  "-Yclosure-elim",
  "-Ydead-code",
  "-Yinline-warnings",
  "-Yinline",
  "-Yrepl-sync",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen"
)

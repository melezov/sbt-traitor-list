name := "traitor-list"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "com.softwaremill.scalamacrodebug" %% "macros" % "0.4"

scalaVersion := "2.11.8"
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
  "-Yconst-opt",
  "-Ydead-code",
  "-Yinline-warnings",
  "-Yinline",
  "-Yrepl-sync",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused"
)

name := "traitor-list"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

scalaVersion := "2.12.10"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:-unused,_",
)

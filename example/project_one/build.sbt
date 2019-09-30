version := "1.1.1"

scalaVersion := "2.12.10"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:-unused,_",
)

enablePlugins(TraitorListPlugin) 

traitorListTraitors := Seq("com.dslplatform.example.Foo")

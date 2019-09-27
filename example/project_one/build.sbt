scalaVersion := "2.12.10"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:-unused,_",
)

traitorListSettings

scalacOptions ++= Seq(
  "-P:traitor-list:output:example.txt",
  "-P:traitor-list:traitors:com.dslplatform.example.Foo;com.dslplatform.example.Zoo"
)

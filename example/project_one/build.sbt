scalaVersion := "2.11.8"

traitorListSettings

scalacOptions ++= Seq(
  "-P:traitor-list:output:example.txt",
  "-P:traitor-list:traitors:com.dslplatform.example.Foo;com.dslplatform.example.Zoo"
)

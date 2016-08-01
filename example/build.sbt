scalaVersion := "2.11.8"

libraryDependencies += "net.revenj" %% "revenj-core" % "0.1.1-SNAPSHOT"

traitorListSettings

scalacOptions ++= Seq(
  "-P:traitor-list:output:example.txt",
  "-P:traitor-list:traitors:net.revenj.patterns.DomainEventHandler"
)

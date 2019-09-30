package com.dslplatform.traitorlist

import sbt._
import Keys._

object TraitorListPlugin extends sbt.AutoPlugin {
  object autoImport {
    val traitorListTraitors = settingKey[Seq[String]]("List of traitors")
    val traitorListOutput = settingKey[File]("Sets the output directory where services will be dumped")

    lazy val traitorListSettings: Seq[Def.Setting[_]] = Seq(
      libraryDependencies += compilerPlugin("com.dslplatform.traitorlist" %% "traitor-list" % (version in ThisBuild).value),
      traitorListTraitors := Seq(
        "net.revenj.extensibility.SystemAspect",
        "net.revenj.patterns.AggregateDomainEventHandler",
        "net.revenj.patterns.DomainEventHandler",
        "net.revenj.patterns.EventStoreAspect",
        "net.revenj.patterns.PersistableRepositoryAspect",
        "net.revenj.patterns.ReportAspect",
        "net.revenj.server.ServerCommand",
        "net.revenj.server.handlers.RequestBinding",
      ),
      traitorListOutput := (Compile / resourceDirectory).value / "META-INF" / "services",
      scalacOptions ++= Seq(
        s"-P:traitor-list:traitors:${traitorListTraitors.value.mkString(";")}",
        s"-P:traitor-list:output:${traitorListOutput.value}",
      ),
    )
  }

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    autoImport.traitorListSettings
}

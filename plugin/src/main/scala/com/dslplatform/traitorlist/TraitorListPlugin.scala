package com.dslplatform.traitorlist

import sbt._

object TraitorListPlugin extends sbt.AutoPlugin {
  object autoImport {
    lazy val traitorListSettings: Seq[Def.Setting[_]] = Seq(
      addCompilerPlugin("com.dslplatform.traitorlist" %% "traitor-list" % "0.1.0")
    )
  }

  override lazy val projectSettings: Seq[Def.Setting[_]] = autoImport.traitorListSettings
}

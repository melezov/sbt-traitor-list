package com.dslplatform.traitorlist

import java.io.FileOutputStream
import scala.collection.mutable.{LinkedHashSet => MSet}
import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.plugins.Plugin
import scala.tools.nsc.plugins.PluginComponent

class TraitorList(val global: Global) extends Plugin {
  override val name = "traitor-list"
  override val components = List[PluginComponent](TraitorSeeker)
  override val description = "Enumerates classes extending specified traitors"

  private[this] var traitors = MSet.empty[String]
  private[this] var outputPath = "traitor-list.txt"

  override val optionsHelp = Some(
    s"""  -P:traitor-list:traitors:<traitors>  Comma separated list of traitors
       |  -P:traitor-list:output:<path>        Sets the output file where classes extending traitors will be listed (default: $outputPath)""".stripMargin)

  override def init(options: List[String], error: String => Unit): Boolean =
    options forall { option =>
      if (option.startsWith("traitors:")) {
        traitors ++= option.substring("traitors:".length) split ","
        true
      } else if (option.startsWith("output:")) {
        outputPath = option.substring("output:".length)
        true
      } else {
        error("Unsupported option: " + option)
        false
      }
    }

  private[this] object TraitorSeeker extends PluginComponent {
    override val runsAfter = List("typer")
    override val runsBefore = List("patmat")
    override val phaseName = "traitor-seeker"

    val global = TraitorList.this.global
    import global._

    private[this] val HorribleConstructorPattern = """(?s).*?def <init>\(\): (.*?) = \{.*"""r

    override def newPhase(prev: Phase): Phase = new StdPhase(prev) {
      override def apply(unit: global.CompilationUnit): Unit = {
        val sb = new StringBuilder
        unit.body foreach {
          case tree @ q"""${Modifiers(NoFlags, typeNames.EMPTY, Nil)} class $clazz[..$_] $_(...$_) extends { ..$early } with ..$traits { $_ => ..$_ }""" =>
            traits foreach { current =>
              current.tpe.baseClasses foreach { base =>
                if (traitors(base.fullName)) {
                  // horrible hack to extract fully qualified class name
                  val HorribleConstructorPattern(fullClass) = tree.toString
                  sb ++= s"""${unit.source};${base.fullName};${current};${fullClass}\n"""
                }
              }
            }
          case _ =>
        }

        val fos = new FileOutputStream(outputPath)
        try {
          fos.write(sb.toString.getBytes("UTF-8"))
        } finally {
          fos.close()
        }
      }
    }
  }
}

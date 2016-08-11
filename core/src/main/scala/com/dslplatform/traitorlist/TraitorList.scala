package com.dslplatform.traitorlist

import java.io.FileOutputStream

import scala.collection.mutable.{LinkedHashMap => MMap, LinkedHashSet => MSet}
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
    s"""  -P:traitor-list:traitors:<traitors>  Semicolon separated list of traitors
       |  -P:traitor-list:output:<path>        Sets the output file where classes extending traitors will be listed (default: $outputPath)""".stripMargin)

  override def init(options: List[String], error: String => Unit): Boolean =
    options forall { option =>
      if (option.startsWith("traitors:")) {
        traitors ++= option.substring("traitors:".length) split ";"
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

    // sourceFile -> ( signature -> classes )
    private[this] val results = MMap.empty[String, MMap[String, MSet[String]]]

    override def newPhase(prev: Phase): Phase = new StdPhase(prev) {
      override def run(): Unit = {
        results.clear()
        super.run()
        dump()
      }

      override def apply(unit: global.CompilationUnit): Unit = {
        val currentSource = MMap.empty[String, MSet[String]]
        results(unit.source.path) = currentSource

        unit.body foreach {
          // match only public classes
          case global.ClassDef(mods, simpleName, _, global.Template(parents, _, body))
            if mods.isPublic && (!mods.isTrait && !mods.isInterface) =>

            // couldn't find a way to neatly resolve the full class name of the class, so reading it from <init> return type
            lazy val className = (body collect {
              case global.DefDef(_, global.TermName("<init>"), _, _, tpt, _) => tpt.tpe.toString
            }).toSet.ensuring(_.size == 1, "Could not resolve fully-qualified classname from <init> return type").head

            // if fully qualified base name matches one of traitors provided in plugin options
            for (current <- parents; base <- current.tpe.baseClasses if traitors(base.fullName)) {
              // resolves inheritance; TODO: find a way to dealias type parameters
              val signature = current.tpe.baseType(base).toString
              currentSource.getOrElseUpdate(signature, MSet.empty[String]) += className
            }

          case _ =>
        }
      }

      private[this] def dump(): Unit = {
        val sb = new StringBuilder
        for ((sourceFile, traits) <- results) {
          if (traits.isEmpty) {
            sb ++= sourceFile ++= ":\n"
          } else {
            for ((signature, classes) <- traits; clazz <- classes) {
              sb ++= sourceFile += ':' ++= signature += ':' ++= clazz += '\n'
            }
          }
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

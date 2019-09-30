package com.dslplatform.traitorlist

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, Path}
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Global, Phase}

class TraitorList(val global: Global) extends Plugin {
  override val name: String = "traitor-list"
  override val components: List[PluginComponent] = List(TraitorSeeker)
  override val description: String = "Enumerates classes extending specified traitors"

  private[this] var traitors = mutable.Set.empty[String]
  private[this] var outputPath = Option.empty[String]

  override val optionsHelp = Some(
    s"""  -P:traitor-list:traitors:<traitors>  Semicolon separated list of traitors
       |  -P:traitor-list:output:<path>        Sets the output directory where services will be dumped""".stripMargin)

  override def init(options: List[String], error: String => Unit): Boolean = {
    outputPath = None
    val parse = options forall { option =>
      if (option.startsWith("traitors:")) {
        traitors ++= option.substring("traitors:".length) split ";"
        true
      } else if (option.startsWith("output:")) {
        outputPath = Some(option.substring("output:".length))
        true
      } else {
        error("Unsupported option: " + option)
        false
      }
    }
    if (parse && outputPath.isEmpty) {
      error("Traitor list output directory was not set!")
      false
    } else {
      parse
    }
  }

  private[this] object TraitorSeeker extends PluginComponent {
    override val runsAfter = List("typer")
    override val runsBefore = List("patmat")
    override val phaseName = "traitor-seeker"

    val global: Global = TraitorList.this.global

    // sourceFile -> ( signature -> classes )
    private[this] val results = mutable.Map.empty[String, (String, mutable.TreeSet[String])]

    override def newPhase(prev: Phase): Phase = new StdPhase(prev) {
      override def run(): Unit = {
        results.clear()
        super.run()
        dump()
      }

      override def apply(unit: global.CompilationUnit): Unit = unit.body foreach {
        // match only public classes
        case tree @ global.ClassDef(mods, simpleName, _, global.Template(parents, _, body))
          if mods.isPublic && (!mods.isTrait && !mods.isInterface) =>

          // couldn't find a way to neatly resolve the full class name of the class, so reading it from <init> return type
          lazy val className = (body collect {
            case global.DefDef(_, global.TermName("<init>"), _, _, tpt, _) => {
              val ts = tpt.tpe.typeSymbol
              if (ts.owner.hasPackageFlag) {
                ts.fullName
              } else {
                ts.owner.fullName + "$" + ts.name
              }
            }
          }).toSet.ensuring(_.size == 1, "Could not resolve fully-qualified classname from <init> return type").head

          // if fully qualified base name matches one of traitors provided in plugin options
          for (current <- parents; base <- current.tpe.baseClasses if traitors(base.fullName)) {
            val scalaType = current.tpe.baseType(base).toString
            // resolves inheritance; TODO: find a way to dealias type parameters
            val javaType = current.tpe.baseType(base).toString
              .replaceFirst("Array\\[([^]]+?)\\]", "$1%5B%5D") // TODO: smarter pattern
              .replace("[", "%3C")
              .replace("]", "%3E")

            val uniqueMark = "__UNESCAPE__" + scala.util.Random.nextLong()
            val unescape = javaType.replace("%", uniqueMark)
            val fileCompatible = URLEncoder.encode(unescape, UTF_8).replace(uniqueMark, "%")
            if (javaType != fileCompatible) {
              global.reporter.error(tree.pos, s"Fancy signature types are not yet supported: $scalaType")
            }

            val (_, classes) = results.getOrElseUpdate(fileCompatible, (scalaType, mutable.TreeSet.empty))
            classes += className
          }

        case _ =>
      }

      private[this] def dump(): Unit = {
        for ((signature, (scalaType, classes)) <- results) {
          val path = Path.of(outputPath.get, signature)
          val newBody = classes.mkString("", "\n", "").getBytes(UTF_8)
          val diff = if (path.toFile.exists) {
            val oldClasses = mutable.TreeSet.empty[String] ++ Files.readAllLines(path, UTF_8).asScala
            (oldClasses -- classes) ++ (classes -- oldClasses)
          } else {
            classes
          }
          if (diff.nonEmpty) {
            for (clazz <- diff) {
              println(s"Updated '$scalaType' services for '$clazz'")
            }
            Files.write(path, newBody)
          }
        }
      }
    }
  }
}

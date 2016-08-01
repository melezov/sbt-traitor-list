package foobar

import net.revenj.patterns._
import scala.concurrent.Future

class Foo extends DomainEvent {
  def createdAt: java.time.OffsetDateTime = java.time.OffsetDateTime.now
  def processedAt: Option[java.time.OffsetDateTime]  = None
  def URI: String = "FOOURI"
}

private class FooHandler extends DomainEventHandler[Foo] {
  override def handle(t: Foo) = Future.successful(println("HANDLING FOO FROM FOOHANDLER"))
}

object Kekec {
  class Boo extends DomainEvent {
  def createdAt: java.time.OffsetDateTime = (java.time.OffsetDateTime.now)
  def processedAt: Option[java.time.OffsetDateTime]  = None
    def URI: String = "BARURI"
  }

  class BooHandler extends DomainEventHandler[Seq[Boo]] {
    override def handle(t: Seq[Boo]) = Future.successful(println("HANDLING BOO FROM BOOHANDLER"))
  }

  type DiEm = DomainEvent
  type DiEmEjch[T] = DomainEventHandler[T]

  class Bar extends DiEm {
  def createdAt: java.time.OffsetDateTime = (java.time.OffsetDateTime.now)
  def processedAt: Option[java.time.OffsetDateTime]  = None
    def URI: String = "BARURI"
  }
  type Bar3000 = Bar
  class BarHandler extends DiEmEjch[() => Bar3000] {
    override def handle(t: () => Bar) = Future.successful(println("HANDLING BAR3000 FROM BARANDLER"))

  }
}

trait MaxHandler[T <: DomainEvent] extends DomainEventHandler[T] {
  override def handle(t: T) = Future.successful(println("no dle for max"))
}

class PeroHandler extends MaxHandler[Foo]

object Main extends App {
  println("Hello from sbt-traitor-list-example!")

  import Kekec._

  println(new FooHandler().handle(new Foo()))
  println(new BooHandler().handle(Seq(new Boo())))
  println(new BarHandler().handle(() => new Bar3000()))
  println(new PeroHandler().handle(new Foo()))
}

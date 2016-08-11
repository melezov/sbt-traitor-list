package com.dslplatform.example
package footraits

trait FooString extends Foo[String]
trait FooInt extends Foo[Int]

class Bar
trait FooBar extends Foo[Bar]
trait FooArrayBar extends Foo[Array[Bar]]
trait FooSeqBar extends Foo[Seq[Bar]]
trait FooOptionBar extends Foo[Option[Bar]]

trait FooUnitBar extends Foo[() => Bar]
trait FooUnitBarImpl extends FooUnitBar {
  def handle(t: () => Bar): Unit = t()
}

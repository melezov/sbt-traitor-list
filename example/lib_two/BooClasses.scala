package com.dslplatform.example
package booclasses

class BooString extends Foo[String] { def handle(t: String) = println("BooString: " + t) }
class BooInt extends Foo[Int] { def handle(t: Int) = println("BooInt: " + t) }

class Bar
class BooBar extends Foo[Bar] { def handle(t: Bar) = println("BooBar: " + t) }
class BooArrayBar extends Foo[Array[Bar]] { def handle(t: Array[Bar]) = println("BooArrayBar: " + t) }
class BooSeqBar extends Foo[Seq[Bar]] { def handle(t: Seq[Bar]) = println("BooSeqBar: " + t) }
class BooOptionBar extends Foo[Option[Bar]] { def handle(t: Option[Bar]) = println("BooOptionBar: " + t) }

class BooUnitBar extends Foo[() => Bar] { def handle(t: () => Bar) = println("BooUnitBar: " + t()) }

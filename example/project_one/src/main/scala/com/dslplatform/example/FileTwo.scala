package com.dslplatform.example
package variationspackage {
  class FooHandlerVariationsPackage(x: Int) extends Foo[Unit] {
    def this() =
      this(666)

    def handle(t: Unit): Unit = t
  }
}

package object variationspackageobject {
  class FooHandlerVariationsPackageObject private() extends Foo[Unit] {
    def handle(t: Unit): Unit = t
  }
}

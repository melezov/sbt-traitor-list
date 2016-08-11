package com.dslplatform.example

/** Visibility tests */
package visibilitytestsscala {
  class PublicFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t}
  private class PrivateFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
  protected class ProtectedFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
  private[visibilitytestsscala] class PackagePrivateFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
  protected[visibilitytestsscala] class PackageProtectedFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
  private[this] class ThisPrivateFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
  protected[this] class ThisProtectedFooHandler extends Foo[Unit] { def handle(t: Unit): Unit = t }
}

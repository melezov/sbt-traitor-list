package com.dslplatform.example
package complex

trait Azerty
trait Xyzzy[T]
trait Qweqwe[T]
trait Qwerty[T]

abstract class AbstractClass extends Foo[Int]

object DH {
  type UnitToOptionalBigDecimal = () => Option[BigDecimal]
  type Boo = Foo[UnitToOptionalBigDecimal]
}

class `DH Azerty` extends {
  val a = 1000
  val b = java.util.UUID.randomUUID.toString
} with Azerty
  with Xyzzy[Long => Int]
  with Qweqwe[Seq[() => Long]]
  with DH.Boo
  with Zoo
  with Qwerty[Map[Set[_], (List[Double], Seq[Array[_]])]]{
  def handle(t: DH.UnitToOptionalBigDecimal) = {}
}

class FooUnitBarImpl extends footraits.FooUnitBarImpl

object XYZ {
  type X_Foo[T] = Foo[T]
  trait Y_Foo[T] extends X_Foo[T]
  type Z_Foo[T] = Y_Foo[T]

  type UUID = java.util.UUID
  type XYZ_UUID = UUID
}

class GoDeeper extends XYZ.Z_Foo[XYZ.XYZ_UUID] {
  def handle(t: XYZ.XYZ_UUID) = t
}

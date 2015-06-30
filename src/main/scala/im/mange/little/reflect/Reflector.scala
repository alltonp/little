package im.mange.little.reflect

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

//TODO: possibly useful later - http://stackoverflow.com/questions/26500539/scala-getting-field-and-type-of-field-of-a-case-class
object Reflector {
  import scala.reflect.internal.Symbols
  import scala.reflect.runtime.universe._

  //TIP: borrowed from: http://stackoverflow.com/questions/16079113/scala-2-10-reflection-how-do-i-extract-the-field-values-from-a-case-class
  def orderedParamsToTypes[T: TypeTag]: ListMap[String, String] = {
    val tpe = typeOf[T]
    val constructorSymbol = tpe.decl(termNames.CONSTRUCTOR)

    val defaultConstructor =
      if (constructorSymbol.isMethod) constructorSymbol.asMethod
      else {
        val ctors = constructorSymbol.asTerm.alternatives
        ctors.map { _.asMethod }.find { _.isPrimaryConstructor }.get
      }

    ListMap[String, String]() ++ defaultConstructor.paramLists.reduceLeft(_ ++ _).map {
      sym => sym.name.toString -> tpe.member(sym.name).asMethod.returnType.toString
    }
  }

  //TIP: borrowed from: http://stackoverflow.com/questions/1226555/case-class-to-map-in-scala
  def orderedParamsToValues(cc: AnyRef): ListMap[String, Any] =
    (ListMap[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
      f.setAccessible(true)
      a + (f.getName -> interrogate(f.get(cc)))
    }

  //TIP: borrowed from: http://stackoverflow.com/questions/12078366/can-i-get-a-compile-time-list-of-all-of-the-case-objects-which-derive-from-a-sea
  def sealedDescendants[Root: TypeTag]: Set[Class[_]] = {
    val symbol = typeOf[Root].typeSymbol
    val internal = symbol.asInstanceOf[Symbols#Symbol]
    val children = if (internal.isSealed) internal.children.map(_.asInstanceOf[Symbol]) - symbol else Set.empty
    children.map(c ⇒ Class.forName(c.fullName))
  }

  @tailrec
  private def interrogate(untyped: Any): Any = untyped match {
    case Some(thing) => interrogate(thing)
    case other => other
  }
}

package im.mange.little.classy

import scala.reflect.ClassTag

object Classy {
  def name[T : ClassTag]: String = name(runtimeClass[T])

  def runtimeClass[T : ClassTag]: Class[_] = implicitly[ClassTag[T]].runtimeClass

  private def name(clazz: Class[_]): String = clazz.getSimpleName.stripSuffix("$")
}

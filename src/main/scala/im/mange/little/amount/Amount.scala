package im.mange.little.amount

import java.text.DecimalFormat

import im.mange.little.percentage.Percentage

object Amount {
  val Zero = Amount("0")
  val MinusOne = Amount("-1")

  implicit object AmountNumeric extends Numeric[Amount] with AmountOrdering with AmountNum
}

trait AmountOrdering extends scala.math.Ordering[Amount] {
  def compare(x: Amount, y: Amount): Int = x.internal compare y.internal
}

trait AmountNum extends scala.math.Numeric[Amount] {
  override def plus(x: Amount, y: Amount): Amount = x + y
  override def minus(x: Amount, y: Amount): Amount = x - y
  override def times(x: Amount, y: Amount): Amount = x * y
  override def negate(x: Amount): Amount = x * Amount.MinusOne
  override def fromInt(x: Int): Amount = Amount(x.toString)
  override def toInt(x: Amount): Int = x.internal.toInt
  override def toLong(x: Amount): Long = x.internal.toLong
  override def toFloat(x: Amount): Float = x.internal.toFloat
  override def toDouble(x: Amount): Double = x.internal.toDouble
}

case class Amount(private val value: String) {
  //TODO: this should probably be in AmountFactory and have options for strict = true
  private val trimmed = if (value == null) "" else value.trim
  private[amount] val internal = BigDecimal(if (trimmed.isEmpty) "0" else trimmed)

  def +(that: Amount)     = Amount((internal + that.internal).toString())
  def -(that: Amount)     = Amount((internal - that.internal).toString())
  def *(that: Amount)     = Amount((internal * that.internal).toString())
  def *(that: Int)        = Amount((internal * that).toString())
  def *(that: Long)       = Amount((internal * that).toString())
  def *(that: Percentage) = Amount((internal * that.internal).toString())
  def /(that: Amount)     = Amount((internal / that.internal).toString())

  def >(that: Amount)  = internal > that.internal
  def <(that: Amount)  = internal < that.internal
  def >=(that: Amount) = internal >= that.internal
  def <=(that: Amount) = internal <= that.internal

  def format(formatter: DecimalFormat) = formatter.format(internal)
  def abs = Amount(internal.abs.toString())
  def underlyingValue = internal.toString()
  def isZero = this.internal.equals(Amount.Zero.internal)
  def nonZero = !isZero
}

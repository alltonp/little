package im.mange.little.amount

import java.text.DecimalFormat

import im.mange.little.percentage.Percentage

object Amount {
  val Zero = Amount("0")
  val MinusOne = Amount("-1")
}

case class Amount(private val value: String) {
  //TODO: this should probably be in AmountFactory and have options for strict = true
  private val trimmed = if (value == null) "" else value.trim
  private val internal = BigDecimal(if (trimmed.isEmpty) "0" else trimmed)

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
  def isZero = this.equals(Amount.Zero)
  def nonZero = !this.equals(Amount.Zero)
}

package im.mange.little.percentage

import java.text.NumberFormat

object Percentage {
  def fromDecimalFraction(value: String) = Percentage((BigDecimal(value) * 100).toString())
  def fromPercentage(value: String) = Percentage(value.replace("\\%", ""))
}

case class Percentage private(private val value: String) {
  private val trimmed = value.trim
  private[little] val internal = BigDecimal(if (trimmed.isEmpty) "0" else trimmed) / 100

  def format(formatter: NumberFormat) = formatter.format(internal)
  def underlyingValue = internal.toString()
}

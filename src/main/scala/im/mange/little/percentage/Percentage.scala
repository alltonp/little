package im.mange.little.percentage

import java.text.NumberFormat

case class Percentage(private val value: String) {
  private val trimmed = value.trim
  private[little] val internal = BigDecimal(if (trimmed.isEmpty) "0" else trimmed) / 100

  def format(formatter: NumberFormat) = formatter.format(internal)
  def underlyingValue = internal.toString()
}

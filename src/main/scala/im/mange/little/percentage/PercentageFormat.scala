package im.mange.little.percentage

import java.text.NumberFormat

object PercentageFormat {
  val pct = NumberFormat.getPercentInstance
  pct.setMinimumFractionDigits(0)
  pct.setMaximumFractionDigits(2)
}

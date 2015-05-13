package im.mange.little.amount

import java.text.DecimalFormat

object AmountFormats {
  val `0dp` = new DecimalFormat("#,##0")
  val `1dp` = new DecimalFormat("#,##0.0")
  val `2dp` = new DecimalFormat("#,##0.00")
  val `4dp` = new DecimalFormat("#,##0.0000")
}

package im.mange.little.percentage

import org.scalatest.{MustMatchers, WordSpec}

class PercentageSpec extends WordSpec with MustMatchers {

  "can round trip from percentage" in {
    val expected = Percentage.fromPercentage("105.00")
    val to = Percentage.fromDecimalFraction("1.05")
    expected mustBe to
  }
}
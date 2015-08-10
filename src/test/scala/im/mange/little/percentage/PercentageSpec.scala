package im.mange.little.percentage

import org.scalatest.{MustMatchers, WordSpec}

class PercentageSpec extends WordSpec with MustMatchers {

  "can round trip" in {
    val from = Percentage("12345.67890")
    val to = Percentage(from.underlyingValue)
    from mustBe to
  }
}
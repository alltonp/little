package im.mange.little.amount

import org.joda.time.DateTimeConstants._
import org.scalatest.{MustMatchers, WordSpec}

class AmountSpec extends WordSpec with MustMatchers {

  "is zero" in {
    Amount("0").isZero mustBe true
    Amount("0.0").isZero mustBe true
    Amount("00.0").isZero mustBe true
    Amount("000.00").isZero mustBe true
    Amount("0.000").isZero mustBe true
    Amount("1").isZero mustBe false
    Amount("0.1").isZero mustBe false
  }

  "non zero" in {
    Amount("1").nonZero mustBe true
    Amount("1.0").nonZero mustBe true
    Amount("10.0").nonZero mustBe true
    Amount("100.00").nonZero mustBe true
    Amount("0.001").nonZero mustBe true
    Amount("0").nonZero mustBe false
    Amount("0.0").nonZero mustBe false
  }

  "Numeric instance" in {
    Seq(Amount("1"), Amount("2"), Amount("3")).sum mustBe Amount("6")
  }

  "can round trip" in {
    val from = Amount("12345.67890")
    val to = Amount(from.underlyingValue)
    from mustBe to
  }
}
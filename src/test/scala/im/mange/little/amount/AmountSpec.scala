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
  }

}
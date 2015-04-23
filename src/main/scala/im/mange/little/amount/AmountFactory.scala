package im.mange.little.amount

object AmountFactory {
  def create(value: String) = {
    val multiplier = value match {
      case v if v.toLowerCase.endsWith("k") ⇒ Amount("1000")
      case v if v.toLowerCase.endsWith("m") ⇒ Amount("1000000")
      case x ⇒ Amount("1")
    }

    // TODO: this is hideous ...
    val fixedUp = value.replaceAll(",", "").replaceAll("k", "").replaceAll("m", "")
    Amount(fixedUp) * multiplier
  }
}

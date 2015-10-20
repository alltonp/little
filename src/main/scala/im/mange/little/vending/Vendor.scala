package im.mange.little.vending

trait Vendor {

  private[vending] class Lazy[T] {
    private var f: () ⇒ T = _
    def apply(): T = value
    def value: T = f()
    def value_=(f: ⇒ T) = this.f = () ⇒ f
  }

  protected def set[T](initializer: ⇒ T) = { val v = new Lazy[T](); v.value = initializer; v }
}

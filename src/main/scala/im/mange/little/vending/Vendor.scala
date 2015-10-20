package im.mange.little.vending

trait Vendor {

  private[vending] class Lazy[T] {
    private var f: () ⇒ T = _
    private var vended: Option[T] = _
    def apply(): T = value
    def value: T = { if (vended.isEmpty) vended = Some(f()); vended.get }
    def value_=(f: ⇒ T) = { this.f = () ⇒ f; vended = None }
  }

  protected def set[T](initializer: ⇒ T) = { val v = new Lazy[T](); v.value = initializer; v }
}

package im.mange.little.counter

case class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}

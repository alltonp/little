package im.mange.little.wait

import scala.annotation.tailrec

//TODO: this is all borrowed from driveby, it should be in little-wait.jar
//TODO: is this used by flakeless? if not, it can die (or mo
object Wait {
  def waitUpTo(timeout: Long = 5000, pollPeriod: Long = 1) = new Wait(timeout, pollPeriod)
}

class Wait(timeout: Long, pollPeriod: Long) {
  def forCondition(f: => Boolean, desc: => String, onFail: () => Unit = () => ()) {
    if (!conditionSatisfied(f, pollPeriod)) {
      onFail()
      throw new ConditionNotMetException("> FAILED: " + desc, timeout)
    }
  }

  private def conditionSatisfied[T](f: => Boolean, pollPeriod: Long): Boolean = {
    val end_? = System.currentTimeMillis() + timeout
    val end = if (end_? > 0) end_? else Long.MaxValue

    def tryIt = try {f} catch {case _: Exception => false}

    def timedOut = System.currentTimeMillis() >= end

    @tailrec
    def loop(last: Boolean): Boolean = {
      if (last || timedOut) last
      else {
        Thread.sleep(pollPeriod)
        loop(tryIt)
      }
    }
    loop(tryIt)
  }
}

class ConditionNotMetException(message: String) extends RuntimeException(message) {
  def this(conditionToCheck: String, millis: Long) = this(conditionToCheck + " (not met within " + millis + " millis)")
}

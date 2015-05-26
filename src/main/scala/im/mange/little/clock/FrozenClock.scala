package im.mange.little.clock

import org.joda.time.DateTime

//TIP: make sure it's UTC
case class FrozenClock(value: DateTime) extends Clock {
  def date = value.toLocalDate
  def dateTime = value
}

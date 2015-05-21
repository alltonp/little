package im.mange.little.clock

import org.joda.time.{DateTime, DateTimeZone}

case class FrozenClock(value: DateTime = new DateTime(2015, 1, 1, 9, 0, 0, DateTimeZone.UTC)) extends Clock {
  def date = value.toLocalDate
  def dateTime = value
}

package im.mange.little.clock

import org.joda.time.{DateTime, DateTimeZone}

//TODO: we should probably free to a more sensible date or just make it mandatory
case class FrozenClock(value: DateTime = new DateTime(2015, 1, 1, 9, 0, 0, DateTimeZone.UTC)) extends Clock {
  def date = value.toLocalDate
  def dateTime = value
}

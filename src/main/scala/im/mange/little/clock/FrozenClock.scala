package im.mange.little.clock

import org.joda.time.LocalDateTime

case class FrozenClock(value: LocalDateTime = new LocalDateTime(2015, 1, 1, 9, 0, 0)) extends Clock {
  def localDate     = value.toLocalDate
  def localDateTime = value
  def dateTime      = localDateTime.toDateTime
}

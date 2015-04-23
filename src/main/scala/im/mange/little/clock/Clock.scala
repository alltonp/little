package im.mange.little.clock

import org.joda.time.{DateTime, LocalDate, LocalDateTime}

trait Clock {
  def localDate: LocalDate
  def localDateTime: LocalDateTime
  def dateTime: DateTime
}

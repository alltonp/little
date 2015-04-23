package im.mange.little.clock

import org.joda.time.{LocalDate, LocalDateTime}

trait Clock {
  def localDate: LocalDate
  def localDateTime: LocalDateTime
}

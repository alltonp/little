package im.mange.little.clock

import org.joda.time.DateTimeZone.UTC
import org.joda.time.{DateTime, LocalDate, LocalDateTime}

object RealClock extends Clock {
  def localDate     = new LocalDate()
  def localDateTime = new LocalDateTime()
  def dateTime      = new DateTime(UTC)
}

package im.mange.little.clock

import org.joda.time.DateTimeZone.UTC
import org.joda.time.{DateTime, LocalDate, LocalDateTime}

object RealClock extends Clock {
  def date     = new LocalDate(UTC)
  def dateTime = new DateTime(UTC)
}
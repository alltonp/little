package im.mange.little.clock

import org.joda.time.{LocalDate, LocalDateTime}

object RealClock extends Clock {
  def localDate = new LocalDate()
  def localDateTime = new LocalDateTime()
}

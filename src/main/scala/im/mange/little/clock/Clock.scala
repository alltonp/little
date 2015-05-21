package im.mange.little.clock

import org.joda.time.{DateTime, LocalDate}

trait Clock {
  def date: LocalDate
  def dateTime: DateTime
}
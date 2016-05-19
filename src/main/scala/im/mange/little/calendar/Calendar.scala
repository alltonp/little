package im.mange.little.calendar

import im.mange.little.clock.{Clock, FrozenClock}
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone.UTC
import org.joda.time.LocalDate

trait Calendar {
  def currentBusinessDate: LocalDate
  def isBusinessDate(date: LocalDate): Boolean
  def previousBusinessDate(decrement: Int = 1): LocalDate
  def previousBusinessDate(decrement: Int, from: LocalDate): LocalDate
  def nextBusinessDate(increment: Int = 1): LocalDate
  def businessDatesBetween(first: LocalDate, last: LocalDate): Seq[LocalDate]
}

class NaiveCalendar(clock: Clock) extends Calendar {
  def currentBusinessDate: LocalDate = {
    val today = clock.date
    if (today.getDayOfWeek > FRIDAY) nextBusinessDateAfter(today) else today
  }

  def previousBusinessDate(decrement: Int): LocalDate = {
    previousBusinessDate(decrement, clock.date)
  }

  def previousBusinessDate(decrement: Int, from: LocalDate): LocalDate = {
    var date = previousBusinessDateBefore(from)
    for (i ← 2 to decrement) date = previousBusinessDateBefore(date)
    date
  }

  //TODO: we should probably support form here too, to be consistent
  def nextBusinessDate(increment: Int): LocalDate = {
    var date = nextBusinessDateAfter(clock.date)
    for (i ← 2 to increment) date = nextBusinessDateAfter(date)
    date
  }

  def isBusinessDate(date: LocalDate): Boolean = date.getDayOfWeek match {
    case SATURDAY | SUNDAY ⇒ false
    case _ ⇒ true
  }

  def businessDatesBetween(first: LocalDate, last: LocalDate): Seq[LocalDate] =
    if (first isAfter last) Stream.Empty
    else {
      val other = new NaiveCalendar(FrozenClock(first.toDateTimeAtCurrentTime(UTC)))
      other.currentBusinessDate +: businessDatesBetween(other.nextBusinessDate(), last)
    }

  private def nextBusinessDateAfter(date: LocalDate) =
    if (date.getDayOfWeek >= FRIDAY) date.plusWeeks(1).withDayOfWeek(MONDAY) else date.plusDays(1)

  private def previousBusinessDateBefore(date: LocalDate) =
    date.getDayOfWeek match {
      case MONDAY ⇒ date.minusWeeks(1).withDayOfWeek(FRIDAY)
      case SATURDAY | SUNDAY ⇒ date.withDayOfWeek(FRIDAY)
      case _ ⇒ date.minusDays(1)
    }
}

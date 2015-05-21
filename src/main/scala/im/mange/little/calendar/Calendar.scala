package im.mange.little.calendar

import im.mange.little.clock.Clock
import org.joda.time.DateTimeConstants._
import org.joda.time.LocalDate

trait Calendar {
  def currentBusinessDate: LocalDate
  def previousBusinessDate(increment: Int = 1): LocalDate
  def nextBusinessDate(increment: Int = 1): LocalDate
}

class NaiveCalendar(clock: Clock) extends Calendar {
  def currentBusinessDate: LocalDate = {
    val today = clock.date
    if (today.getDayOfWeek > FRIDAY) nextBusinessDateAfter(today) else today
  }

  def previousBusinessDate(decrement: Int): LocalDate = {
    val date1: LocalDate = clock.date
    var date = previousBusinessDateBefore(date1)
    for (i ← 2 to decrement) date = previousBusinessDateBefore(date)
    date
  }

  def nextBusinessDate(increment: Int): LocalDate = {
    var date = nextBusinessDateAfter(clock.date)
    for (i ← 2 to increment) date = nextBusinessDateAfter(date)
    date
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

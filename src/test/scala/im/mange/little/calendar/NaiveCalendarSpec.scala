package im.mange.little.calendar

import im.mange.little.clock.FrozenClock
import org.joda.time.DateTimeConstants._
import org.joda.time.{DateTime, LocalDate}
import org.scalatest.{MustMatchers, WordSpec}

class NaiveCalendarSpec extends WordSpec with MustMatchers {

  "previous business date during a working week" in {
    previousBusinessDateOn(MONDAY)    mustBe dateForLast(FRIDAY)
    previousBusinessDateOn(TUESDAY)   mustBe dateForThis(MONDAY)
    previousBusinessDateOn(WEDNESDAY) mustBe dateForThis(TUESDAY)
    previousBusinessDateOn(THURSDAY)  mustBe dateForThis(WEDNESDAY)
    previousBusinessDateOn(FRIDAY)    mustBe dateForThis(THURSDAY)
  }

  "previous business date during week-ends" in {
    previousBusinessDateOn(SATURDAY)  mustBe dateForThis(FRIDAY)
    previousBusinessDateOn(SUNDAY)    mustBe dateForThis(FRIDAY)
  }

  "previous business date from provided date" in {
    serviceWith(ignored).previousBusinessDate(decrement = 1, new LocalDate(2015, 12, 31)) mustBe new LocalDate(2015, 12, 30)
    serviceWith(ignored).previousBusinessDate(decrement = 2, new LocalDate(2015, 12, 31)) mustBe new LocalDate(2015, 12, 29)
  }

  "current business date during a working week" in {
    currentBusinessDateOn(MONDAY)    mustBe dateForThis(MONDAY)
    currentBusinessDateOn(TUESDAY)   mustBe dateForThis(TUESDAY)
    currentBusinessDateOn(WEDNESDAY) mustBe dateForThis(WEDNESDAY)
    currentBusinessDateOn(THURSDAY)  mustBe dateForThis(THURSDAY)
    currentBusinessDateOn(FRIDAY)    mustBe dateForThis(FRIDAY)
  }

  "current business date during week-ends" in {
    currentBusinessDateOn(SATURDAY)  mustBe dateForNext(MONDAY)
    currentBusinessDateOn(SUNDAY)    mustBe dateForNext(MONDAY)
  }

  "T+2 occurs in the same working week" in {
    twoBusinessDaysAfter(MONDAY)     mustBe dateForThis(WEDNESDAY)
    twoBusinessDaysAfter(TUESDAY)    mustBe dateForThis(THURSDAY)
    twoBusinessDaysAfter(WEDNESDAY)  mustBe dateForThis(FRIDAY)
  }

  "T+2 occurs in the next working week" in {
    twoBusinessDaysAfter(THURSDAY)   mustBe dateForNext(MONDAY)
    twoBusinessDaysAfter(FRIDAY)     mustBe dateForNext(TUESDAY)
    twoBusinessDaysAfter(SATURDAY)   mustBe dateForNext(TUESDAY)
    twoBusinessDaysAfter(SUNDAY)     mustBe dateForNext(TUESDAY)
  }

  "T-2 occurs in the same working week" in {
    twoBusinessDaysBefore(WEDNESDAY) mustBe dateForThis(MONDAY)
    twoBusinessDaysBefore(THURSDAY)  mustBe dateForThis(TUESDAY)
    twoBusinessDaysBefore(FRIDAY)    mustBe dateForThis(WEDNESDAY)
    twoBusinessDaysBefore(SATURDAY)  mustBe dateForThis(THURSDAY)
    twoBusinessDaysBefore(SUNDAY)    mustBe dateForThis(THURSDAY)
  }

  "T-2 occurs in the previous working week" in {
    twoBusinessDaysBefore(MONDAY)    mustBe dateForLast(THURSDAY)
    twoBusinessDaysBefore(TUESDAY)   mustBe dateForLast(FRIDAY)
  }

  "business dates between dates of the same working week" in {
    asOfNow.businessDatesBetween(dateForThis(MONDAY), dateForThis(FRIDAY)) mustBe Seq(
      dateForThis(MONDAY),
      dateForThis(TUESDAY),
      dateForThis(WEDNESDAY),
      dateForThis(THURSDAY),
      dateForThis(FRIDAY)
    )
  }

  "business dates between dates of consecutive working weeks" in {
    asOfNow.businessDatesBetween(dateForThis(WEDNESDAY), dateForNext(TUESDAY)) mustBe Seq(
      dateForThis(WEDNESDAY),
      dateForThis(THURSDAY),
      dateForThis(FRIDAY),
      dateForNext(MONDAY),
      dateForNext(TUESDAY)
    )
  }

  "business dates between dates several working weeks apart" in {
    asOfNow.businessDatesBetween(dateForThis(THURSDAY), dateForThis(TUESDAY).plusWeeks(2)) mustBe Seq(
      dateForThis(THURSDAY),
      dateForThis(FRIDAY),
      dateForNext(MONDAY),
      dateForNext(TUESDAY),
      dateForNext(WEDNESDAY),
      dateForNext(THURSDAY),
      dateForNext(FRIDAY),
      dateForNext(MONDAY).plusWeeks(1),
      dateForNext(TUESDAY).plusWeeks(1)
    )
  }

  "business dates between a date and another before it" in {
    asOfNow.businessDatesBetween(dateForNext(WEDNESDAY), dateForThis(TUESDAY)) mustBe empty
  }

  "business dates between a business date and itself" in {
    asOfNow.businessDatesBetween(dateForThis(WEDNESDAY), dateForThis(WEDNESDAY)) must contain only dateForThis(WEDNESDAY)
  }

  private def currentBusinessDateOn(dayOfWeek: Int)  = serviceWith(dayOfWeek).currentBusinessDate
  private def previousBusinessDateOn(dayOfWeek: Int) = serviceWith(dayOfWeek).previousBusinessDate()
  private def twoBusinessDaysAfter(dayOfWeek: Int)   = serviceWith(dayOfWeek).nextBusinessDate(increment = 2)
  private def twoBusinessDaysBefore(dayOfWeek: Int)  = serviceWith(dayOfWeek).previousBusinessDate(decrement = 2)

  private def serviceWith(dayOfWeek: Int) = new NaiveCalendar(FrozenClock(value = DateTime.now().withDayOfWeek(dayOfWeek)))
  private def asOfNow = serviceWith(DateTime.now().dayOfWeek().get())

  private def dateForLast(dayOfWeek: Int) = dateForThis(dayOfWeek).minusWeeks(1)
  private def dateForThis(dayOfWeek: Int) = LocalDate.now().withDayOfWeek(dayOfWeek)
  private def dateForNext(dayOfWeek: Int) = dateForThis(dayOfWeek).plusWeeks(1)

  private val ignored = 1
}

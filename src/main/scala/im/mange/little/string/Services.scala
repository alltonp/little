package im.mange.little.string

import im.mange.little.calendar.{NaiveCalendar, Calendar}
import im.mange.little.clock.{FrozenClock, RealClock, Clock}
import im.mange.little.date.DateFormatForHumans
import im.mange.little.vending.Vendor
import org.joda.time.DateTime

object Services extends App with Vendor {
  lazy val systemClock     = set[Clock] { RealClock}
  lazy val calendarService = set[Calendar] { new NaiveCalendar(systemClock())}
  lazy val dateFormats     = set { new DateFormatForHumans(systemClock())}

  println(s">>>>>>>>>>>>>>>>>>>>>>>>>>> before 0.0 (systemClock: ${toString(systemClock())}): " + dateFormats().shortDateTimeFormat.print(systemClock().dateTime))
  systemClock.value = FrozenClock(DateTime.parse("2015-01-22"))
  println(s">>>>>>>>>>>>>>>>>>>>>>>>>>>  after 1.1 (systemClock: ${toString(systemClock())}): " + dateFormats().shortDateTimeFormat.print(systemClock().dateTime))
  println(s">>>>>>>>>>>>>>>>>>>>>>>>>>>  after 1.2 (systemClock: ${toString(systemClock())}): " + dateFormats().shortDateTimeFormat.print(systemClock().dateTime))
  systemClock.value = FrozenClock(DateTime.parse("2018-12-25"))
  println(s">>>>>>>>>>>>>>>>>>>>>>>>>>>  after 2.1 (systemClock: ${toString(systemClock())}): " + dateFormats().shortDateTimeFormat.print(systemClock().dateTime))
  println(s">>>>>>>>>>>>>>>>>>>>>>>>>>>  after 2.2 (systemClock: ${toString(systemClock())}): " + dateFormats().shortDateTimeFormat.print(systemClock().dateTime))

  private def toString(x: Any) = s"$x@${Integer.toHexString(System.identityHashCode(x))}"
}
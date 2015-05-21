package im.mange.little.date

import im.mange.little.clock.Clock
import org.joda.time.DateTimeZone._
import org.joda.time.format.DateTimeFormat._
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.{DateTime, Interval, LocalDateTime, Period}

//TIP: we work on the assumption that it's best to:
//(1) always create/store in UTC
//(2) render how client wants to see it, but default to UTC
//TODO: pass the DTZ through ...
class DateFormatForHumans(clock: Clock) {
  val standardTimeFormat     = forPattern("HH:mm:ss").withZone(UTC)
  val standardDateTimeFormat = forPattern("HH:mm:ss EEE dd MMM yyyy").withZone(UTC)
  val shortDateTimeFormat    = forPattern("HH:mm:ss dd/MM/yyyy").withZone(UTC)
  val shortDateFormat        = forPattern("dd/MM/yyyy").withZone(UTC)
  val fileDateFormat         = forPattern("yyyy-MM-dd").withZone(UTC)
  val todayDateTimeFormat    = forPattern("HH:mm:ss 'Today'").withZone(UTC)
  val thisYearDateTimeFormat = forPattern("HH:mm:ss EEE dd MMM").withZone(UTC)

  private val agoFormat = new PeriodFormatterBuilder()
    .appendHours()
    .appendSuffix("h")
    .appendSeparator(", ")
    .printZeroRarelyLast()
    .appendMinutes()
    .appendSuffix("m")
    .appendSeparator(", ")
    .appendSeconds()
    .appendSuffix("s")
    .toFormatter

  def today(when: DateTime) = formatFor(when).print(when)
  def ago(when: DateTime) = agoFormat.print(new Interval(when, todaysDateTime).toPeriod)
  def ago(period: Period) = agoFormat.print(period)
  def timeNow = standardTimeFormat.print(todaysDateTime)

  private def formatFor(when: DateTime) = {
    if (isToday(when)) todayDateTimeFormat
    else if (isThisYear(when)) thisYearDateTimeFormat
    else standardDateTimeFormat
  }

  private def isToday(when: DateTime) = isSameDay(when, todaysDateTime)
  private def isThisYear(when: DateTime) = when.isAfter(todaysDateTime.minusYears(1))

  private def isSameDay(when: DateTime, as: DateTime) =
    when.getYear == as.getYear &&
      when.getMonthOfYear == as.getMonthOfYear &&
      when.getDayOfMonth == as.getDayOfMonth

  private def todaysDateTime = clock.dateTime
}

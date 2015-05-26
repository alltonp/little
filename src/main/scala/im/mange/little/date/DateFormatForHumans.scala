package im.mange.little.date

import im.mange.little.clock.Clock
import org.joda.time._
import org.joda.time.format.DateTimeFormat._
import org.joda.time.format.PeriodFormatterBuilder

//TIP: we work on the assumption that it's best to:
//(1) always create/store in UTC
//(2) render how client wants to see it, default to default - but allow it be passed
class DateFormatForHumans(clock: Clock, dateTimeZone: DateTimeZone = DateTimeZone.getDefault) {
  val standardTimeFormat     = forPattern("HH:mm:ss").withZone(dateTimeZone)
  val standardDateTimeFormat = forPattern("HH:mm:ss EEE dd MMM yyyy").withZone(dateTimeZone)
  val shortDateTimeFormat    = forPattern("HH:mm:ss dd/MM/yyyy").withZone(dateTimeZone)
  val shortDateFormat        = forPattern("dd/MM/yyyy").withZone(dateTimeZone)
  val fileDateFormat         = forPattern("yyyy-MM-dd").withZone(dateTimeZone)
  val todayDateTimeFormat    = forPattern("HH:mm:ss 'Today'").withZone(dateTimeZone)
  val thisYearDateTimeFormat = forPattern("HH:mm:ss EEE dd MMM").withZone(dateTimeZone)

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

package im.mange.little.json

import im.mange.little.amount.Amount
import im.mange.little.percentage.Percentage
import org.joda.time.format.ISODateTimeFormat
import org.joda.time._
import org.json4s._
import org.json4s.ext.DateParser

import scala.reflect.ClassTag
import scala.util.Try

object LittleJodaSerialisers {
  private val datePattern = ISODateTimeFormat.date().withZoneUTC()
  private val dateTimePattern = ISODateTimeFormat.localDateOptionalTimeParser().withZoneUTC()

  val date     = SerialSerialiser[LocalDate](s ⇒ datePattern.print(s), s ⇒ opt(datePattern.parseLocalDate(s)))
  val dateTime = UtcDateTimeSerializer

  def all = Seq(date, dateTime)

  private def opt[T](v: ⇒T): Option[T] = Try(v).toOption

  //TIP: this is because the json4s DateTimeSerializer parses UTC DateTimes in the users default TimeZone (i.e. London), tsk.
  case object UtcDateTimeSerializer extends CustomSerializer[DateTime](format => (
    {
      case JString(s) => new DateTime(DateParser.parse(s, format), DateTimeZone.UTC)
      case JNull => null
    },
    {
      case d: DateTime => JString(format.dateFormat.format(d.toDate))
    }
    ))
}

object LittleSerialisers {
  val number        = SerialSerialiser[BigDecimal](_.toString(), s ⇒ opt(BigDecimal(s)))
  val percentage    = SerialSerialiser[Percentage](_.underlyingValue, s ⇒ opt(Percentage((BigDecimal(s) * 100).toString())))
  val boolean       = SerialSerialiser[Boolean](_.toString, s ⇒ opt(s.toBoolean))
  val amount        = SerialSerialiser[Amount](_.underlyingValue, s ⇒ opt(Amount(s)))

  def all = Seq(number, percentage, boolean, amount)

  private def opt[T](v: ⇒T): Option[T] = Try(v).toOption
}

case class SerialSerialiser[T: ClassTag](serialise: T ⇒ String, deserialise: String ⇒ Option[T]) extends Serializer[T] {
  private val TheClass       = implicitly[ClassTag[T]].runtimeClass
  private val serialiserName = TheClass.getSimpleName

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), T] = {
    case (TypeInfo(TheClass, _), JString(value)) ⇒ deserialise(value).getOrElse(failHard(value, None))
  }

  private def failHard(value: Any, thrown: Option[Throwable] = None) =
    throw new MappingException(s"Can't convert [$value] to an instance of $serialiserName.${thrown.fold("")("Wrapped exception: " + _)}")

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: T ⇒ JString(serialise(x))
  }
}

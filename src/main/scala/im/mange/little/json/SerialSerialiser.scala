package im.mange.little.json

import im.mange.little.amount.Amount
import im.mange.little.percentage.Percentage
import org.joda.time.format.ISODateTimeFormat._
import org.joda.time.{LocalDate, LocalDateTime, LocalTime}
import org.json4s._

import scala.reflect.ClassTag
import scala.util.Try

object LittleSerialisers {
  val localDate     = SerialSerialiser[LocalDate](s ⇒ JodaTime.datePattern.print(s), s ⇒ opt(JodaTime.datePattern.parseLocalDate(s)))
  val localTime     = SerialSerialiser[LocalTime](t ⇒ t.toString, s ⇒ opt(JodaTime.timePattern.parseLocalTime(s)))
  val localDateTime = SerialSerialiser[LocalDateTime](t ⇒ t.toString, s⇒ opt(JodaTime.dateTimePattern.parseLocalDateTime(s)))
  val percentage    = SerialSerialiser[Percentage](_.underlyingValue, s ⇒ opt(Percentage((BigDecimal(s) * 100).toString())))
  val boolean       = SerialSerialiser[Boolean](_.toString, s ⇒ opt(s.toBoolean))
  val amount        = SerialSerialiser[Amount](_.underlyingValue, s ⇒ opt(Amount(s)))

  def all = Seq(
    localDate, localTime, localDateTime, percentage, boolean, amount
  )
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

object JodaTime {
  val datePattern = date()
  val timePattern = localTimeParser()
  val dateTimePattern = localDateOptionalTimeParser()
}

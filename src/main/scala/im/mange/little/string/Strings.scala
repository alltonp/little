package im.mange.little.string

object Strings {
  def titleCase(value: String): String = "[A-Z\\d]".r.replaceAllIn(value, { m ⇒ " " + m.group(0) }).trim.capitalize
  def removeExcessWhitespace(value: String): String = value.trim.replaceAll("\\s{1,}", "")
}

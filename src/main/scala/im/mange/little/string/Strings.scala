package im.mange.little.string

object Strings {
  def titleCase(s: String) = "[A-Z\\d]".r.replaceAllIn(s, {m ⇒ " " + m.group(0) }).trim.capitalize
}

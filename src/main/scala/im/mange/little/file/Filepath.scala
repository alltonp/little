package im.mange.little.file

import java.nio.file.StandardCopyOption._
import java.nio.file.StandardOpenOption._
import java.nio.file.{DirectoryStream, Files, OpenOption, Path}
import java.util.Comparator

import scala.io.Codec
import scala.reflect.io.File

object Filepath {
  def load(path: Path, codec: Codec = Codec.UTF8): String =
    File(path.toFile).slurp(codec)
    //or Files.readAllLines(path).asScala.mkString("\n")

  def loadLines(path: Path, codec: Codec = Codec.UTF8): List[String] =
    load(path, codec).split(System.lineSeparator).toList

  def save(content: String, path: Path, codec: Codec = Codec.UTF8): Path = {
    if (!exists(path.getParent)) createDir(path.getParent)
    write(content, path, codec, CREATE, WRITE, TRUNCATE_EXISTING)
  }

  def append(content: String, path: Path, codec: Codec = Codec.UTF8): Path =
    write(content, path, codec, CREATE, WRITE, APPEND)

  def move(src: Path, dest: Path): Path =
    Files.move(src, dest, ATOMIC_MOVE, REPLACE_EXISTING)

  def copy(src: Path, dest: Path): Path =
    Files.copy(src, dest, ATOMIC_MOVE, REPLACE_EXISTING)

  def createDir(dir: Path): Path = Files.createDirectories(dir)

  def listDir(dir: Path, glob: String): List[String] = {
    val stream: DirectoryStream[Path] = Files.newDirectoryStream(dir, glob)
    var result: List[String] = Nil
    stream.forEach(f => result = f.getFileName.toFile.getName :: result)
    result
  }

  def deleteDir(dir: Path): Unit = {
    if (Files.exists(dir)) Files.walk(dir)
      .sorted(Comparator.reverseOrder())
      .forEach(Files.delete(_))
  }

  private def write(content: String, path: Path, codec: Codec, options: OpenOption*) =
    Files.write(path, content.getBytes(codec.charSet), options:_*)

  private def exists(path: Path) = Files.exists(path)
}

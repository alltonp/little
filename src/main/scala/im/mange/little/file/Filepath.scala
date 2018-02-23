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

  def move(source: Path, target: Path): Path =
    Files.move(source, target, ATOMIC_MOVE, REPLACE_EXISTING)

  def copy(source: Path, target: Path): Path =
    Files.copy(source, target, ATOMIC_MOVE, REPLACE_EXISTING)

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

  //TIP: keep api to use Path, means the client will be less stringy
  //TODO: maybe the dir ones should be on a different object ... again
  //  def toPath(value: String) = Paths.get(value)

  //TODO: kill this soon, if def not needed - it's basically save("") anyway
  //def touch(path: Path): Path = createFile(path)
}

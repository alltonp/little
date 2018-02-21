package im.mange.little.file

import java.nio.file.StandardCopyOption._
import java.nio.file.StandardOpenOption._
import java.nio.file.{DirectoryStream, Files, Path}
import java.util.Comparator

import scala.io.Codec
import scala.reflect.io.File

object Filepath {
  def load(path: Path, codec: Codec = Codec.UTF8): String =
    File(path.toFile).slurp(codec)
    //or Files.readAllLines(path).asScala.mkString("\n")

  def save(content: String, path: Path, codec: Codec = Codec.UTF8): Path =
    Files.write(path, content.getBytes(codec.charSet), CREATE, WRITE, TRUNCATE_EXISTING)

  def append(content: String, path: Path, codec: Codec = Codec.UTF8): Path =
    Files.write(path, content.getBytes(codec.charSet), CREATE, WRITE, APPEND)

  //TODO: kill these soon, if not needed
  //def touch(path: Path): Path = createFile(path)
  //def exists(path: Path) = Files.exists(path)

  def move(source: Path, target: Path): Path =
    Files.move(source, target, ATOMIC_MOVE, REPLACE_EXISTING)

  def copy(source: Path, target: Path): Path =
    Files.copy(source, target, ATOMIC_MOVE, REPLACE_EXISTING)

  //TODO: not sure about this version, prefer string for overloads
//  def move(source: File, target: File): Path = move(Paths.get(source.path), Paths.get(target.path))

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
//  def toPath(value: String) = Paths.get(value)

  //TODO: consider string values instead of/as well as Path
  //TODO: maybe the dir ones should be on a different object ... again
}

package im.mange.little.file

import java.nio.file.Files._
import java.nio.file.StandardCopyOption._
import java.nio.file.StandardOpenOption._
import java.nio.file.{DirectoryStream, Files, Path, Paths}

import scala.io.Codec
import scala.reflect.io.File

object Filepath {
  def load(path: Path, codec: Codec = Codec.UTF8): String = File(path.toFile).slurp(codec)

  def save(content: String, path: Path, codec: Codec = Codec.UTF8): Path =
    write(path, content.getBytes(codec.charSet), CREATE, WRITE, TRUNCATE_EXISTING)

  def append(content: String, path: Path, codec: Codec = Codec.UTF8): Path =
    write(path, content.getBytes(codec.charSet), CREATE, WRITE, APPEND)

  //TODO: kill this soon, if not needed
  //def touch(path: Path): Path = createFile(path)

  def move(source: Path, target: Path): Path = Files.move(source, target, ATOMIC_MOVE)
  def move(source: File, target: File): Path = move(Paths.get(source.path), Paths.get(target.path))

  def createDir(dir: Path): Path = Files.createDirectories(dir)

  def listDir(dir: Path, glob: String): List[String] = {
    val stream: DirectoryStream[Path] = Files.newDirectoryStream(dir, glob)
    var result: List[String] = Nil
    stream.forEach(f => result = f.getFileName.toFile.getName :: result)
    result
  }

  //TODO: add deleteDir(dir: Path)

  //TODO: maybe the dir ones should be on a different object ... again
}

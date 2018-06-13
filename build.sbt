import scala.util.Try
import xerial.sbt.Sonatype._

name := "little"
organization := "im.mange"
version := Try(sys.env("TRAVIS_BUILD_NUMBER")).map("0.0." + _).getOrElse("1.0-SNAPSHOT")

scalaVersion := "2.12.4"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  //TIP: try to keep my deps range based to allows clients to specifiy
  "com.github.nscala-time" %% "nscala-time" % "[2.18.0,2.99.99]" % "provided",

//  "joda-time"      % "joda-time"      % "[2.7,3.0]" % "provided",
//  "org.joda"       % "joda-convert"   % "[1.6,2.0]" % "provided",

  //TODO: make range based once json4s fixes their new versions
  "org.json4s"     %% "json4s-native" % "3.2.11" % "provided"
    exclude("org.scala-lang", "scala-compiler")
//    exclude("org.scala-lang", "scalap")
    exclude("joda-time", "joda-time")
  ,

  //TODO: make range based once json4s fixes their new versions
  "org.json4s"     %% "json4s-ext"    % "3.2.11" % "provided"
    exclude("joda-time", "joda-time")
  ,

  "org.scala-lang" % "scala-reflect"  % scalaVersion.value % "provided",
  "org.scalatest"  %% "scalatest"     % "3.0.1" % "test" //notTransitive()
)

//net.virtualvoid.sbt.graph.Plugin.graphSettings

sonatypeSettings

publishTo <<= version { project_version ⇒
  val nexus = "https://oss.sonatype.org/"
  if (project_version.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
publishArtifact in Test := false

homepage := Some(url("https://github.com/alltonp/little"))
licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", System.getenv("SONATYPE_USER"), System.getenv("SONATYPE_PASSWORD"))

pomExtra :=
    <scm>
      <url>git@github.com:alltonp/little.git</url>
      <connection>scm:git:git@github.com:alltonp/little.git</connection>
    </scm>
    <developers>
      <developer>
        <id>alltonp</id>
      </developer>
    </developers>

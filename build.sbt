lazy val Scala211 = "2.11.12"
lazy val Scala212 = "2.12.11"
lazy val Scala213 = "2.13.2"

organization := "com.github.andriimartynov"
name := "zio-jasync-sql"
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
scalaVersion := Scala212
startYear := Some(2020)
version := "1.0.1"

crossScalaVersions := Seq(Scala211, Scala212, Scala213)

libraryDependencies += JasyncSql.dependency

libraryDependencies ++= ScalaTest.dependencies

libraryDependencies += Tools.dependency

libraryDependencies += Zio.dependency

credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  sys.env.getOrElse("GPG_PUBLIC_KEY", ""), // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)

resolvers += Resolver.jcenterRepo
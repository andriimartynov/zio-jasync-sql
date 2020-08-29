lazy val Scala211 = "2.11.12"
lazy val Scala212 = "2.12.12"
lazy val Scala213 = "2.13.3"

organization := "com.github.andriimartynov"
name := "zio-jasync-sql"
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
scalaVersion := Scala212
startYear := Some(2020)
version := "1.0.2"

crossScalaVersions := Seq(Scala211, Scala212, Scala213)

libraryDependencies += JasyncSql.dependency

libraryDependencies ++= ScalaTest.dependencies

libraryDependencies += Tools.dependency

libraryDependencies += Zio.dependency

pomExtra := {
  <url>https://github.com/andriimartynov/zio-jasync-sql</url>
    <licenses>
      <license>
        <name>Apache 2 License</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:andriimartynov/zio-jasync-sql.git</url>
      <connection>scm:git:git@github.com:andriimartynov/zio-jasync-sql.git</connection>
    </scm>
    <developers>
      <developer>
        <id>andriimartynov</id>
        <name>andriimartynov</name>
        <url>https://github.com/andriimartynov</url>
      </developer>
    </developers>
}

credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  sys.env.getOrElse("GPG_PUBLIC_KEY", ""), // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)

resolvers += Resolver.jcenterRepo
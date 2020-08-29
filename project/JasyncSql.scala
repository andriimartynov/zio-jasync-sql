import sbt._

object JasyncSql {
  lazy val JASYNC_VERSION = "1.1.+"
  
  val dependency: ModuleID =
    "com.github.jasync-sql" % "jasync-common" % JASYNC_VERSION
  
}

import sbt._

object Zio {
  lazy val ZIO_VERSION = "1.0.1"

  lazy val dependency: ModuleID =
    "dev.zio" %% "zio" % ZIO_VERSION

}

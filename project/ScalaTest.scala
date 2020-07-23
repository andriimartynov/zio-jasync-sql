import sbt._

object ScalaTest {
  lazy val SCALATEST_VERSION = "3.2.0"
  
  def dependencies: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % SCALATEST_VERSION % Test,
    "org.github.andriimartynov" %% "scalatest-zio" % SCALATEST_VERSION % Test,
    "org.mockito" %% "mockito-scala-scalatest" % "1.14.2" % Test
  )

}

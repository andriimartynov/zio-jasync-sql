import sbt._

object Tools {
  def dependency: ModuleID = 
    "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"
  
}

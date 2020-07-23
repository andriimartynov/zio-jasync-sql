package com.github.jasync.sql.db

import java.util.concurrent.CompletableFuture

import org.joda.time.LocalDateTime

import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters
import scala.concurrent.Future

object ScalaConverters {

  implicit val toRowDataOps: RowData => RowDataOps =
    rowData => new RowDataOps(rowData)

  final class RowDataOps(private val rowData: RowData) extends AnyVal {

    def as[T](column: Int): T =
      rowData.getAs[T](column)

    def as[T](column: String): T =
      rowData.getAs[T](column)

    def boolean(column: Int): Boolean =
      rowData.getBoolean(column)

    def boolean(column: String): Boolean =
      rowData.getBoolean(column)

    def byte(column: Int): Byte =
      rowData.getByte(column)

    def byte(column: String): Byte =
      rowData.getByte(column)

    def date(column: Int): LocalDateTime =
      rowData.getDate(column)

    def date(column: String): LocalDateTime =
      rowData.getDate(column)

    def double(column: Int): Double =
      rowData.getDouble(column)

    def double(column: String): Double =
      rowData.getDouble(column)

    def float(column: Int): Float =
      rowData.getFloat(column)

    def float(column: String): Float =
      rowData.getFloat(column)

    def int(column: Int): Int =
      rowData.getInt(column)

    def int(column: String): Int =
      rowData.getInt(column)

    def long(column: Int): Long =
      rowData.getLong(column)

    def long(column: String): Long =
      rowData.getLong(column)

    def string(column: Int): String =
      rowData.getString(column)

    def string(column: String): String =
      rowData.getString(column)

  }

  implicit def toQueryResultOps: QueryResult => QueryResultOps =
    queryResult => new QueryResultOps(queryResult)

  final class QueryResultOps(private val queryResult: QueryResult) extends AnyVal {

    def rows: Seq[RowData] =
      queryResult.getRows.asScala.toSeq //in Scala 2.13 asScala return Buffer

    def rowsAffected: Long =
      queryResult.getRowsAffected

    def statusMessage: String =
      queryResult.getStatusMessage

  }

  protected[db] implicit def toCompletableFuture[A](future: Future[A]): FutureOps[A] =
    new FutureOps[A](future)

  protected[db] final class FutureOps[A](private val future: Future[A]) extends AnyVal {
    def asJava: CompletableFuture[A] =
      FutureConverters.toJava(future).toCompletableFuture
  }

  protected[db] implicit def toFunctionOps[T, R](f: T => R): FunctionOps[T, R] =
    new FunctionOps[T, R](f)

  protected[db] final class FunctionOps[T, R](private val f: T => R) extends AnyVal {
    def asKotlin: kotlin.jvm.functions.Function1[T, R] =
      new kotlin.jvm.functions.Function1[T, R] {
        override def invoke(p1: T): R = f(p1)
      }
  }
}

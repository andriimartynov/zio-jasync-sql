package com.github.jasync.sql.db

import java.util.concurrent.CompletableFuture

import com.github.jasync.sql.db.ScalaConverters._

import scala.collection.JavaConverters._
import zio._

object ConnectionService {

  type ConnectionService = Has[ConnectionService.Service]

  trait Service {
    def connect(): Task[ConnectionService]

    def disconnect(): Task[ConnectionService]

    def inTransaction[A](
        rio: RIO[ConnectionService, A]
    )(implicit
        runtime: Runtime[_]
    ): Task[A]

    def isConnected: UIO[Boolean]

    def releasePreparedStatement(
        query: String
    ): Task[Boolean]

    def sendPreparedStatement(
        query: String
    ): Task[QueryResult]

    def sendPreparedStatement(
        query: String,
        values: Any*
    ): Task[QueryResult]

    def sendQuery(
        query: String
    ): Task[QueryResult]

    private[db] def connectionPool: Connection
  }

  object Service {
    private[db] def apply(pool: Connection): Service =
      new Service {

        def connect(): Task[ConnectionService] =
          Task
            .fromCompletionStage(connectionPool.connect())
            .map(_ => Has(this))

        def disconnect(): Task[ConnectionService] =
          Task
            .fromCompletionStage(connectionPool.disconnect())
            .map(_ => Has(this))

        def inTransaction[A](
            rio: RIO[ConnectionService, A]
        )(implicit
            runtime: Runtime[_]
        ): Task[A] = {
          val f: Connection => CompletableFuture[A] =
            (p1: Connection) => {
              val layer: Layer[Nothing, ConnectionService] =
                ZLayer.fromFunction(_ => Service(p1))

              runtime
                .unsafeRunToFuture(rio.provideLayer(layer))
                .asJava
            }

          Task.fromCompletionStage(connectionPool.inTransaction[A](f.asKotlin))
        }

        def isConnected: UIO[Boolean] =
          UIO
            .fromFunction(_ => connectionPool.isConnected)

        def releasePreparedStatement(
            query: String
        ): Task[Boolean] =
          Task
            .fromCompletionStage(
              connectionPool.releasePreparedStatement(query)
            )
            .map(Boolean.unbox)

        def sendPreparedStatement(
            query: String
        ): Task[QueryResult] =
          Task
            .fromCompletionStage(connectionPool.sendQuery(query))

        def sendPreparedStatement(
            query: String,
            values: Any*
        ): Task[QueryResult] =
          Task
            .fromCompletionStage(connectionPool.sendPreparedStatement(query, values.asJava))

        def sendQuery(
            query: String
        ): Task[QueryResult] =
          Task
            .fromCompletionStage(connectionPool.sendQuery(query))

        private[db] val connectionPool = pool
      }

  }

  val live: Connection => Layer[Throwable, ConnectionService] =
    pool =>
      ZLayer
        .fromAcquireRelease(
          Task
            .fromCompletionStage(
              pool.connect()
            )
            .map(c => Service(c))
        )(c => UIO.fromFunction(_ => c.connectionPool.disconnect()))

  val managed: Connection => Managed[Throwable, ConnectionService.Service] =
    pool =>
      Managed
        .make(
          Task
            .fromCompletionStage(
              pool.connect()
            )
            .map(c => Service(c))
        )(c => UIO.fromFunction(_ => c.connectionPool.disconnect()))

  def connect(): RIO[ConnectionService, ConnectionService] =
    ZIO.accessM(_.get.connect())

  def disconnect(): RIO[ConnectionService, ConnectionService] =
    ZIO.accessM(_.get.disconnect())

  def inTransaction[A](
      rio: RIO[ConnectionService, A]
  )(implicit
      runtime: Runtime[_]
  ): RIO[ConnectionService, A] =
    ZIO.accessM(_.get.inTransaction(rio))

  def isConnected: RIO[ConnectionService, Boolean] =
    ZIO.accessM(_.get.isConnected)

  def releasePreparedStatement(
      query: String
  ): RIO[ConnectionService, Boolean] =
    ZIO.accessM(_.get.releasePreparedStatement(query))

  def sendPreparedStatement(
      query: String
  ): RIO[ConnectionService, QueryResult] =
    ZIO.accessM(_.get.sendPreparedStatement(query))

  def sendPreparedStatement(
      query: String,
      values: Any*
  ): RIO[ConnectionService, QueryResult] =
    ZIO.accessM(_.get.sendPreparedStatement(query, values: _*))

  def sendQuery(
      query: String
  ): RIO[ConnectionService, QueryResult] =
    ZIO.accessM(_.get.sendQuery(query))
}

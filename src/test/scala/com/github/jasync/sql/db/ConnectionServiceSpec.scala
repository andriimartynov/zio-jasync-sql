package com.github.jasync.sql.db

import java.util.concurrent.CompletableFuture

import com.github.jasync.sql.db.ConnectionService.{Service, _}
import org.mockito.captor.ArgCaptor
import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.mockito.matchers.EqTo
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.ZioFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.{Layer, RIO, Task, ZLayer}

import scala.util.Try

class ConnectionServiceSpec extends ZioFlatSpec
  with BeforeAndAfterEach
  with Matchers
  with ArgumentMatchersSugar
  with MockitoSugar {

  private val pool = mock[TestConnection]
  private val poolConnection: Connection = pool
  implicit protected val layer: Layer[Nothing, ConnectionService] = ZLayer.fromFunction(_ => Service(pool))

  override def beforeEach() {
    reset(pool)
  }

  "service" should "return future of poolConnection on connect" in {
    when(pool.connect()).thenReturn(toCompletableFuture(poolConnection))

    for {
      result <- connect()
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).connect()
        result.get[ConnectionService.Service].connectionPool should be(poolConnection)
      })
    } yield assertion
  }

  "service" should "return future of poolConnection on disconnect" in {
    when(pool.disconnect()).thenReturn(toCompletableFuture(poolConnection))

    for {
      result <- disconnect()
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).disconnect()
        result.get[ConnectionService.Service].connectionPool should be(poolConnection)
      })
    } yield assertion
  }

  //TODO: add captor for check pool inTransaction params
  "service" should "return 10 on inTransaction" in {
    when(pool.inTransaction[Int](any[kotlin.jvm.functions.Function1[Connection, CompletableFuture[Int]]])).thenReturn(toCompletableFuture(10))

    for {
      result <- inTransaction[Int](RIO.fromFunction[ConnectionService, Int] { _ => 11 })
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).inTransaction[Int](any[kotlin.jvm.functions.Function1[Connection, CompletableFuture[Int]]])
        result should be(10)
      })
    } yield assertion
  }

  "service" should "return true on isConnected" in {
    when(pool.isConnected).thenReturn(true)

    for {
      result <- isConnected
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).isConnected
        result should be(true)
      })
    } yield assertion
  }

  "service" should "return false on isConnected" in {
    when(pool.isConnected).thenReturn(false)

    for {
      result <- isConnected
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).isConnected
        result should be(false)
      })
    } yield assertion
  }

  "service" should "return future of true on releasePreparedStatement query" in {
    when(pool.releasePreparedStatement("query")).thenReturn(toCompletableFuture(true))
    when(pool.releasePreparedStatement("query2")).thenReturn(toCompletableFuture(false))

    for {
      result <- releasePreparedStatement("query")
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).releasePreparedStatement(argThat(EqTo("query")))
        verify(pool, never).releasePreparedStatement(argThat(EqTo("query2")))
        result should be(true)
      })
    } yield assertion
  }

  "service" should "return future of false on releasePreparedStatement query2" in {
    when(pool.releasePreparedStatement("query")).thenReturn(toCompletableFuture(true))
    when(pool.releasePreparedStatement("query2")).thenReturn(toCompletableFuture(false))

    for {
      result <- releasePreparedStatement("query2")
      assertion <- Task.fromTry(Try {
        verify(pool, never).releasePreparedStatement(argThat(EqTo("query")))
        verify(pool, times(1)).releasePreparedStatement(argThat(EqTo("query2")))
        result should be(false)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult on sendPreparedStatement query" in {
    val queryResult = mock[QueryResult]
    val queryResult2 = mock[QueryResult]

    when(pool.sendPreparedStatement("query")).thenReturn(toCompletableFuture(queryResult))
    when(pool.sendPreparedStatement("query2")).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendPreparedStatement("query")
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendPreparedStatement(argThat(EqTo("query")))
        verify(pool, never).sendPreparedStatement(argThat(EqTo("query2")))
        result should be(queryResult)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult2 on sendPreparedStatement query2" in {
    val queryResult = mock[QueryResult]
    val queryResult2 = mock[QueryResult]

    when(pool.sendPreparedStatement("query")).thenReturn(toCompletableFuture(queryResult))
    when(pool.sendPreparedStatement("query2")).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendPreparedStatement("query2")
      assertion <- Task.fromTry(Try {
        verify(pool, never).sendPreparedStatement(argThat(EqTo("query")))
        verify(pool, times(1)).sendPreparedStatement(argThat(EqTo("query2")))
        result should be(queryResult2)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult on sendPreparedStatement query and values" in {
    val queryCaptor = ArgCaptor[String]
    val valuesCaptor = ArgCaptor[java.util.List[_ <: Any]]
    val queryResult = mock[QueryResult]

    when(pool.sendPreparedStatement(*, *)).thenReturn(toCompletableFuture(queryResult))

    for {
      result <- sendPreparedStatement("query", Seq(1, 2, 3))
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendPreparedStatement(queryCaptor, valuesCaptor)
        queryCaptor.value should be("query")
        val values = valuesCaptor.value
        values.size() should be(3)
        values.get(0) should be(1)
        values.get(1) should be(2)
        values.get(2) should be(3)

        result should be(queryResult)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult2 on sendPreparedStatement query2 and values2" in {
    val queryCaptor = ArgCaptor[String]
    val valuesCaptor = ArgCaptor[java.util.List[_ <: Any]]
    val queryResult2 = mock[QueryResult]

    when(pool.sendPreparedStatement(*, *)).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendPreparedStatement("query2", Seq("one", "two"))
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendPreparedStatement(queryCaptor, valuesCaptor)
        queryCaptor.value should be("query2")
        val values = valuesCaptor.value
        values.size() should be(2)
        values.get(0) should be("one")
        values.get(1) should be("two")

        result should be(queryResult2)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult on sendPreparedStatement query and values and release" in {
    val queryCaptor = ArgCaptor[String]
    val valuesCaptor = ArgCaptor[java.util.List[_ <: Any]]
    val releaseCaptor = ArgCaptor[Boolean]
    val queryResult = mock[QueryResult]

    when(pool.sendPreparedStatement(*, *, *)).thenReturn(toCompletableFuture(queryResult))

    for {
      result <- sendPreparedStatement("query", Seq(1, 2, 3), release = true)
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendPreparedStatement(queryCaptor, valuesCaptor, releaseCaptor)
        queryCaptor.value should be("query")
        releaseCaptor.value should be(true)
        val values = valuesCaptor.value
        values.size() should be(3)
        values.get(0) should be(1)
        values.get(1) should be(2)
        values.get(2) should be(3)

        result should be(queryResult)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult2 on sendPreparedStatement query2 and values2 and release2" in {
    val queryCaptor = ArgCaptor[String]
    val valuesCaptor = ArgCaptor[java.util.List[_ <: Any]]
    val releaseCaptor = ArgCaptor[Boolean]
    val queryResult2 = mock[QueryResult]

    when(pool.sendPreparedStatement(*, *, *)).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendPreparedStatement("query2", Seq("one", "two"), release = false)
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendPreparedStatement(queryCaptor, valuesCaptor, releaseCaptor)
        queryCaptor.value should be("query2")
        releaseCaptor.value should be(false)
        val values = valuesCaptor.value
        values.size() should be(2)
        values.get(0) should be("one")
        values.get(1) should be("two")

        result should be(queryResult2)
      })
    } yield assertion
  }


  "service" should "return future of QueryResult on sendQuery query" in {
    val queryResult = mock[QueryResult]
    val queryResult2 = mock[QueryResult]

    when(pool.sendQuery("query")).thenReturn(toCompletableFuture(queryResult))
    when(pool.sendQuery("query2")).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendQuery("query")
      assertion <- Task.fromTry(Try {
        verify(pool, times(1)).sendQuery(argThat(EqTo("query")))
        verify(pool, never).sendQuery(argThat(EqTo("query2")))
        result should be(queryResult)
      })
    } yield assertion
  }

  "service" should "return future of QueryResult2 on sendQuery query2" in {
    val queryResult = mock[QueryResult]
    val queryResult2 = mock[QueryResult]

    when(pool.sendQuery("query")).thenReturn(toCompletableFuture(queryResult))
    when(pool.sendQuery("query2")).thenReturn(toCompletableFuture(queryResult2))

    for {
      result <- sendQuery("query2")
      assertion <- Task.fromTry(Try {
        verify(pool, never).sendQuery(argThat(EqTo("query")))
        verify(pool, times(1)).sendQuery(argThat(EqTo("query2")))
        result should be(queryResult2)
      })
    } yield assertion
  }

  private def toCompletableFuture[T](v: T): CompletableFuture[T] =
    CompletableFuture.completedFuture(v)
}

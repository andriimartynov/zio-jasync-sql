package com.github.jasync.sql.db

import com.github.jasync.sql.db.ScalaConverters.toQueryResultOps
import com.github.jasync.sql.db.general.MutableResultSet
import org.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.JavaConverters._

class QueryResultOpsSpec extends AnyFlatSpec
  with Matchers
  with MockitoSugar {

  private val rowData = mock[RowData]
  private val rowData2 = mock[RowData]
  private val rowData3 = mock[RowData]
  private val rows: ResultSet =
    new MutableResultSet(List.empty.asJava, List(rowData, rowData2, rowData3).asJava)
  private val queryResult = new QueryResult(3, "status", rows)

  "rowData" should "return rows as scala Seq" in {
    val result = queryResult.rows

    result.size should be(3)
    result should be(Seq(rowData, rowData2, rowData3))

    result.head should be(rowData)
    result(1) should be(rowData2)
    result.last should be(rowData3)
  }

  "rowData" should "return 3 when ask rows affected" in {
    queryResult.rowsAffected should be(3)
  }

  "rowData" should "return status when ask status message" in {
    queryResult.statusMessage should be("status")
  }

}

package com.github.jasync.sql.db

import com.github.jasync.sql.db.ScalaConverters.toRowDataOps
import org.joda.time.LocalDateTime
import org.mockito.MockitoSugar
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RowDataOpsSpec
  extends AnyFlatSpec
    with BeforeAndAfterEach
    with Matchers
    with MockitoSugar {

  private val rowData = mock[RowData]
  private val dateTime = LocalDateTime.now()

  override def beforeEach() {
    reset(rowData)
  }

  "rowData" should "return 5 when ask as by column number" in {
    when(rowData.getAs[Int](0)).thenReturn(5)

    val result = rowData.as[Int](0)
    verify(rowData, times(1)).getAs[Int](0)
    result should be(5)
  }

  "rowData" should "return 10 when ask as by column name" in {
    when(rowData.getAs[Int]("test")).thenReturn(10)

    val result = rowData.as[Int]("test")
    verify(rowData, times(1)).getAs[Int]("test")
    result should be(10)
  }

  "rowData" should "return true when ask boolean by column number" in {
    when(rowData.getBoolean(0)).thenReturn(true)

    val result = rowData.boolean(0)
    verify(rowData, times(1)).getBoolean(0)
    result should be(true)
  }

  "rowData" should "return false when ask boolean by column name" in {
    when(rowData.getBoolean("test")).thenReturn(false)

    val result = rowData.boolean("test")
    verify(rowData, times(1)).getBoolean("test")
    result should be(false)
  }

  "rowData" should "return 0 when ask byte by column number" in {
    when(rowData.getByte(0)).thenReturn(0.toByte)

    val result = rowData.byte(0)
    verify(rowData, times(1)).getByte(0)
    result should be(0)
  }

  "rowData" should "return 1 when ask byte by column name" in {
    when(rowData.getByte("test")).thenReturn(1.toByte)

    val result = rowData.byte("test")
    verify(rowData, times(1)).getByte("test")
    result should be(1)
  }

  "rowData" should "return dateTime when ask date by column number" in {
    when(rowData.getDate(0)).thenReturn(dateTime)

    val result = rowData.date(0)
    verify(rowData, times(1)).getDate(0)
    result should be(dateTime)
  }

  "rowData" should "return dateTime when ask date by column name" in {
    when(rowData.getDate("test")).thenReturn(dateTime)

    val result = rowData.date("test")
    verify(rowData, times(1)).getDate("test")
    result should be(dateTime)
  }

  "rowData" should "return 0.1 when ask double by column number" in {
    when(rowData.getDouble(0)).thenReturn(0.1)

    val result = rowData.double(0)
    verify(rowData, times(1)).getDouble(0)
    result should be(0.1)
  }

  "rowData" should "return 2.1 when ask double by column name" in {
    when(rowData.getDouble("test")).thenReturn(2.1)

    val result = rowData.double("test")
    verify(rowData, times(1)).getDouble("test")
    result should be(2.1)
  }

  "rowData" should "return 1.1 when ask float by column number" in {
    when(rowData.getFloat(0)).thenReturn(1.1F)

    val result = rowData.float(0)
    verify(rowData, times(1)).getFloat(0)
    result should be(1.1F)
  }

  "rowData" should "return 3.1 when ask double by column name" in {
    when(rowData.getFloat("test")).thenReturn(3.1F)

    val result = rowData.float("test")
    verify(rowData, times(1)).getFloat("test")
    result should be(3.1F)
  }

  "rowData" should "return 1 when ask int by column number" in {
    when(rowData.getInt(0)).thenReturn(1)

    val result = rowData.int(0)
    verify(rowData, times(1)).getInt(0)
    result should be(1)
  }

  "rowData" should "return 3 when ask int by column name" in {
    when(rowData.getInt("test")).thenReturn(3)

    val result = rowData.int("test")
    verify(rowData, times(1)).getInt("test")
    result should be(3)
  }

  "rowData" should "return 5L when ask long by column number" in {
    when(rowData.getLong(0)).thenReturn(5L)

    val result = rowData.long(0)
    verify(rowData, times(1)).getLong(0)
    result should be(5L)
  }

  "rowData" should "return 7 when ask long by column name" in {
    when(rowData.getLong("test")).thenReturn(7L)

    val result = rowData.long("test")
    verify(rowData, times(1)).getLong("test")
    result should be(7L)
  }

  "rowData" should "return string1 when ask string by column number" in {
    when(rowData.getString(0)).thenReturn("string1")

    val result = rowData.string(0)
    verify(rowData, times(1)).getString(0)
    result should be("string1")
  }

  "rowData" should "return string2 when ask string by column name" in {
    when(rowData.getString("test")).thenReturn("string2")

    val result = rowData.string("test")
    verify(rowData, times(1)).getString("test")
    result should be("string2")
  }
}

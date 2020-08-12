package com.example

import java.io.{File, FileOutputStream, OutputStreamWriter}
import java.util.zip.{ZipEntry, ZipOutputStream}

import akka.Done
import akka.actor.ActorSystem
import au.com.bytecode.opencsv.CSVWriter
import slick.driver.H2Driver
import slick.driver.H2Driver.api._
import slick.jdbc.{GetResult, PositionedResult}

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

final case class Query(query: String, fileName: String)

object QueryRegistry {
  // needed to run the route
  implicit val system = ActorSystem()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  def executeQuery(db: H2Driver.backend.DatabaseDef,query:Query,limitSize: Option[Int]):Future[Done] = {
    val queryString:String = {
      if (!query.query.contains("limit") && limitSize.isDefined) s"${query.query} limit ${limitSize.get};"
      else query.query
    }
    println(s"got query '${queryString}' to store to '${query.fileName}'")
    val queryResult = sql"#$queryString".as(ResultMap)

    Await.result(db.run(queryResult).map((result: Seq[Map[String, String]]) =>{
      val csvData: List[Array[String]] = toCSV(result)
      val fileName = query.fileName.replace("csv", "zip")
      val zos = new ZipOutputStream(new FileOutputStream(fileName))
      zos.putNextEntry(new ZipEntry(new File(query.fileName).getName)); // create a zip entry and add it to ZipOutputStream

      val writer = new CSVWriter(new OutputStreamWriter(zos))
      writer.writeAll(csvData.asJava)
      writer.flush()
      zos.closeEntry()
      zos.close() // finally closing the ZipOutputStream to mark completion of ZIP file
    }), Duration.Inf)

    Future { Done }
  }

  private def toCSV(list: Seq[Map[String, String]]):List[Array[String]] = {
    val headers: Array[String] = list.flatMap((map) => map.keySet).distinct.toArray
    val rows: Seq[Array[String]] = list.map(map => map.values.toArray)
    headers :: rows.toList
  }
}

object ResultMap extends GetResult[Map[String, String]] {
  def apply(pr: PositionedResult) = {
    val resultSet = pr.rs
    val metaData = resultSet.getMetaData();
    (1 to pr.numColumns).map { i =>
      metaData.getColumnName(i) -> resultSet.getString(i) //getObject
    }.toMap
  }
}
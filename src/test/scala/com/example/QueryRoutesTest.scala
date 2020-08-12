package com.example

import java.util.Properties

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import slick.driver.H2Driver
import slick.driver.H2Driver.api._

class QueryRoutesTest extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  val prop:Properties = new Properties()
  prop.setProperty("numThreads","10")
  prop.setProperty("connectionPool","disabled")
  prop.setProperty("keepAliveConnection","true")
  lazy val db: H2Driver.backend.DatabaseDef = Database.forURL("jdbc:postgresql://hh-pgsql-public.ebi.ac.uk:5432/pfmegrnargs","reader","NWDMCE5xdipIjRrp",prop,"org.postgresql.Driver")
  lazy val routes = QueryRoutes.queryRoutes(db,Option.empty)

  //#actual-test
  "QueryRoutes" should {
    "execute query" in {
      val queryEntity = "{\"query\":\"SELECT * FROM rnacen.rna ORDER BY upi ASC LIMIT 100\",\"fileName\":\"C:/Users/luba/Downloads/testdata.csv\"}";
      val entity = HttpEntity(ContentType(MediaTypes.`application/json`), queryEntity)

      // using the RequestBuilding DSL:
      val request = Post("/execute-query").withEntity(entity)
      request ~> routes ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] shouldEqual "query executed"
      }
    }
  }
}

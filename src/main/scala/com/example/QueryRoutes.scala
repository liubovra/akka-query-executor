package com.example

import akka.Done
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import slick.driver.H2Driver

import scala.concurrent.Future

object QueryRoutes {

  //#user-routes-class
  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#import-json-formats

  def queryRoutes(db: H2Driver.backend.DatabaseDef,limitSize: Option[Int]): Route =
    post {
      path("execute-query") {
        entity(as[Query]) { query =>
          val saved: Future[Done] = QueryRegistry.executeQuery(db,query,limitSize)
          onComplete(saved) { done =>
            complete("query executed")
          }
        }
      }
    }
}

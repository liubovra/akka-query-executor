package com.example

import java.util.Properties

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import slick.driver.H2Driver
import slick.driver.H2Driver.api._

import scala.io.StdIn

object StartupApp {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def main(args: Array[String]) {
    val prop:Properties = new Properties()
    prop.setProperty("numThreads","10")
    prop.setProperty("connectionPool","disabled")
    prop.setProperty("keepAliveConnection","true")

    //lazy val db: H2Driver.backend.DatabaseDef = Database.forURL("jdbc:postgresql://hh-pgsql-public.ebi.ac.uk:5432/pfmegrnargs","reader","NWDMCE5xdipIjRrp",prop,"org.postgresql.Driver")
    lazy val db: H2Driver.backend.DatabaseDef = Database.forURL(args(0),args(1),args(2),prop,args(3))
    println(s"connected to db ${args(0)} with user ${args(1)} and password ${args(2)} using driver ${args(3)}")

    val limitSize: Option[Int] = if (args.size==5) Option(args(4).toInt) else Option.empty;
    Http().bindAndHandle(QueryRoutes.queryRoutes(db,limitSize), "0.0.0.0", 1234)
    println(s"Server online at http://0.0.0.0:1234/")

  }
}

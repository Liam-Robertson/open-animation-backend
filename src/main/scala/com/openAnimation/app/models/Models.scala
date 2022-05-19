package com.openAnimation.app.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val snippetFormat: RootJsonFormat[Snippet] = jsonFormat3(Snippet)
}

case class Snippet(startTime: String, endTime: String, image: String)

package com.terway.base

import play.api.libs.json.Json

case class ShortenRequest(target: String)

object ShortenRequest {
  implicit val format = Json.format[ShortenRequest]
}

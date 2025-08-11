package com.zestia.capsulemcp.model

import zio.json.*

case class Website(
    id: Long,
    service: String,
    address: String,
    `type`: Option[String],
    url: String
) derives JsonDecoder,
      JsonEncoder

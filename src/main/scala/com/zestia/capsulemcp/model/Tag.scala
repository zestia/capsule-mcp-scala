package com.zestia.capsulemcp.model

import zio.json.*

case class Tag(
    id: Long,
    name: String,
    description: Option[String]
) derives JsonDecoder,
      JsonEncoder

package com.zestia.capsulemcp.model

import zio.json.*

case class EmailAddress(
    id: Long,
    `type`: Option[String],
    address: String
) derives JsonDecoder,
      JsonEncoder

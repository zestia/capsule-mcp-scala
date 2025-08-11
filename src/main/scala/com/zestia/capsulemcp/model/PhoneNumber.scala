package com.zestia.capsulemcp.model

import zio.json.*

case class PhoneNumber(
    id: Long,
    `type`: Option[String],
    number: String
) derives JsonDecoder,
      JsonEncoder

package com.zestia.capsulemcp.model

import zio.json.*

case class Team(
    id: Long,
    name: Option[String]
) derives JsonDecoder,
      JsonEncoder

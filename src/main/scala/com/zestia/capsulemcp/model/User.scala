package com.zestia.capsulemcp.model

import zio.json.*

case class User(
    id: Long,
    username: String,
    name: String
) derives JsonDecoder,
      JsonEncoder

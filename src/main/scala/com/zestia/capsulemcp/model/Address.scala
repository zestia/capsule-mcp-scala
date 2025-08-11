package com.zestia.capsulemcp.model

import zio.json.*

case class Address(
    id: Long,
    `type`: String,
    street: Option[String],
    city: Option[String],
    state: Option[String],
    country: Option[String],
    zip: Option[String]
) derives JsonDecoder, JsonEncoder
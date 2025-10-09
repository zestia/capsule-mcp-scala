package com.zestia.capsulemcp.model

import zio.json.*

case class FieldDefinition(
    id: Long,
    name: String,
    `type`: Option[String],
    description: Option[String],
    important: Option[Boolean],
    tag: Option[Tag],
    options: Option[List[String]],
    captureRule: Option[String]
) derives JsonDecoder,
      JsonEncoder

package com.zestia.capsulemcp.model

import zio.json.*

case class Tag(
    id: Long,
    name: String,
    description: Option[String],
    dataTag: Boolean,
    definitions: Option[List[FieldDefinition]]
) derives JsonDecoder,
      JsonEncoder

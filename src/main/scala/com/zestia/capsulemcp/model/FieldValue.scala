package com.zestia.capsulemcp.model

import zio.json.*

case class FieldValue(
    id: Long,
    value: String,
    definition: FieldDefinition
) derives JsonDecoder,
      JsonEncoder

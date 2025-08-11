package com.zestia.capsulemcp.model

import zio.json.*

case class FieldDefinition(
    id: String,
    name: String
) derives JsonDecoder,
      JsonEncoder

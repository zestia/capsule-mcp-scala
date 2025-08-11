package com.zestia.capsulemcp.model

import zio.json.*

case class ContactResponse(parties: List[Party], meta: Meta)
    derives JsonDecoder,
      JsonEncoder

case class FieldDefinitionsResponse(definitions: List[FieldDefinition])
    derives JsonDecoder,
      JsonEncoder

package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.{FilterField, ValueType}

trait HasCustomFieldFilterFields:

  // Custom field patterns - one for each value type to ensure correct operators
  val customFieldFilterFields: List[SchemaTypes.FilterField] = List(
    FilterField(
      "^custom:\\d+$",
      ValueType.Boolean,
      "Filter on a boolean custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true,
      mandatory = true
    ),
    FilterField(
      "^custom:\\d+$",
      ValueType.Date,
      "Filter on a date custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    ),
    FilterField(
      "^custom:\\d+$",
      ValueType.Number,
      "Filter on a number custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    ),
    FilterField(
      "^custom:\\d+$",
      ValueType.String,
      "Filter on a string custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    )
  )

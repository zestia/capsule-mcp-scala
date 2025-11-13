package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*

trait HasCustomFieldFilterFields:

  // Custom field patterns - one for each value type to ensure correct operators
  val customFieldFilterFields: List[FilterField] = List(
    ExactMatchFilterField(
      "^custom:\\d+$",
      "Filter on a boolean custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Boolean,
      mandatory = true,
      isPattern = true
    ),
    ExactMatchFilterField(
      "^custom:\\d+$",
      "Filter on a date custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Date,
      isPattern = true
    ),
    ExactMatchFilterField(
      "^custom:\\d+$",
      "Filter on a number custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Number,
      isPattern = true
    ),
    ExactMatchFilterField(
      "^custom:\\d+$",
      "Filter on a string custom field by field definition ID (format = custom:{fieldId})",
      ValueType.String,
      isPattern = true
    )
  )

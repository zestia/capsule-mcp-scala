/*
 * Copyright 2026 Zestia Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/*
 * Copyright 2025 Zestia Ltd
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

/**
 * JSON Schemas for Activity-related Tools
 */
object ActivitySchemas:

  /**
   * Valid filter fields for activities
   */
  private val activityFilterFields: List[FilterField] = List(
    FilterField(
      name = "user",
      valueType = ValueType.Number,
      description = "User ID"
    ),
    FilterField(
      name = "taskCategory",
      valueType = ValueType.Number,
      description = "Task Category ID"
    ),
    FilterField(
      name = "activityType",
      valueType = ValueType.Number,
      description = "Activity Type ID"
    ),
    FilterField(
      name = "addedOn",
      valueType = ValueType.Date,
      description = "Date the activity was added (format: YYYY-MM-DD)"
    )
  )

  /**
   * Schema for the list_activities tool
   */
  val activityFilterSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> paginationSchema,
      "filter" -> buildFilterSchema(activityFilterFields)
    )
  )

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
 * JSON Schemas for Project-related Tools
 */
object ProjectSchemas extends HasCustomFieldFilterFields:

  /**
   * Valid filter fields for opportunities. See https://developer.capsulecrm.com/v2/reference/filters#field-reference
   */
  private val projectFilterFields: List[FilterField] = List(
    FilterField("id", ValueType.Number, "Project ID"),
    FilterField("name", ValueType.String, "Project name"),
    FilterField("status", ValueType.String, "Project status (open, closed)"),
    FilterField("primaryContact", ValueType.Number, "Primary contact ID"),
    FilterField("primaryContactName", ValueType.String, "Primary contact name"),
    FilterField("board", ValueType.String, "Board name"),
    FilterField("board", ValueType.Number, "Board ID"),
    FilterField("stage", ValueType.String, "Stage name"),
    FilterField("stage", ValueType.Number, "Stage ID"),
    FilterField("tag", ValueType.Number, "Tag ID"),
    FilterField("tag", ValueType.String, "Tag name"),
    FilterField("owner", ValueType.String, "Owner name"),
    FilterField("owner", ValueType.Number, "Owner ID"),
    FilterField("team", ValueType.String, "Team name"),
    FilterField("team", ValueType.Number, "Team ID"),
    FilterField("isOpen", ValueType.Boolean, "Is open", mandatory = true),
    FilterField("hasTags", ValueType.Boolean, "Has tags", mandatory = true),
    FilterField("hasOpenTasks", ValueType.Boolean, "Has open tasks", mandatory = true),
    FilterField("addedOn", ValueType.Date, "Date added"),
    FilterField("updatedOn", ValueType.Date, "Date updated"),
    FilterField("closedOn", ValueType.Date, "Date closed on"),
    FilterField("expectedCloseOn", ValueType.Date, "Expected date close on"),
    FilterField("startOn", ValueType.Date, "Start on date")
  ) ++ customFieldFilterFields

  /**
   * Schema for the list_opportunities tool
   */
  val projectFilterSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> paginationSchema,
      "filter" -> buildFilterSchema(projectFilterFields)
    )
  )

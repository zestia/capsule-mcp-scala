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

/**
 * JSON Schemas for Task-related Tools
 */
object TaskSchemas:

  /**
   * Schema for the list_tasks tool
   */
  val listTasksSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> SchemaTypes.paginationSchema,
      "since" -> SchemaBuilders.dateTime("Filter Tasks updated since this datetime"),
      "status" -> SchemaBuilders.arrayOfEnum(
        "Filter Tasks by one or more statuses",
        List("open", "completed", "pending")
      ),
      "owner" -> SchemaBuilders.integer("Filter Tasks by owner user ID"),
      "category" -> SchemaBuilders.integer("Filter Tasks by Task Category ID"),
      "dueFrom" -> SchemaBuilders.dateTime("Filter Tasks due from this datetime"),
      "dueTo" -> SchemaBuilders.dateTime("Filter Tasks due to this datetime"),
      "repeating" -> SchemaBuilders.boolean("Filter repeating or non-repeating Tasks"),
      "relatedTo" -> SchemaBuilders.arrayOfEnum(
        "Filter by one or more entity types. 'kase' = internal name for a Project",
        List("party", "opportunity", "kase")
      )
    )
  )

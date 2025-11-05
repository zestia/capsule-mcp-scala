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

package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.macros.MapToFunctionMacro
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.server.tools.common.{FilterOperators, SchemaBuilders, SchemaTypes}
import com.zestia.capsulemcp.service.CapsuleHttpClient.filterRequest
import zio.*
import zio.json.*

object ActivityTools extends HasManualTools:

  /**
   * Define the valid filter fields for activities
   */
  private val activityFilterFields = List(
    SchemaTypes.FilterField(
      name = "user",
      valueType = "number",
      operators = FilterOperators.numberOperators,
      description = "User ID"
    ),
    SchemaTypes.FilterField(
      name = "taskCategory",
      valueType = "number",
      operators = FilterOperators.numberOperators,
      description = "Task Category ID"
    ),
    SchemaTypes.FilterField(
      name = "activityType",
      valueType = "number",
      operators = FilterOperators.numberOperators,
      description = "Activity Type ID"
    ),
    SchemaTypes.FilterField(
      name = "addedOn",
      valueType = "date",
      operators = FilterOperators.dateOperators,
      description = "Date the activity was added (format: YYYY-MM-DD)"
    )
  )

  /**
   * Schema for the search_activities tool
   */
  private val searchActivitiesSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> SchemaTypes.pagination,
      "filter" -> SchemaTypes.filterWithFields(activityFilterFields)
    )
  )

  private def searchActivities(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[ActivitiesResponse](
      "activities/filters/results",
      filter,
      pagination.getOrElse(Pagination()),
      embed = List("entry", "task")
    ).toJson

  /**
   * Register manual tools that need custom schemas
   */
  override def registerManualTools(server: FastMcpServer): ZIO[Any, Throwable, Unit] =
    for
      // Register search_activities
      _ <- {
        server.tool(
          name = "search_activities",
          description = Some("Perform a search of Activities"),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(searchActivities)(args)),
          inputSchema = Right(searchActivitiesSchema)
        )
      }
    yield ()

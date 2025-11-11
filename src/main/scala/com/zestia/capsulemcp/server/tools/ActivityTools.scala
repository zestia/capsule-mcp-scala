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
import com.zestia.capsulemcp.server.schemas.ActivitySchemas
import com.zestia.capsulemcp.service.CapsuleHttpClient.filterRequest
import zio.*
import zio.json.*

object ActivityTools extends HasManualTools:

  private def listActivities(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[ActivityListWrapper](
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
      // Register list_activities
      _ <- {
        server.tool(
          name = "list_activities",
          description = Some("Retrieve Activities with basic filtering ability"),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listActivities)(args)),
          inputSchema = Right(ActivitySchemas.activityFilterSchema)
        )
      }
    yield ()

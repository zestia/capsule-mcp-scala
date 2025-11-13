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

package com.zestia.capsulemcp.server

import com.tjclp.fastmcp.macros.MapToFunctionMacro
import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.*
import com.zestia.capsulemcp.model.filter.Condition.*
import com.zestia.capsulemcp.server.schemas.*
import com.zestia.capsulemcp.server.tools.*
import com.zestia.capsulemcp.server.tools.ActivityTools.listActivities
import com.zestia.capsulemcp.server.tools.ContactTools.*
import com.zestia.capsulemcp.server.tools.ProjectTools.*
import com.zestia.capsulemcp.server.tools.TaskTools.listTasks
import com.zestia.capsulemcp.server.tools.OpportunityTools.*
import com.zestia.capsulemcp.service.CapsuleHttpClient.*
import com.zestia.capsulemcp.util.{FileLogger, FileLogging}
import sttp.tapir.generic.auto.*
import zio.*
import zio.json.*

object McpServer extends ZIOAppDefault with FileLogging:

  override def run =
    for
      _ <- ZIO.attempt {
        FileLogger.init()
        logger.info("MCP Server starting...")
      }
      // Create server instance
      server <- ZIO.succeed(FastMcpServer("McpServer"))
      // Process tools using the scanAnnotations macro extension method
      _ <- ZIO.attempt {
        // This macro finds all methods with @Tool, @Prompt or @Resource annotations
        // and registers them with the server
        server
          .scanAnnotations[OpportunityTools.type]
          .scanAnnotations[ContactTools.type]
          .scanAnnotations[ProjectTools.type]
          .scanAnnotations[CustomFieldTools.type]
          .scanAnnotations[TagTools.type]
          .scanAnnotations[UserTools.type]
          .scanAnnotations[TeamTools.type]
          .scanAnnotations[TrackTools.type]
          .scanAnnotations[ActivityTools.type]
      }
      // Manually register tools with custom schemas
      _ <- server.tool(
        name = "list_tasks",
        description = Some(
          "List Tasks with basic filtering ability. By default only Tasks with a status of 'OPEN' are returned."
        ),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listTasks)(args)),
        inputSchema = Right(TaskSchemas.listTasksSchema)
      )
      _ <- server.tool(
        name = "list_activities",
        description = Some("List Activities with basic filtering ability"),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listActivities)(args)),
        inputSchema = Right(ActivitySchemas.filterSchema)
      )
      _ <- {
        server.tool(
          name = "list_contacts",
          description = Some("List Contacts with comprehensive filtering ability"),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listContacts)(args)),
          inputSchema = Right(ContactSchemas.filterSchema)
        )
      }
      _ <- {
        server.tool(
          name = "list_projects",
          description = Some(
            "List Projects with comprehensive filtering ability. Any references to kase/case are internal/legacy names for Projects"
          ),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listProjects)(args)),
          inputSchema = Right(ProjectSchemas.filterSchema)
        )
      }
      _ <- {
        server.tool(
          name = "list_opportunities",
          description = Some("List Opportunities with comprehensive filtering ability"),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listOpportunities)(args)),
          inputSchema = Right(OpportunitySchemas.filterSchema)
        )
      }
      _ <- {
        server.tool(
          name = "calculate_value_of_opportunities",
          description = Some("Get Total & Projected Values for Opportunities with comprehensive filtering ability"),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(calculateValueOfOpportunities)(args)),
          inputSchema = Right(OpportunitySchemas.filterSchema)
        )
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

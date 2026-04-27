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

package com.zestia.capsulemcp.server

import com.tjclp.fastmcp.ToolInputSchema
import com.tjclp.fastmcp.core.ToolAnnotations
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
import com.zestia.capsulemcp.util.{FileLogger, FileLogging, Version}
import sttp.tapir.generic.auto.*
import zio.*

trait BaseMcpServer:
  protected[server] def writeToolsEnabled: Boolean

  private[server] def registerAnnotatedTools(server: FastMcpServer): Unit =
    server.scanAnnotations[OpportunityTools.type]
    server.scanAnnotations[ContactTools.type]
    server.scanAnnotations[ProjectTools.type]
    server.scanAnnotations[CustomFieldTools.type]
    server.scanAnnotations[TagTools.type]
    server.scanAnnotations[UserTools.type]
    server.scanAnnotations[TeamTools.type]
    server.scanAnnotations[TrackTools.type]
    server.scanAnnotations[ActivityTools.type]

  private val readOnlyAnnotations = Some(
    ToolAnnotations(
      readOnlyHint = Some(true),
      idempotentHint = Some(true),
      destructiveHint = Some(false),
      openWorldHint = Some(true)
    )
  )

  private val writeAnnotations = Some(
    ToolAnnotations(
      readOnlyHint = Some(false),
      idempotentHint = Some(true),
      destructiveHint = Some(true),
      openWorldHint = Some(true)
    )
  )

  private[server] def registerManualTools(server: FastMcpServer): ZIO[Any, Throwable, Unit] =
    for
      _ <- server.tool(
        name = "list_tasks",
        description = Some(
          "List Tasks with basic filtering ability. By default only Tasks with a status of 'OPEN' are returned."
        ),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listTasks)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(TaskSchemas.listTasksSchema),
        annotations = readOnlyAnnotations
      )
      _ <- server.tool(
        name = "list_activities",
        description = Some("List Activities with basic filtering ability"),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listActivities)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(ActivitySchemas.filterSchema),
        annotations = readOnlyAnnotations
      )
      _ <- server.tool(
        name = "list_contacts",
        description = Some("List Contacts with comprehensive filtering ability"),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listContacts)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(ContactSchemas.filterSchema),
        annotations = readOnlyAnnotations
      )
      _ <- server.tool(
        name = "list_projects",
        description = Some(
          "List Projects with comprehensive filtering ability. Any references to kase/case are internal/legacy names for Projects"
        ),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listProjects)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(ProjectSchemas.filterSchema),
        annotations = readOnlyAnnotations
      )
      _ <- server.tool(
        name = "list_opportunities",
        description = Some("List Opportunities with comprehensive filtering ability"),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(listOpportunities)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(OpportunitySchemas.filterSchema),
        annotations = readOnlyAnnotations
      )
      _ <- server.tool(
        name = "calculate_value_of_opportunities",
        description = Some("Get Total & Projected Values for Opportunities with comprehensive filtering ability"),
        handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(calculateValueOfOpportunities)(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(OpportunitySchemas.filterSchema),
        annotations = readOnlyAnnotations
      )
      _ <- ZIO.when(writeToolsEnabled) {
        server.tool(
          name = "update_contact",
          description = Some(
            "Update a Contact. Only adding/updating custom fields is currently supported. Deleting is NOT allowed but updates will overwrite existing values."
          ),
          handler = (args, _) => ZIO.succeed(MapToFunctionMacro.callByMap(updateContact)(args)),
          inputSchema = ToolInputSchema.unsafeFromJsonString(ContactSchemas.updateContactSchema),
          annotations = writeAnnotations
        )
      }
    yield ()

object McpServer extends ZIOAppDefault with FileLogging with BaseMcpServer:

  override protected[server] def writeToolsEnabled: Boolean =
    sys.env.get("ENABLE_WRITE_TOOLS").contains("true")

  override def run: ZIO[Any, Throwable, Unit] =
    for
      _ <- ZIO.attempt {
        FileLogger.init()
        logger.info(s"MCP Server starting... version ${Version.current}")
      }
      // Create server instance
      server <- ZIO.succeed(FastMcpServer("CapsuleMcpServer", Version.current))
      // Process tools using the scanAnnotations macro extension method
      _ <- ZIO.attempt {
        registerAnnotatedTools(server)
      }
      // Manually register tools with custom schemas
      _ <- registerManualTools(server)
      // Run the server
      _ <- server.runStdio()
    yield ()

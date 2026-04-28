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
import com.zestia.capsulemcp.server.tools.ProjectTools.{listProjects, updateProject}
import com.zestia.capsulemcp.server.tools.TaskTools.listTasks
import com.zestia.capsulemcp.server.tools.OpportunityTools.{
  calculateValueOfOpportunities,
  listOpportunities,
  updateOpportunity
}
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

  private def registerManualTool(
      server: FastMcpServer,
      name: String,
      description: String,
      fn: Map[String, Any] => Any,
      schema: String,
      annotations: Option[ToolAnnotations]
  ): ZIO[Any, Throwable, Unit] =
    server
      .tool(
        name = name,
        description = Some(description),
        handler = (args, _) => ZIO.succeed(fn(args)),
        inputSchema = ToolInputSchema.unsafeFromJsonString(schema),
        annotations = annotations
      )
      .unit

  private[server] def registerManualTools(server: FastMcpServer): ZIO[Any, Throwable, Unit] =
    for
      _ <- registerManualTool(
        server,
        "list_tasks",
        "List Tasks with basic filtering ability. By default only Tasks with a status of 'OPEN' are returned.",
        MapToFunctionMacro.callByMap(listTasks),
        TaskSchemas.listTasksSchema,
        readOnlyAnnotations
      )
      _ <- registerManualTool(
        server,
        "list_activities",
        "List Activities with basic filtering ability",
        MapToFunctionMacro.callByMap(listActivities),
        ActivitySchemas.filterSchema,
        readOnlyAnnotations
      )
      _ <- registerManualTool(
        server,
        "list_contacts",
        "List Contacts with comprehensive filtering ability",
        MapToFunctionMacro.callByMap(listContacts),
        ContactSchemas.filterSchema,
        readOnlyAnnotations
      )
      _ <- registerManualTool(
        server,
        "list_projects",
        "List Projects with comprehensive filtering ability. Any references to kase/case are internal/legacy names for Projects",
        MapToFunctionMacro.callByMap(listProjects),
        ProjectSchemas.filterSchema,
        readOnlyAnnotations
      )
      _ <- registerManualTool(
        server,
        "list_opportunities",
        "List Opportunities with comprehensive filtering ability",
        MapToFunctionMacro.callByMap(listOpportunities),
        OpportunitySchemas.filterSchema,
        readOnlyAnnotations
      )
      _ <- registerManualTool(
        server,
        "calculate_value_of_opportunities",
        "Get Total & Projected Values for Opportunities with comprehensive filtering ability",
        MapToFunctionMacro.callByMap(calculateValueOfOpportunities),
        OpportunitySchemas.filterSchema,
        readOnlyAnnotations
      )
      _ <- ZIO.when(writeToolsEnabled) {
        for
          _ <- registerManualTool(
            server,
            "update_contact",
            "Update a Contact. Only adding/updating custom fields is currently supported. Deleting is NOT allowed but updates will overwrite existing values.",
            MapToFunctionMacro.callByMap(updateContact),
            ContactSchemas.updateContactSchema,
            writeAnnotations
          )
          _ <- registerManualTool(
            server,
            "update_opportunity",
            "Update an Opportunity. Only adding/updating custom fields is currently supported. Deleting is NOT allowed but updates will overwrite existing values.",
            MapToFunctionMacro.callByMap(updateOpportunity),
            OpportunitySchemas.updateOpportunitySchema,
            writeAnnotations
          )
          _ <- registerManualTool(
            server,
            "update_project",
            "Update a Project. Only adding/updating custom fields is currently supported. Deleting is NOT allowed but updates will overwrite existing values.",
            MapToFunctionMacro.callByMap(updateProject),
            ProjectSchemas.updateProjectSchema,
            writeAnnotations
          )
        yield ()
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

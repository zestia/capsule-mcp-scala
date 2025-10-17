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

import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.*
import com.zestia.capsulemcp.model.filter.Condition.*
import com.zestia.capsulemcp.server.tools.*
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
          .scanAnnotations[ActivityTools.type]
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

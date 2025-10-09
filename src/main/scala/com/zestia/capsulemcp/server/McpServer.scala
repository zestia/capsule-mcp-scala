package com.zestia.capsulemcp.server

import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.*
import com.zestia.capsulemcp.model.filter.SimpleCondition.*
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
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

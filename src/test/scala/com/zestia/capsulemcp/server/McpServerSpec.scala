package com.zestia.capsulemcp.server

import com.tjclp.fastmcp.server.*
import io.modelcontextprotocol.spec.McpSchema
import zio.test._
import zio.test.Assertion._
import zio.*

import scala.jdk.CollectionConverters.*

object McpServerSpec extends ZIOSpecDefault {

  def spec: Spec[Any, Throwable] = suite("McpServer")(
    test("registerAnnotatedTools should register all annotated Tools") {
      for {
        server <- ZIO.succeed(FastMcpServer("TestServer"))
        _ <- ZIO.succeed(McpServer.registerAnnotatedTools(server))
        toolsResult <- server.listTools()
        tools = toolsResult.tools().asScala.toList
      } yield assertTrue(tools.size == 43)
    },
    test("registerManualTools should register all manual Tools") {
      for {
        server <- ZIO.succeed(FastMcpServer("TestServer"))
        _ <- McpServer.registerManualTools(server)
        toolsResult <- server.listTools()
        tools = toolsResult.tools().asScala.toList
      } yield assertTrue(tools.size == 6)
    }
  )
}

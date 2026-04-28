package com.zestia.capsulemcp.server

import com.tjclp.fastmcp.server.*
import io.modelcontextprotocol.spec.McpSchema
import zio.test._
import zio.test.Assertion._
import zio.*

import scala.jdk.CollectionConverters.*

object McpServerSpec extends ZIOSpecDefault {

  // TODO: introduce proper integration tests
  def spec: Spec[Any, Throwable] = suite("McpServer")(
    test("registerAnnotatedTools should register all annotated Tools") {
      for {
        server <- ZIO.succeed(FastMcpServer("TestServer"))
        _ <- ZIO.succeed(new TestMcpServer(writeToolsEnabled = false).registerAnnotatedTools(server))
        toolsResult <- server.listTools()
        tools = toolsResult.tools().asScala.toList
      } yield assertTrue(tools.size == 43)
    },
    test("registerManualTools should register all manual Tools") {
      for {
        server <- ZIO.succeed(FastMcpServer("TestServer"))
        _ <- new TestMcpServer(writeToolsEnabled = false).registerManualTools(server)
        toolsResult <- server.listTools()
        tools = toolsResult.tools().asScala.toList
      } yield assertTrue(tools.size == 6)
    },
    test("registerManualTools should register write tools when enabled") {
      for {
        server <- ZIO.succeed(FastMcpServer("TestServer"))
        _ <- new TestMcpServer(writeToolsEnabled = true).registerManualTools(server)
        toolsResult <- server.listTools()
        tools = toolsResult.tools().asScala.toList
      } yield assertTrue(tools.size == 9) &&
        assertTrue(tools.exists(_.name() == "update_contact")) &&
        assertTrue(tools.exists(_.name() == "update_opportunity")) &&
        assertTrue(tools.exists(_.name() == "update_project"))
    }
  )

  private class TestMcpServer(override protected[server] val writeToolsEnabled: Boolean) extends BaseMcpServer
}

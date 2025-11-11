package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.server.FastMcpServer
import zio.ZIO

trait HasManualTools:
  def registerManualTools(server: FastMcpServer): ZIO[Any, Throwable, Unit]

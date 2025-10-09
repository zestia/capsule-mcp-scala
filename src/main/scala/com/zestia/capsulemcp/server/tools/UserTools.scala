package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.Tool
import com.zestia.capsulemcp.model.UsersResponse
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object UserTools:

  @Tool(
    name = Some("list_users"),
    description = Some("List Users")
  )
  def listUsers(): String =
    getRequest[UsersResponse](
      "users"
    ).toJson

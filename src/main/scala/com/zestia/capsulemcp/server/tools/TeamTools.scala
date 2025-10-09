package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.Tool
import com.zestia.capsulemcp.model.{TeamsResponse, UsersResponse}
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TeamTools:

  @Tool(
    name = Some("list_teams"),
    description = Some("List Teams and team members")
  )
  def listTeams(): String =
    getRequest[TeamsResponse](
      "teams",
      embed = List("memberships")
    ).toJson

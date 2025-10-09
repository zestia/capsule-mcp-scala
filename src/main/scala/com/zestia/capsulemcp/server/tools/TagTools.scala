package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.{Pagination, TagDefinitionsResponse}
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TagTools:

  @Tool(
    name = Some("list_contact_tags"),
    description = Some("List Tags defined for Contacts")
  )
  def listContactTags(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse](
      "parties/tags",
      pagination
    ).toJson

  @Tool(
    name = Some("list_opportunity_tags"),
    description = Some("List Tags defined for Opportunities")
  )
  def listOpportunityTags(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse](
      "opportunities/tags",
      pagination
    ).toJson

  @Tool(
    name = Some("list_project_tags"),
    description = Some("List Tags defined for Projects")
  )
  def listProjectTags(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse](
      "kases/tags",
      pagination
    ).toJson

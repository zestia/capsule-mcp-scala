package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.model.{Pagination, ProjectsResponse}
import com.zestia.capsulemcp.server.tools.ToolDescriptionHelper.*
import com.zestia.capsulemcp.service.CapsuleHttpClient.filterRequest
import zio.json.*

object ProjectTools:

  @Tool(
    name = Some("describe_search_projects"),
    description = Some(
      "Returns a detailed description of how to use the `search_projects` tool."
    )
  )
  def describeSearchProjects(): String =
    searchToolDescription("projects", projectFieldReference)

  @Tool(
    name = Some("search_projects"),
    description = Some(
      "Perform a search of Projects. Refer to `describe_search_projects` for tool description and usage"
    )
  )
  def searchProjects(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[ProjectsResponse](
      "kases/filters/results",
      filter,
      pagination
    ).toJson
  }

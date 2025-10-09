package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.model.{OpportunitiesResponse, Pagination}
import ToolDescriptionHelper.*
import com.zestia.capsulemcp.service.CapsuleHttpClient.filterRequest
import zio.json.*

object OpportunityTools:

  @Tool(
    name = Some("describe_search_opportunities"),
    description = Some(
      "Returns a detailed description of how to use the `search_opportunities` tool."
    )
  )
  def describeSearchOpportunities(): String =
    searchToolDescription("opportunities", opportunityFieldReference)

  @Tool(
    name = Some("search_opportunities"),
    description = Some(
      "Perform a search of Opportunities. Refer to `describe_search_opportunities` for tool description and usage"
    )
  )
  def searchOpportunities(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[OpportunitiesResponse](
      "opportunities/filters/results",
      filter,
      pagination
    ).toJson
  }

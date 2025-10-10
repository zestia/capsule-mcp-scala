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

package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.model.{OpportunitiesResponse, Pagination}
import com.zestia.capsulemcp.server.tools.common.ToolDescriptions.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
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
        ToolParams.paginationDescription,
        required = ToolParams.paginationRequired
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[OpportunitiesResponse](
      "opportunities/filters/results",
      filter,
      pagination
    ).toJson
  }

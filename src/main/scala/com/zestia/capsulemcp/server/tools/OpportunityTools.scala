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
import com.zestia.capsulemcp.model.{
  MilestonesResponse,
  OpportunitiesResponse,
  Pagination,
  PipelinesResponse
}
import com.zestia.capsulemcp.server.tools.common.ToolDescriptions.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.{
  filterRequest,
  getRequest
}
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

  @Tool(
    name = Some("list_pipelines"),
    description = Some(
      "List Sales Pipelines for Opportunities, with optional searching by name"
    )
  )
  def listPipelines(
      @Param(
        ToolParams.paginationDescription,
        required = ToolParams.paginationRequired
      ) pagination: Pagination,
      @Param("Search Pipelines by name", required = false) query: Option[
        String
      ] = None
  ): String = {
    getRequest[PipelinesResponse](
      "pipelines",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson
  }

  @Tool(
    name = Some("list_milestones"),
    description = Some(
      "List Milestones across all Sales Pipelines. To list Milestones on a specific Pipeline, use `list_milestones_by_pipeline_id`"
    )
  )
  def listMilestones(
      @Param(
        ToolParams.paginationDescription,
        required = ToolParams.paginationRequired
      ) pagination: Pagination
  ): String = {
    getRequest[MilestonesResponse](
      "milestones/all",
      pagination
    ).toJson
  }

  @Tool(
    name = Some("list_milestones_by_pipeline_id"),
    description = Some(
      "List Milestones associated with a Sales Pipeline"
    )
  )
  def listMilestonesByPipelineId(
      @Param(
        ToolParams.paginationDescription,
        required = ToolParams.paginationRequired
      ) pagination: Pagination,
      @Param("Sales Pipeline ID", required = true) pipelineId: Long
  ): String = {
    getRequest[MilestonesResponse](
      s"pipelines/$pipelineId/milestones",
      pagination
    ).toJson
  }

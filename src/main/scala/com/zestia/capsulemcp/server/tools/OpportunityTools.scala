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
  LostReasonsResponse,
  MilestonesResponse,
  OpportunitiesResponse,
  OpportunityValueResponse,
  Pagination,
  PipelinesResponse
}
import com.zestia.capsulemcp.server.tools.common.ToolDescriptions.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.{filterRequest, getRequest}
import zio.json.*

object OpportunityTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  @Tool(
    Some("describe_search_opportunities"),
    Some("Returns a detailed description of how to use the `search_opportunities` tool.")
  )
  def describeSearchOpportunities(): String =
    searchToolDescription("opportunities", opportunityFieldReference)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  @Tool(
    Some("search_opportunities"),
    Some("Perform a search of Opportunities. Refer to `describe_search_opportunities` for tool description and usage")
  )
  def searchOpportunities(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param(ToolParams.filterDescription) filter: Filter
  ): String =
    filterRequest[OpportunitiesResponse]("opportunities/filters/results", filter, pagination).toJson

  @Tool(
    Some("describe_calculate_value_of_opportunities"),
    Some("Returns a detailed description of how to use the `calculate_value_of_opportunities` tool.")
  )
  def describeCalculateValueOfOpportunities(): String = calculateValueOfOpportunitiesToolDescription

  @Tool(
    Some("calculate_value_of_opportunities"),
    Some("Get Total & Projected Values for Opportunities. See `describe_calculate_value_of_opportunities` for usage")
  )
  def calculateValueOfOpportunities(@Param(ToolParams.filterDescription) filter: Filter): String =
    filterRequest[OpportunityValueResponse]("opportunities/value", filter).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Pipeline#listPipelines"</a>
   */
  @Tool(Some("list_pipelines"), Some("List Sales Pipelines for Opportunities, with optional searching by name"))
  def listPipelines(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param("Search Pipelines by name", required = false) query: Option[String] = None
  ): String =
    getRequest[PipelinesResponse](
      "pipelines",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Milestone#listMilestones"</a>
   */
  @Tool(
    Some("list_milestones"),
    Some(
      "List Milestones across all Sales Pipelines. To list Milestones on a specific Pipeline, use `list_milestones_by_pipeline_id`"
    )
  )
  def listMilestones(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[MilestonesResponse]("milestones/all", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Milestone#listMilestonesForPipeline"</a>
   */
  @Tool(Some("list_milestones_by_pipeline_id"), Some("List Milestones associated with a Sales Pipeline"))
  def listMilestonesByPipelineId(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param("Sales Pipeline ID", required = true) pipelineId: Long
  ): String =
    getRequest[MilestonesResponse](s"pipelines/$pipelineId/milestones", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Lost_Reason#listLostReasons"</a>
   */
  @Tool(
    Some("list_lost_reasons"),
    Some(
      "List Lost Reasons, with optional searching by name. Lost Reasons allow users to record the reason an Opportunity is lost"
    )
  )
  def listLostReasons(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param("Search Lost Reasons by name", required = false) query: Option[String] = None
  ): String =
    getRequest[LostReasonsResponse](
      "lostreasons",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

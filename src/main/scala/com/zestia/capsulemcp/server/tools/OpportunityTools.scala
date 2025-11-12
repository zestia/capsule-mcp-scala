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
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.{filterRequest, getRequest}
import zio.json.*

object OpportunityTools:

  /**
   * Manually registered tool
   *
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  def listOpportunities(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[OpportunityListWrapper]("opportunities/filters/results", filter, pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Opportunity#showOpportunity"</a>
   */
  @Tool(Some("get_opportunity"), Some("Get an Opportunity by ID"))
  def getOpportunity(
      @Param("Opportunity ID") id: Long
  ): String =
    getRequest[OpportunityWrapper](s"opportunities/$id").toJson

  /**
   * Manually registered tool
   */
  def calculateValueOfOpportunities(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[OpportunityValueWrapper]("opportunities/value", filter, pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Pipeline#listPipelines"</a>
   */
  @Tool(Some("list_pipelines"), Some("List Sales Pipelines for Opportunities, with optional searching by name"))
  def listPipelines(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Search Pipelines by name", required = false) query: Option[String] = None
  ): String =
    getRequest[PipelineListWrapper](
      "pipelines",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Pipeline#showPipeline"</a>
   */
  @Tool(Some("get_pipeline"), Some("Get a Pipeline by ID"))
  def getPipeline(
      @Param("Pipeline ID") id: Long
  ): String =
    getRequest[PipelineWrapper](s"pipelines/$id").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Milestone#listMilestones"</a>
   */
  @Tool(
    Some("list_milestones"),
    Some(
      "List Milestones across all Sales Pipelines. To list Milestones on a specific Pipeline, use `list_milestones_by_pipeline`"
    )
  )
  def listMilestones(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    getRequest[MilestoneListWrapper]("milestones/all", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Milestone#showMilestone"</a>
   */
  @Tool(Some("get_milestone"), Some("Get a Milestone by ID"))
  def getMilestone(
      @Param("Milestone ID") id: Long
  ): String =
    getRequest[MilestoneWrapper](s"milestones/$id").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Milestone#listMilestonesForPipeline"</a>
   */
  @Tool(Some("list_milestones_by_pipeline"), Some("List Milestones associated with a Sales Pipeline"))
  def listMilestonesByPipelineId(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Sales Pipeline ID") pipelineId: Long
  ): String =
    getRequest[MilestoneListWrapper](s"pipelines/$pipelineId/milestones", pagination).toJson

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
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Search Lost Reasons by name", required = false) query: Option[String] = None
  ): String =
    getRequest[LostReasonListWrapper](
      "lostreasons",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Lost_Reason#showLostReason"</a>
   */
  @Tool(Some("get_lost_reason"), Some("Get a Lost Reason by ID"))
  def getLostReason(
      @Param("Lost Reason ID") id: Long
  ): String =
    getRequest[LostReasonWrapper](s"lostreasons/$id").toJson

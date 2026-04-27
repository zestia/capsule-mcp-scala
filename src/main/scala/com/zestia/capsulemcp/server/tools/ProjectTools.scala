/*
 * Copyright 2026 Zestia Ltd
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

object ProjectTools:

  /**
   * Manually registered tool
   *
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  def listProjects(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[ProjectListWrapper]("kases/filters/results", filter, pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Project#showProject"</a>
   */
  @Tool(
    Some("get_project"),
    Some("Get a Project by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def getProject(
      @Param("Project ID") id: Long
  ): String =
    getRequest[ProjectWrapper](s"kases/$id").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Board#listBoards"</a>
   */
  @Tool(
    Some("list_boards"),
    Some("List Boards for Projects, with optional searching by name"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def listBoards(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Search Boards by name", required = false) query: Option[String] = None
  ): String =
    getRequest[BoardListWrapper](
      "boards",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Board#showBoard"</a>
   */
  @Tool(
    Some("get_board"),
    Some("Get a Project Board by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def getBoard(
      @Param("Board ID") id: Long
  ): String =
    getRequest[BoardWrapper](s"boards/$id").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Stage#listStages"</a>
   */
  @Tool(
    Some("list_stages"),
    Some("List Stages across all Project Boards. To list Stages on a specific Board, use `list_stages_by_board_id`"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def listStages(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    getRequest[StageListWrapper]("stages", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Stage#showStage"</a>
   */
  @Tool(
    Some("get_stage"),
    Some("Get a Project Stage by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def getStage(
      @Param("Stage ID") id: Long
  ): String =
    getRequest[StageWrapper](s"stages/$id").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Stage#listStagesForBoard"</a>
   */
  @Tool(
    Some("list_stages_by_board"),
    Some("List Stages associated with a Project Board"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true)
  )
  def listStagesByBoardId(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Board ID") boardId: Long
  ): String =
    getRequest[StageListWrapper](s"boards/$boardId/stages", pagination).toJson

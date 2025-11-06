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
import com.zestia.capsulemcp.model.{BoardsResponse, Pagination, ProjectsResponse, StagesResponse}
import com.zestia.capsulemcp.server.tools.common.ToolDescriptions.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.{filterRequest, getRequest}
import zio.json.*

object ProjectTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  @Tool(
    Some("describe_list_projects"),
    Some("Returns a detailed description of how to use the `list_projects` tool.")
  )
  def describeSearchProjects(): String =
    listToolDescription("projects", projectFieldReference)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  @Tool(
    Some("list_projects"),
    Some(
      "List Projectes with comprehensive filtering ability. Refer to `describe_list_projects` for tool description and usage"
    )
  )
  def listProjects(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param(ToolParams.filterDescription) filter: Filter
  ): String =
    filterRequest[ProjectsResponse]("kases/filters/results", filter, pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Board#listBoards"</a>
   */
  @Tool(Some("list_boards"), Some("List Boards for Projects, with optional searching by name"))
  def listBoards(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param("Search Boards by name", required = false) query: Option[String] = None
  ): String =
    getRequest[BoardsResponse](
      "boards",
      pagination,
      queryParams = query.fold(Map.empty[String, String])(q => Map("q" -> q))
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Stage#listStages"</a>
   */
  @Tool(
    Some("list_stages"),
    Some("List Stages across all Project Boards. To list Stages on a specific Board, use `list_stages_by_board_id`")
  )
  def listStages(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[StagesResponse]("stages", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Stage#listStagesForBoard"</a>
   */
  @Tool(Some("list_stages_by_board_id"), Some("List Stages associated with a Project Board"))
  def listStagesByBoardId(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination,
      @Param("Board ID", required = true) boardId: Long
  ): String =
    getRequest[StagesResponse](s"boards/$boardId/stages", pagination).toJson

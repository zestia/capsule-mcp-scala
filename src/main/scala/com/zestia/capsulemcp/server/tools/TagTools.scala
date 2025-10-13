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
import com.zestia.capsulemcp.model.{Pagination, TagDefinitionsResponse}
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TagTools:

  @Tool(
    Some("list_contact_tags"),
    description = Some("List Tags defined for Contacts")
  )
  def listContactTags(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse]("parties/tags", pagination).toJson

  @Tool(
    Some("list_opportunity_tags"),
    description = Some("List Tags defined for Opportunities")
  )
  def listOpportunityTags(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse]("opportunities/tags", pagination).toJson

  @Tool(
    Some("list_project_tags"),
    description = Some("List Tags defined for Projects")
  )
  def listProjectTags(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[TagDefinitionsResponse]("kases/tags", pagination).toJson

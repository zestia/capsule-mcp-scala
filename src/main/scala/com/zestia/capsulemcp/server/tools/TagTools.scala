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
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TagTools:

  private def getTagForEntity(entityPath: String, id: Long): String =
    getRequest[TagDefinitionWrapper](s"$entityPath/tags/$id").toJson

  private def listTagsForEntity(entityPath: String, pagination: Option[Pagination]): String =
    getRequest[TagDefinitionListWrapper](s"$entityPath/tags", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#showTag"</a>
   */
  @Tool(Some("get_contact_tag"), Some("Get Contact Tag Definition by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def getContactTag(
      @Param("Tag ID") id: Long
  ): String =
    getTagForEntity("parties", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#showTag"</a>
   */
  @Tool(Some("get_opportunity_tag"), Some("Get Opportunity Tag Definition by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def getOpportunityTag(
      @Param("Tag ID") id: Long
  ): String =
    getTagForEntity("opportunities", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#showTag"</a>
   */
  @Tool(Some("get_project_tag"), Some("Get Project Tag Definition by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def getProjectTag(
      @Param("Tag ID") id: Long
  ): String =
    getTagForEntity("kases", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#listTags"</a>
   */
  @Tool(Some("list_contact_tags"), Some("List Tags defined for Contacts"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listContactTags(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listTagsForEntity("parties", pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#listTags"</a>
   */
  @Tool(Some("list_opportunity_tags"), Some("List Tags defined for Opportunities"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listOpportunityTags(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listTagsForEntity("opportunities", pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Tag#listTags"</a>
   */
  @Tool(Some("list_project_tags"), Some("List Tags defined for Projects"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listProjectTags(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listTagsForEntity("kases", pagination)

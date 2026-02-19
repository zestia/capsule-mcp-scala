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

import com.tjclp.fastmcp.macros.MapToFunctionMacro
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.server.schemas.ActivitySchemas
import zio.*
import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.{filterRequest, getRequest}
import zio.json.*

object ActivityTools:

  /**
   * Manually registered tool
   */
  def listActivities(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[ActivityListWrapper](
      "activities/filters/results",
      filter,
      pagination,
      embed = List("entry", "task")
    ).toJson

  private def listEntriesForEntity(entityPath: String, id: Long, pagination: Option[Pagination]): String =
    getRequest[EntryListWrapper](s"$entityPath/$id/entries", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Entry#listEntriesForEntity"</a>
   */
  @Tool(Some("list_entries_for_contact"), Some("List notes, emails and completed tasks for a Contact"))
  def listEntriesForContact(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Contact ID") id: Long
  ): String =
    listEntriesForEntity("parties", id, pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Entry#listEntriesForEntity"</a>
   */
  @Tool(Some("list_entries_for_opportunity"), Some("List notes, emails and completed tasks for an Opportunity"))
  def listEntriesForOpportunity(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Opportunity ID") id: Long
  ): String =
    listEntriesForEntity("opportunities", id, pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Entry#listEntriesForEntity"</a>
   */
  @Tool(Some("list_entries_for_project"), Some("List notes, emails and completed tasks for a Project"))
  def listEntriesForProject(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Project ID") id: Long
  ): String =
    listEntriesForEntity("kases", id, pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Entry#showEntry"</a>
   */
  @Tool(Some("get_entry"), Some("Get an Entry (notes, emails and completed tasks) by ID"))
  def getEntry(
      @Param("Entry ID") id: Long
  ): String =
    getRequest[EntryWrapper](s"entries/$id").toJson

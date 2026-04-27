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
import com.zestia.capsulemcp.model.{Pagination, TrackListWrapper, TrackWrapper}
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TrackTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Track#showTrack"</a>
   */
  @Tool(Some("get_track"), Some("Get Track by ID"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def getTrack(
      @Param("Track ID", required = true) id: Long
  ): String =
    getRequest[TrackWrapper](s"tracks/$id", embed = List("tasks")).toJson

  private def listTracksForEntity(entityPath: String, entityId: Long, pagination: Option[Pagination]): String =
    getRequest[TrackListWrapper](s"$entityPath/$entityId/tracks", pagination, embed = List("tasks")).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Track#listTrack"</a>
   */
  @Tool(Some("list_tracks_for_contact"), Some("List Tracks for specified Contact"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listTracksForContact(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Contact ID") contactId: Long
  ): String =
    listTracksForEntity("parties", contactId, pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Track#listTrack"</a>
   */
  @Tool(Some("list_tracks_for_opportunity"), Some("List Tracks for specified Opportunity"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listTracksForOpportunity(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Opportunity ID") opportunityId: Long
  ): String =
    listTracksForEntity("opportunities", opportunityId, pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Track#listTrack"</a>
   */
  @Tool(Some("list_tracks_for_project"), Some("List Tracks for specified Project"),
    readOnlyHint = Some(true),
    destructiveHint = Some(false),
    idempotentHint = Some(true),
    openWorldHint = Some(true))
  def listTracksForProject(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Project ID") projectId: Long
  ): String =
    listTracksForEntity("kases", projectId, pagination)

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

import com.tjclp.fastmcp.core.Tool
import com.zestia.capsulemcp.model.{TeamsResponse, UsersResponse}
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object TeamTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Team#listTeams"</a>
   */
  @Tool(Some("list_teams"), Some("List Teams and team members"))
  def listTeams(): String =
    getRequest[TeamsResponse]("teams", embed = List("memberships")).toJson

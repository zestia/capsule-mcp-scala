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
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object UserTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/User#listUsers"</a>
   */
  @Tool(Some("list_users"), Some("List Users"))
  def listUsers(): String =
    getRequest[UserListWrapper]("users").toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/User#showUser"</a>
   */
  @Tool(Some("get_user"), Some("Get User by ID"))
  def getUser(
      @Param("User ID") id: Long
  ): String =
    getRequest[UserWrapper](s"users/$id", embed = List("party")).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/User#showCurrentUser"</a>
   */
  @Tool(Some("get_current_user"), Some("Get the current User"))
  def getCurrentUser(): String =
    getRequest[UserWrapper]("users/current", embed = List("party")).toJson

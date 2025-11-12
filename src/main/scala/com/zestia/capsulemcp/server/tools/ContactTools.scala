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

import com.zestia.capsulemcp.server.schemas.ContactSchemas
import com.tjclp.fastmcp.macros.MapToFunctionMacro
import com.tjclp.fastmcp.server.FastMcpServer
import com.zestia.capsulemcp.model.*
import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.server.schemas.ActivitySchemas
import com.zestia.capsulemcp.service.CapsuleHttpClient.*
import zio.*
import zio.json.*

object ContactTools:

  /**
   * Manually registered tool
   *
   * See <a href="https://developer.capsulecrm.com/v2/operations/Filter#runAdHocFilterQuery"</a>
   */
  def listContacts(
      pagination: Option[Pagination],
      filter: Filter
  ): String =
    filterRequest[ContactListWrapper]("parties/filters/results", filter, pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Party#showParty"</a>
   */
  @Tool(Some("get_contact"), Some("Get a Contact"))
  def getEntry(
      @Param("Contact ID") id: Long
  ): String =
    getRequest[ContactWrapper](s"parties/$id").toJson

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
import com.zestia.capsulemcp.model.{FieldDefinitionsResponse, Pagination}
import com.zestia.capsulemcp.server.tools.common.ToolParams
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object CustomFieldTools:

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(Some("list_contact_custom_fields"), Some("List Custom Fields defined for Contacts"))
  def listContactCustomFields(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse]("parties/fields/definitions", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(Some("list_opportunity_custom_fields"), Some("List Custom Fields defined for Opportunities"))
  def listOpportunityCustomFields(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse]("opportunities/fields/definitions", pagination).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(Some("list_project_custom_fields"), Some("List Custom Fields defined for Projects"))
  def listProjectCustomFields(
      @Param(ToolParams.paginationDescription, required = ToolParams.paginationRequired) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse]("kases/fields/definitions", pagination).toJson

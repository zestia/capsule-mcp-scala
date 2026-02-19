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

object CustomFieldTools:

  private def getCustomFieldForEntity(entityPath: String, id: Long): String =
    getRequest[FieldDefinitionWrapper](s"$entityPath/fields/definitions/$id").toJson

  private def listCustomFieldsForEntity(entityPath: String, pagination: Option[Pagination]): String =
    getRequest[FieldDefinitionListWrapper](
      s"$entityPath/fields/definitions",
      pagination,
      Map("includeDataTagFields" -> false)
    ).toJson

  private def listCustomFieldDefinitionsForTag(entityPath: String, id: Long, pagination: Option[Pagination]): String =
    getRequest[DataTagFieldDefinitionListWrapper](
      s"$entityPath/tags/$id/definitions",
      pagination
    ).toJson

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#showField"</a>
   */
  @Tool(Some("get_contact_custom_field"), Some("Get Contact Custom Field Definition by ID"))
  def getContactCustomField(
      @Param("Custom Field Definition ID") id: Long
  ): String =
    getCustomFieldForEntity("parties", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#showField"</a>
   */
  @Tool(Some("get_opportunity_custom_field"), Some("Get Opportunity Custom Field Definition by ID"))
  def getOpportunityCustomField(
      @Param("Custom Field Definition ID") id: Long
  ): String =
    getCustomFieldForEntity("opportunities", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#showField"</a>
   */
  @Tool(Some("get_project_custom_field"), Some("Get Project Custom Field Definition by ID"))
  def getProjectCustomField(
      @Param("Custom Field Definition ID") id: Long
  ): String =
    getCustomFieldForEntity("kases", id)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(
    Some("list_contact_custom_fields"),
    Some("List Custom Fields defined for Contacts (does not include DataTag fields)")
  )
  def listContactCustomFields(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listCustomFieldsForEntity("parties", pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(
    Some("list_opportunity_custom_fields"),
    Some("List Custom Fields defined for Opportunities (does not include DataTag fields)")
  )
  def listOpportunityCustomFields(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listCustomFieldsForEntity("opportunities", pagination)

  /**
   * See <a href="https://developer.capsulecrm.com/v2/operations/Custom_Field#listFields"</a>
   */
  @Tool(
    Some("list_project_custom_fields"),
    Some("List Custom Fields defined for Projects (does not include DataTag fields)")
  )
  def listProjectCustomFields(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination]
  ): String =
    listCustomFieldsForEntity("kases", pagination)

  @Tool(
    Some("list_custom_fields_for_contact_data_tag"),
    Some("List Custom Fields defined for a Contact DataTag (a type of Tag used to group Custom Fields together)")
  )
  def listCustomFieldsContactDataTag(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Tag ID") id: Long
  ): String =
    listCustomFieldDefinitionsForTag("parties", id, pagination)

  @Tool(
    Some("list_custom_fields_for_opportunity_data_tag"),
    Some("List Custom Fields defined for an Opportunity DataTag (a type of Tag used to group Custom Fields together)")
  )
  def listCustomFieldsOpportunityDataTag(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Tag ID") id: Long
  ): String =
    listCustomFieldDefinitionsForTag("opportunities", id, pagination)

  @Tool(
    Some("list_custom_fields_for_project_data_tag"),
    Some("List Custom Fields defined for a Project DataTag (a type of Tag used to group Custom Fields together)")
  ) def listCustomFieldsProjectDataTag(
      @Param(ToolParams.paginationDescription, required = false) pagination: Option[Pagination],
      @Param("Tag ID") id: Long
  ): String =
    listCustomFieldDefinitionsForTag("kases", id, pagination)

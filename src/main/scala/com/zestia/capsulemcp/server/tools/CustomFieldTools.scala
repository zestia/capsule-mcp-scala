package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.{FieldDefinitionsResponse, Pagination}
import com.zestia.capsulemcp.service.CapsuleHttpClient.getRequest
import zio.json.*

object CustomFieldTools:

  @Tool(
    name = Some("list_contact_custom_fields"),
    description = Some("List Custom Fields defined for Contacts")
  )
  def listContactCustomFields(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse](
      "parties/fields/definitions",
      pagination
    ).toJson

  @Tool(
    name = Some("list_opportunity_custom_fields"),
    description = Some(
      "List Custom Fields defined for Opportunities"
    )
  )
  def listOpportunityCustomFields(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse](
      "opportunities/fields/definitions",
      pagination
    ).toJson

  @Tool(
    name = Some("list_project_custom_fields"),
    description = Some(
      "List Custom Fields defined for Projects"
    )
  )
  def listProjectCustomFields(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[FieldDefinitionsResponse](
      "kases/fields/definitions",
      pagination
    ).toJson

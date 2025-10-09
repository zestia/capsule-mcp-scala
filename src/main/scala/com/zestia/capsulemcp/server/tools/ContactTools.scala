package com.zestia.capsulemcp.server.tools

import com.tjclp.fastmcp.core.{Param, Tool}
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.model.{ContactsResponse, Pagination}
import ToolDescriptionHelper.*
import com.zestia.capsulemcp.service.CapsuleHttpClient.filterRequest
import zio.json.*

object ContactTools:
  
  @Tool(
    name = Some("describe_search_contacts"),
    description = Some(
      "Returns a detailed description of how to use the `search_contacts` tool."
    )
  )
  def describeSearchContacts(): String =
    searchToolDescription("contacts", contactFieldReference)

  @Tool(
    name = Some("search_contacts"),
    description = Some(
      "Perform a search of contacts. Refer to `describe_search_contacts` for tool description and usage"
    )
  )
  def searchContacts(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[ContactsResponse](
      "parties/filters/results",
      filter,
      pagination
    ).toJson
  }

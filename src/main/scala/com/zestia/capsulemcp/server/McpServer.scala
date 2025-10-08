package com.zestia.capsulemcp.server

import com.zestia.capsulemcp.server.ToolDescriptionHelper.*
import com.zestia.capsulemcp.service.CapsuleHttpClient
import com.zestia.capsulemcp.service.CapsuleHttpClient.*
import com.zestia.capsulemcp.model.*
import com.zestia.capsulemcp.model.filter.Filter
import com.zestia.capsulemcp.model.filter.*
import com.zestia.capsulemcp.model.filter.SimpleCondition.*
import com.zestia.capsulemcp.model.Pagination
import com.zestia.capsulemcp.util.{FileLogger, FileLogging}
import com.tjclp.fastmcp.core.{Tool, Param}
import com.tjclp.fastmcp.macros.RegistrationMacro.*
import com.tjclp.fastmcp.server.FastMcpServer
import zio.*
import zio.json.*
import sttp.tapir.generic.auto.*

object McpServer extends ZIOAppDefault with FileLogging:
  override def run =
    for
      _ <- ZIO.attempt {
        FileLogger.init()
        logger.info("MCP Server starting...")
      }
      // Create server instance
      server <- ZIO.succeed(FastMcpServer("McpServer"))
      // Process tools using the scanAnnotations macro extension method
      _ <- ZIO.attempt {
        // This macro finds all methods with @Tool, @Prompt or @Resource annotations
        // and registers them with the server
        server.scanAnnotations[CapsuleMcpServer.type]
      }
      // Run the server
      _ <- server.runStdio()
    yield ()

object CapsuleMcpServer extends FileLogging:

  /* Describe Search Tools */

  @Tool(
    name = Some("describe_search_contacts"),
    description = Some(
      "Returns a detailed description of how to use the `search_contacts` tool."
    )
  )
  def describeSearchContacts(): String =
    searchToolDescription("contacts", contactFieldReference)

  @Tool(
    name = Some("describe_search_opportunities"),
    description = Some(
      "Returns a detailed description of how to use the `search_opportunities` tool."
    )
  )
  def describeSearchOpportunities(): String =
    searchToolDescription("opportunities", opportunityFieldReference)

  @Tool(
    name = Some("describe_search_projects"),
    description = Some(
      "Returns a detailed description of how to use the `search_projects` tool."
    )
  )
  def describeSearchProjects(): String =
    searchToolDescription("projects", projectFieldReference)

  /* Search Tools */

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

  @Tool(
    name = Some("search_opportunities"),
    description = Some(
      "Perform a search of Opportunities. Refer to `describe_search_opportunities` for tool description and usage"
    )
  )
  def searchOpportunities(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[OpportunitiesResponse](
      "opportunities/filters/results",
      filter,
      pagination
    ).toJson
  }

  @Tool(
    name = Some("search_projects"),
    description = Some(
      "Perform a search of Projects. Refer to `describe_search_projects` for tool description and usage"
    )
  )
  def searchProjects(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @Param("array of zero or more conditions") filter: Filter
  ): String = {
    filterRequest[ProjectsResponse](
      "kases/filters/results",
      filter,
      pagination
    ).toJson
  }

  /* List Custom Field Definitions Tools */

  @Tool(
    name = Some("list_contact_custom_fields"),
    description = Some("List Custom Fields defined for Contacts in CRM account")
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

  /* Various other simple list tools */

  @Tool(
    name = Some("list_users"),
    description = Some("List Users in CRM account")
  )
  def listUsers(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[UsersResponse](
      "users",
      pagination
    ).toJson

  @Tool(
    name = Some("list_teams"),
    description = Some("List Teams in CRM account")
  )
  def listTeams(
      @Param(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[TeamsResponse](
      "teams",
      pagination
    ).toJson

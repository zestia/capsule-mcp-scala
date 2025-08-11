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
import com.tjclp.fastmcp.core.{Tool, ToolParam}
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
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @ToolParam("array of zero or more conditions") filter: Filter
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
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @ToolParam("array of zero or more conditions") filter: Filter
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
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination,
      @ToolParam("array of zero or more conditions") filter: Filter
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
      @ToolParam(
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
      @ToolParam(
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
      @ToolParam(
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
      @ToolParam(
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
      @ToolParam(
        "pagination options",
        required = false
      ) pagination: Pagination
  ): String =
    getRequest[TeamsResponse](
      "teams",
      pagination
    ).toJson

  /*
   * TODO: Work in progress around allowing client to specify field selection
   * */
  @Tool(
    name = Some("describe_contacts"),
    description = Some(
      "Returns the available fields that can be requested for contacts in CRM account"
    )
  )
  def describeParties(): String =
    val availableFields = EmbeddableFields(
      List(
        EmbeddableField(
          "id",
          includedByDefault = true
        ),
        EmbeddableField(
          "type",
          description = Some("Contact type: either 'person' or 'organisation'"),
          includedByDefault = true
        ),
        EmbeddableField("about"),
        EmbeddableField("createdAt"),
        EmbeddableField("updatedAt"),
        EmbeddableField("lastContactedAt"),
        EmbeddableField("addresses"),
        EmbeddableField("phoneNumbers"),
        EmbeddableField("websites"),
        EmbeddableField("emailAddresses"),
        EmbeddableField("pictureURL"),
        EmbeddableField("tags"),
        EmbeddableField(
          "fields",
          description =
            Some("An array of custom fields that are defined for this contact")
        ),
        EmbeddableField(
          "owner",
          description = Some("The user this contact is assigned to")
        ),
        EmbeddableField(
          "team",
          description = Some("The team this contact is assigned to.")
        ),
        EmbeddableField(
          "missingImportantFields",
          description = Some(
            "Indicates if this contact has any important custom fields that are missing a value"
          )
        ),
        EmbeddableField(
          "firstName",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "lastName",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "title",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "jobTitle",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "organisation",
          appliesTo = Seq(PartyType.person.toString)
        ),
        EmbeddableField(
          "name",
          appliesTo = Seq(PartyType.organisation.toString)
        )
      )
    )
    availableFields.toJson

case class EmbeddableField(
    fieldName: String,
    description: Option[String] = None,
    includedByDefault: Boolean = false,
    appliesTo: Seq[String] =
      Seq(PartyType.person.toString, PartyType.organisation.toString)
) derives JsonDecoder,
      JsonEncoder

case class EmbeddableFields(fields: List[EmbeddableField])
    derives JsonDecoder,
      JsonEncoder

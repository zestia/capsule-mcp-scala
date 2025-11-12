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

package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*

/**
 * JSON Schemas for Opportunity-related Tools
 */
object OpportunitySchemas extends HasCustomFieldFilterFields:

  /**
   * Valid filter fields for opportunities. See https://developer.capsulecrm.com/v2/reference/filters#field-reference
   */
  private val opportunityFilterFields: List[FilterField] = List(
    FilterField("id", ValueType.Number, "Opportunity ID"),
    FilterField("name", ValueType.String, "Opportunity name"),
    FilterField("status", ValueType.String, "Opportunity status (open, won, lost)"),
    FilterField("party", ValueType.Number, "Associated contact ID"),
    FilterField("primaryContact", ValueType.Number, "Primary contact ID"),
    FilterField("primaryContactName", ValueType.String, "Primary contact name"),
    FilterField("pipeline", ValueType.String, "Pipeline name"),
    FilterField("pipeline", ValueType.Number, "Pipeline ID"),
    FilterField("milestone", ValueType.String, "Milestone name"),
    FilterField("milestone", ValueType.Number, "Milestone ID"),
    FilterField("probability", ValueType.Number, "Probability (0-100)"),
    FilterField("lostReason", ValueType.String, "Lost reason name"),
    FilterField("lostReason", ValueType.Number, "Lost reason ID"),
    FilterField("currency", ValueType.String, "Currency"),
    FilterField("expectedValue", ValueType.Money, "Expected value"),
    FilterField("tag", ValueType.Number, "Tag ID"),
    FilterField("tag", ValueType.String, "Tag name"),
    FilterField("owner", ValueType.String, "Owner name"),
    FilterField("owner", ValueType.Number, "Owner ID"),
    FilterField("team", ValueType.String, "Team name"),
    FilterField("team", ValueType.Number, "Team ID"),
    FilterField("isOpen", ValueType.Boolean, "Is open", mandatory = true),
    FilterField("isStale", ValueType.Boolean, "Is stale", mandatory = true),
    FilterField("isOwnedByMe", ValueType.Boolean, "Is owned by me", mandatory = true),
    FilterField("hasTags", ValueType.Boolean, "Has tags", mandatory = true),
    FilterField("includedForConversion", ValueType.Boolean, "Is included in conversion rate", mandatory = true),
    FilterField("hasOpenTasks", ValueType.Boolean, "Has open tasks", mandatory = true),
    FilterField("addedOn", ValueType.Date, "Date added"),
    FilterField("updatedOn", ValueType.Date, "Date updated"),
    FilterField("closedOn", ValueType.Date, "Date closed on"),
    FilterField("expectedCloseOn", ValueType.Date, "Expected date close on"),
    FilterField("lastContactedOn", ValueType.Date, "Last contacted date")
  ) ++ customFieldFilterFields

  /**
   * Schema for the list_opportunities tool
   */
  val opportunityFilterSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> paginationSchema,
      "filter" -> buildFilterSchema(opportunityFilterFields)
    )
  )

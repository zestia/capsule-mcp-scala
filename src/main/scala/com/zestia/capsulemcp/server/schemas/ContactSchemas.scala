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
 * JSON Schemas for Contact-related Tools
 */
object ContactSchemas extends HasFilterSchema with HasCustomFieldFilterFields:

  private val parentOrgCustomFieldNamePattern = "^org\\.custom:\\d+$"

  override protected val filterFields: List[FilterField] = List(
    FilterField("id", ValueType.Number, "Contact ID"),
    FilterField("name", ValueType.String, "Contact name"),
    FilterField("jobTitle", ValueType.String, "Job Title"),
    FilterField("title", ValueType.String, "Personal Title"),
    FilterField("email", ValueType.String, "Email address"),
    FilterField("website", ValueType.String, "Website"),
    FilterField("phone", ValueType.String, "Phone number"),
    FilterField("city", ValueType.String, "City"),
    FilterField("zip", ValueType.String, "Zip/postal code"),
    FilterField("state", ValueType.String, "State"),
    FilterField("country", ValueType.String, "Country code (alpha-2)"),
    FilterField("type", ValueType.String, "Contact type (person or organisation)"),
    FilterField("tag", ValueType.Number, "Tag ID"),
    FilterField("tag", ValueType.String, "Tag name"),
    FilterField("owner", ValueType.String, "Owner name"),
    FilterField("owner", ValueType.Number, "Owner ID"),
    FilterField("team", ValueType.String, "Team name"),
    FilterField("team", ValueType.Number, "Team ID"),
    FilterField("hasEmailAddress", ValueType.Boolean, "Has email address", mandatory = true),
    FilterField("hasPhoneNumber", ValueType.Boolean, "Has phone number", mandatory = true),
    FilterField("hasAddress", ValueType.Boolean, "Has address", mandatory = true),
    FilterField("hasPeople", ValueType.Boolean, "Has people", mandatory = true),
    FilterField("hasTags", ValueType.Boolean, "Has tags", mandatory = true),
    FilterField("hasOpenTasks", ValueType.Boolean, "Has open tasks", mandatory = true),
    FilterField("hasOpportunities", ValueType.Boolean, "Has opportunities", mandatory = true),
    FilterField("hasCases", ValueType.Boolean, "Has projects", mandatory = true),
    FilterField("addedOn", ValueType.Date, "Date added"),
    FilterField("updatedOn", ValueType.Date, "Date updated"),
    FilterField("lastContactedOn", ValueType.Date, "Last contacted date"),
    FilterField("org.name", ValueType.String, "Parent organisation's name"),
    FilterField("org.tag", ValueType.Number, "Parent organisation's tag ID"),
    FilterField("org.tag", ValueType.String, "Parent organisation's tag name"),
    // Parent organisation custom field patterns
    FilterField(
      parentOrgCustomFieldNamePattern,
      ValueType.Boolean,
      "Filter on a parent organisation's boolean custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true,
      mandatory = true
    ),
    FilterField(
      parentOrgCustomFieldNamePattern,
      ValueType.Date,
      "Filter on a parent organisation's date custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    ),
    FilterField(
      parentOrgCustomFieldNamePattern,
      ValueType.Number,
      "Filter on a parent organisation's number custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    ),
    FilterField(
      parentOrgCustomFieldNamePattern,
      ValueType.String,
      "Filter on a parent organisation's string custom field by field definition ID (format = custom:{fieldId})",
      isPattern = true
    )
  ) ++ customFieldFilterFields

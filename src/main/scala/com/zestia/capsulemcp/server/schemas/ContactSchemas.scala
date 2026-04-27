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

package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*
import io.circe.*

/**
 * JSON Schemas for Contact-related Tools
 */
object ContactSchemas extends HasFilterSchema with HasCustomFieldFilterFields:

  private val parentOrgCustomFieldNamePattern = "^org\\.custom:\\d+$"

  override protected val filterFields: List[FilterField] = List(
    ExactMatchFilterField("id", "Contact ID", ValueType.Number),
    StringFilterField("name", "Contact name"),
    StringFilterField("jobTitle", "Job Title"),
    StringFilterField("title", "Personal Title"),
    StringFilterField("email", "Email address"),
    StringFilterField("website", "Website"),
    StringFilterField("phone", "Phone number"),
    StringFilterField("city", "City"),
    StringFilterField("zip", "Zip/postal code"),
    StringFilterField("state", "State"),
    ExactMatchFilterField("country", "Country code (alpha-2)", ValueType.String),
    ExactMatchFilterField("type", "Contact type (person or organisation)", ValueType.String),
    ExactMatchFilterField("tag", "Tag ID", ValueType.Number),
    ExactMatchFilterField("tag", "Tag name", ValueType.String),
    ExactMatchFilterField("owner", "User owner name", ValueType.String),
    ExactMatchFilterField("owner", "User owner ID", ValueType.Number),
    ExactMatchFilterField("team", "Team owner name", ValueType.String),
    ExactMatchFilterField("team", "Team owner ID", ValueType.Number),
    BooleanFilterField("hasEmailAddress", "Has email address"),
    BooleanFilterField("hasPhoneNumber", "Has phone number"),
    BooleanFilterField("hasAddress", "Has address"),
    BooleanFilterField("hasPeople", "Has people"),
    BooleanFilterField("hasTags", "Has tags"),
    BooleanFilterField("hasOpenTasks", "Has open tasks"),
    BooleanFilterField("hasOpportunities", "Has opportunities"),
    BooleanFilterField("hasCases", "Has projects"),
    DateFilterField("addedOn", "Date added"),
    DateFilterField("updatedOn", "Date updated"),
    DateFilterField("lastContactedOn", "Last contacted date"),
    StringFilterField("org.name", "Parent organisation's name"),
    ExactMatchFilterField("org.tag", "Parent organisation's tag ID", ValueType.Number),
    ExactMatchFilterField("org.tag", "Parent organisation's tag name", ValueType.String),
    // Parent organisation custom field patterns
    ExactMatchFilterField(
      parentOrgCustomFieldNamePattern,
      "Filter on a parent organisation's boolean custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Boolean,
      mandatory = true,
      isPattern = true
    ),
    ExactMatchFilterField(
      parentOrgCustomFieldNamePattern,
      "Filter on a parent organisation's date custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Date,
      mandatory = true,
      isPattern = true
    ),
    ExactMatchFilterField(
      parentOrgCustomFieldNamePattern,
      "Filter on a parent organisation's number custom field by field definition ID (format = custom:{fieldId})",
      ValueType.Number,
      mandatory = true,
      isPattern = true
    ),
    ExactMatchFilterField(
      parentOrgCustomFieldNamePattern,
      "Filter on a parent organisation's string custom field by field definition ID (format = custom:{fieldId})",
      ValueType.String,
      mandatory = true,
      isPattern = true
    )
  ) ++ customFieldFilterFields

  val updateContactSchema: String =
    SchemaBuilders.objectSchema(
      Map(
        "id" -> SchemaBuilders.integer("Contact ID"),
        "fields" -> Json.obj(
          "type" -> Json.fromString("array"),
          "description" -> Json.fromString(
            "Custom field values to update or add. Deleting is NOT allowed but updates will overwrite existing values."
          ),
          "items" -> Json.obj(
            "type" -> Json.fromString("object"),
            "properties" -> Json.obj(
              "definition" -> SchemaBuilders.integer("Field definition ID"),
              "value" -> Json.obj(
                "description" -> Json.fromString(
                  "Value type depends on the custom field definition: text/list/link → string; date → string YYYY-MM-DD; multiselectlist → semicolon-delimited string; boolean → boolean; number → string or number. List values must match predefined options. Link values may include placeholders: {id}, {name}, {email}, {phone}, {phone[Mobile]}, {custom[field name]}"
                ),
                "oneOf" -> Json.arr(
                  Json.obj("type" -> Json.fromString("string")),
                  Json.obj("type" -> Json.fromString("boolean")),
                  Json.obj("type" -> Json.fromString("number"))
                )
              )
            ),
            "required" -> Json.arr(Json.fromString("definition"), Json.fromString("value"))
          )
        )
      ),
      required = List("id", "fields")
    )

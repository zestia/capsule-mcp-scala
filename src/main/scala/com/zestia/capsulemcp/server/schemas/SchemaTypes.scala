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

import com.zestia.capsulemcp.model.Pagination
import com.zestia.capsulemcp.model.filter.FilterOperators
import com.zestia.capsulemcp.model.filter.FilterOperators.*
import io.circe.*

/**
 * Reusable JSON schema type definitions
 */
object SchemaTypes:

  /**
   * Value types for filter fields
   */
  enum ValueType:
    case Boolean, String, Date, Number, Money

  /**
   * Standard pagination object schema
   */
  val paginationSchema: Json = Json.obj(
    "type" -> Json.fromString("object"),
    "description" -> Json.fromString("Pagination options"),
    "properties" -> Json.obj(
      "page" -> Json.obj(
        "type" -> Json.fromString("integer"),
        "description" -> Json.fromString("Page number"),
        "default" -> Json.fromInt(Pagination.DEFAULT_PAGE)
      ),
      "perPage" -> Json.obj(
        "type" -> Json.fromString("integer"),
        "description" -> Json.fromString("Page size"),
        "default" -> Json.fromInt(Pagination.DEFAULT_PER_PAGE)
      )
    ),
    "default" -> Json.obj(
      "page" -> Json.fromInt(Pagination.DEFAULT_PAGE),
      "perPage" -> Json.fromInt(Pagination.DEFAULT_PER_PAGE)
    )
  )

  /**
   * Build a filter schema with specific field constraints using anyOf. Provides per-field validation with specific
   * operators and value types
   */
  def buildFilterSchema(fields: List[FilterField]): Json =
    // create a schema for each field with its specific operators and value type
    val fieldSchemas = fields.map { field =>
      Json.obj(
        "type" -> Json.fromString("object"),
        "properties" -> Json.obj(
          "field" -> buildFieldSchema(field),
          "operator" -> buildFieldOperatorSchema(field),
          "value" -> buildFieldValueSchema(field)
        ),
        "required" -> Json.arr(
          Json.fromString("field"),
          Json.fromString("operator"),
          Json.fromString("value")
        )
      )
    }

    Json.obj(
      "type" -> Json.fromString("object"),
      "description" -> Json.fromString("Filter containing conditions to match"),
      "properties" -> Json.obj(
        "conditions" -> Json.obj(
          "type" -> Json.fromString("array"),
          "description" -> Json.fromString(
            "Array of conditions. Each condition filters on a specific field."
          ),
          "items" -> Json.obj(
            "anyOf" -> Json.arr(fieldSchemas*)
          )
        )
      ),
      "required" -> Json.arr(Json.fromString("conditions"))
    )

  /**
   * Build value schema based on field type
   */
  private def buildFieldValueSchema(field: FilterField): Json =
    def buildType(rawType: String): Json =
      if (field.mandatory) Json.fromString(rawType)
      else Json.arr(Json.fromString(rawType), Json.fromString("null"))

    field.valueType match
      case ValueType.Number =>
        Json.obj(
          "type" -> buildType("number"),
          "description" -> Json.fromString(field.description)
        )
      case ValueType.Date =>
        Json.obj(
          "type" -> buildType("string"),
          "format" -> Json.fromString("date"),
          "description" -> Json.fromString(field.description)
        )
      case ValueType.String =>
        Json.obj(
          "type" -> buildType("string"),
          "description" -> Json.fromString(field.description)
        )
      case ValueType.Boolean =>
        Json.obj(
          "type" -> buildType("boolean"),
          "description" -> Json.fromString(field.description)
        )
      case ValueType.Money =>
        Json.obj(
          "type" -> buildType("object"),
          "description" -> Json.fromString(field.description),
          "properties" -> Json.obj(
            "currency" -> Json.obj(
              "type" -> Json.fromString("string")
            ),
            "amount" -> Json.obj(
              "type" -> Json.fromString("number")
            )
          )
        )

  /**
   * Build field schema. Use pattern matching for dynamic field names (custom fields), const for static ones
   */
  private def buildFieldSchema(field: FilterField): Json =
    if (field.isPattern)
      val example = field.name match
        case s if s.contains("org\\.custom") => "org.custom:123"
        case s if s.contains("custom") => "custom:123"
        case _ => field.name

      Json.obj(
        "type" -> Json.fromString("string"),
        "pattern" -> Json.fromString(field.name),
        "example" -> Json.fromString(example)
      )
    else
      Json.obj(
        "const" -> Json.fromString(field.name)
      )

  /**
   * Build field operator schema
   */
  private def buildFieldOperatorSchema(field: FilterField): Json =
    Json.obj(
      "type" -> Json.fromString("string"),
      "enum" -> Json.arr(field.operators.toStrings.map(Json.fromString)*)
    )

  case class FilterField(
      name: String,
      valueType: ValueType,
      description: String,
      isPattern: Boolean = false, // if true, use regex pattern for matching field name instead of const
      mandatory: Boolean = false
  ):

    /**
     * Derive operators from valueType
     */
    def operators: List[Operator] = valueType match
      case ValueType.Boolean => FilterOperators.booleanOperators
      case ValueType.String => FilterOperators.stringOperators
      case ValueType.Date => FilterOperators.dateOperators
      case ValueType.Number => FilterOperators.numberOperators
      case ValueType.Money => FilterOperators.numberOperators

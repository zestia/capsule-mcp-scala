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

  /**
   * Base trait for filter fields
   */
  sealed trait FilterField:
    val name: String
    val description: String
    val isPattern: Boolean
    val mandatory: Boolean
    def valueType: ValueType
    def operators: List[Operator]

  /**
   * String filter field with default string operators (is, is not, contains, starts with, ends with)
   */
  case class StringFilterField(
      name: String,
      description: String,
      isPattern: Boolean = false,
      mandatory: Boolean = false,
      operatorsOverride: Option[List[Operator]] = None
  ) extends FilterField:
    def valueType: ValueType = ValueType.String
    def operators: List[Operator] = operatorsOverride.getOrElse(FilterOperators.stringOperators)

  /**
   * Number filter field with default number operators (is, is not, is greater than, is less than)
   */
  case class NumberFilterField(
      name: String,
      description: String,
      isPattern: Boolean = false,
      mandatory: Boolean = false,
      operatorsOverride: Option[List[Operator]] = None
  ) extends FilterField:
    def valueType: ValueType = ValueType.Number
    def operators: List[Operator] = operatorsOverride.getOrElse(FilterOperators.numberOperators)

  /**
   * Boolean filter field with default boolean operators (is, is not)
   */
  case class BooleanFilterField(
      name: String,
      description: String,
      isPattern: Boolean = false,
      operatorsOverride: Option[List[Operator]] = None
  ) extends FilterField:
    def valueType: ValueType = ValueType.Boolean
    def operators: List[Operator] = operatorsOverride.getOrElse(FilterOperators.booleanOperators)
    val mandatory: Boolean = true

  /**
   * Date filter field with default date operators (is, is not, is before, is after)
   */
  case class DateFilterField(
      name: String,
      description: String,
      isPattern: Boolean = false,
      mandatory: Boolean = false,
      operatorsOverride: Option[List[Operator]] = None
  ) extends FilterField:
    def valueType: ValueType = ValueType.Date
    def operators: List[Operator] = operatorsOverride.getOrElse(FilterOperators.dateOperators)

  /**
   * Money filter field with default number operators
   */
  case class MoneyFilterField(
      name: String,
      description: String,
      isPattern: Boolean = false,
      mandatory: Boolean = false,
      operatorsOverride: Option[List[Operator]] = None
  ) extends FilterField:
    def valueType: ValueType = ValueType.Money
    def operators: List[Operator] = operatorsOverride.getOrElse(FilterOperators.numberOperators)

  /**
   * For fields that only support 'is' and 'is not' operators even if their field type might indicate otherwise
   */
  case class ExactMatchFilterField(
      name: String,
      description: String,
      valueType: ValueType, // typically Number or String
      isPattern: Boolean = false,
      mandatory: Boolean = false
  ) extends FilterField:
    def operators: List[Operator] = FilterOperators.booleanOperators

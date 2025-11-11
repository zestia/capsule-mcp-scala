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
import com.zestia.capsulemcp.model.filter.FilterOperators.*
import io.circe.*

/**
 * Reusable JSON schema type definitions
 */
object SchemaTypes:

  /**
   * Standard pagination object schema
   */
  val pagination: Json = Json.obj(
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
   * Field specification for building constrained filter schemas
   */
  case class FilterField(
      name: String,
      valueType: String, // "number", "string", "date"
      operators: List[Operator],
      description: String
  )

  /**
   * Build a filter schema with specific field constraints using anyOf This provides precise per-field validation with
   * specific operators and value types
   */
  def filterWithFields(fields: List[FilterField]): Json =
    // Create a schema for each field with its specific operators and value type
    val fieldSchemas = fields.map { field =>
      val operatorStrings = field.operators.toStrings

      // Build value schema based on field type
      val valueSchema = field.valueType match
        case "number" =>
          Json.obj(
            "type" -> Json.fromString("number"),
            "description" -> Json.fromString(field.description)
          )
        case "date" =>
          Json.obj(
            "type" -> Json.fromString("string"),
            "format" -> Json.fromString("date"),
            "description" -> Json.fromString(field.description)
          )
        case "string" =>
          Json.obj(
            "type" -> Json.fromString("string"),
            "description" -> Json.fromString(field.description)
          )
        case _ =>
          Json.obj(
            "description" -> Json.fromString(field.description)
          )

      Json.obj(
        "type" -> Json.fromString("object"),
        "properties" -> Json.obj(
          "field" -> Json.obj(
            "const" -> Json.fromString(field.name)
          ),
          "operator" -> Json.obj(
            "type" -> Json.fromString("string"),
            "enum" -> Json.arr(operatorStrings.map(Json.fromString)*)
          ),
          "value" -> valueSchema
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

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

import io.circe.*

/**
 * Functions for building JSON schema components
 */
object SchemaBuilders:

  /**
   * Create a date-time string property
   */
  def dateTime(description: String): Json =
    Json.obj(
      "type" -> Json.fromString("string"),
      "format" -> Json.fromString("date-time"),
      "description" -> Json.fromString(description)
    )

  /**
   * Create a string property with optional enum values
   */
  def string(description: String, enumValues: Option[List[String]] = None): Json =
    val base = Json.obj(
      "type" -> Json.fromString("string"),
      "description" -> Json.fromString(description)
    )
    enumValues match
      case Some(values) =>
        base.deepMerge(Json.obj("enum" -> Json.arr(values.map(Json.fromString)*)))
      case None => base

  /**
   * Create an integer property
   */
  def integer(description: String): Json =
    Json.obj(
      "type" -> Json.fromString("integer"),
      "description" -> Json.fromString(description)
    )

  /**
   * Create a boolean property
   */
  def boolean(description: String): Json =
    Json.obj(
      "type" -> Json.fromString("boolean"),
      "description" -> Json.fromString(description)
    )

  /**
   * Create an array property with enum items
   */
  def arrayOfEnum(description: String, enumValues: List[String]): Json =
    Json.obj(
      "type" -> Json.fromString("array"),
      "items" -> Json.obj(
        "type" -> Json.fromString("string"),
        "enum" -> Json.arr(enumValues.map(Json.fromString)*)
      ),
      "description" -> Json.fromString(description)
    )

  /**
   * Create an array property with string items
   */
  def arrayOfStrings(description: String): Json =
    Json.obj(
      "type" -> Json.fromString("array"),
      "items" -> Json.obj("type" -> Json.fromString("string")),
      "description" -> Json.fromString(description)
    )

  /**
   * Create an array property with object items
   */
  def arrayOfObjects(description: String, itemSchema: Json): Json =
    Json.obj(
      "type" -> Json.fromString("array"),
      "items" -> itemSchema,
      "description" -> Json.fromString(description)
    )

  /**
   * Build the standard schema for an update tool that only supports updating custom fields
   */
  def updateEntitySchema(idDescription: String): String =
    objectSchema(
      Map(
        "id" -> integer(idDescription),
        "fields" -> Json.obj(
          "type" -> Json.fromString("array"),
          "description" -> Json.fromString(
            "Custom field values to update or add. Deleting is NOT allowed but updates will overwrite existing values."
          ),
          "items" -> Json.obj(
            "type" -> Json.fromString("object"),
            "properties" -> Json.obj(
              "definition" -> integer("Field definition ID"),
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

  /**
   * Build a complete object schema from a map of property names to their schemas
   */
  def objectSchema(properties: Map[String, Json], required: List[String] = List.empty): String =
    Json
      .obj(
        "type" -> Json.fromString("object"),
        "properties" -> Json.obj(properties.toSeq*),
        "required" -> Json.arr(required.map(Json.fromString)*)
      )
      .spaces2

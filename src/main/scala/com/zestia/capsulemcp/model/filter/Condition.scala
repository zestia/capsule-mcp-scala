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

package com.zestia.capsulemcp.model.filter

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import sttp.tapir.Schema.annotations.description
import zio.json.ast.Json
import zio.json.{jsonHint, JsonDecoder, JsonEncoder}

import scala.util.Try

/**
 * See <a href="https://developer.capsulecrm.com/v2/reference/filters"</a>
 */

// Jackson-style deserialisation needed for under the hood @Param binders in fast-mcp-scala
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[StringCondition], name = "string"),
    new JsonSubTypes.Type(value = classOf[NumberCondition], name = "number"),
    new JsonSubTypes.Type(value = classOf[DateCondition], name = "date"),
    new JsonSubTypes.Type(value = classOf[BooleanCondition], name = "boolean")
  )
)
sealed trait Condition:
  @description("the name of the field to filter on") val field: String
  @description("the operator to use (must be valid for the field type)") val operator: String
  @description("the type of the field value") val `type`: String

object Condition:

  implicit val encoder: JsonEncoder[Condition] =
    JsonEncoder[Json].contramap { condition =>
      val valueFieldValue = condition match {
        case StringCondition(_, _, _, value) =>
          Json.Str(value)
        case DateCondition(_, _, _, value) =>
          Json.Str(value)
        case NumberCondition(_, _, _, value) =>
          Json.Num(value)
        case BooleanCondition(_, _, _, value) =>
          Json.Bool(value)
      }

      Json.Obj(
        "field" -> Json.Str(condition.field),
        "operator" -> Json.Str(condition.operator),
        "value" -> valueFieldValue
      )
    }

  // Custom decoder to automatically convert value to the correct type
  implicit val decoder: JsonDecoder[Condition] =
    JsonDecoder[Json.Obj].mapOrFail { jsonObj =>
      for {
        fieldNameJson <- jsonObj.get("field").toRight("missing field")
        operatorJson <- jsonObj.get("operator").toRight("missing operator")
        fieldValueJson <- jsonObj
          .get("value")
          .toRight("missing value")

        fieldName <- fieldNameJson
          .as[String]
          .left
          .map(_ => "field must be a string")
        operator <- operatorJson
          .as[String]
          .left
          .map(_ => "operator must be a string")

        condition <- fieldValueJson match
          case Json.Str(s) =>
            Right(StringCondition(field = fieldName, operator = operator, value = s))
          case Json.Num(n) =>
            Try(n.longValue()).toOption
              .map(l => NumberCondition(field = fieldName, operator = operator, value = l))
              .toRight("value is not a valid number")
          case Json.Bool(b) =>
            Right(BooleanCondition(field = fieldName, operator = operator, value = b))
          case _ => Left("unsupported value type")
      } yield condition
    }

@jsonHint("string")
final case class StringCondition(
    `type`: String = "string",
    field: String,
    operator: String,
    @description("the field value to filter on (must be valid for the field type)") value: String
) extends Condition derives JsonDecoder, JsonEncoder

@jsonHint("date")
final case class DateCondition(
    `type`: String = "date",
    field: String,
    operator: String,
    @description("the field value to filter on (must be valid for the field type)") value: String
) extends Condition derives JsonDecoder, JsonEncoder

@jsonHint("number")
final case class NumberCondition(
    `type`: String = "number",
    field: String,
    operator: String,
    @description("the field value to filter on (must be valid for the field type)") value: Long
) extends Condition derives JsonDecoder, JsonEncoder

@jsonHint("boolean")
final case class BooleanCondition(
    `type`: String = "boolean",
    field: String,
    operator: String,
    @description("the field value to filter on (must be valid for the field type)") value: Boolean
) extends Condition derives JsonDecoder, JsonEncoder

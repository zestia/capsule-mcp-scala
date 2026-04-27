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

package com.zestia.capsulemcp.model

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, JsonNode, ValueDeserializer}
import tools.jackson.databind.annotation.JsonDeserialize
import zio.json.*
import zio.json.ast.Json
import scala.util.Try

sealed trait FieldValue:
  val id: Long
  val definition: FieldDefinition

object FieldValue:

  implicit val encoder: JsonEncoder[FieldValue] =
    JsonEncoder[Json].contramap { fieldValue =>
      val valueFieldValue = fieldValue match {
        case FieldValueString(_, value, _) =>
          Json.Str(value)
        case FieldValueNumber(_, value, _) =>
          Json.Num(value)
        case FieldValueBoolean(_, value, _) =>
          Json.Bool(value)
      }

      val defJson: Json =
        fieldValue.definition.toJsonAST match
          case Right(j) => j
          case Left(err) => Json.Null

      Json.Obj("id" -> Json.Num(fieldValue.id), "value" -> valueFieldValue, "definition" -> defJson)
    }

  implicit val decoder: JsonDecoder[FieldValue] =
    JsonDecoder[Json.Obj].mapOrFail { jsonObj =>
      for {
        idJson <- jsonObj.get("id").toRight("missing id")
        definitionJson <- jsonObj.get("definition").toRight("missing definition")
        valueJson <- jsonObj
          .get("value")
          .toRight("missing value")

        id <- idJson
          .as[Long]
          .left
          .map(_ => "id must be a number")
        definition <- definitionJson
          .as[FieldDefinition]
          .left
          .map(error => s"could not parse definition: $error")

        fieldValue <- valueJson match
          case Json.Str(s) =>
            Right(FieldValueString(id = id, value = s, definition = definition))
          case Json.Num(n) =>
            Try(n.longValue()).toOption
              .map(l => FieldValueNumber(id = id, value = l, definition = definition))
              .toRight("value is not a valid number")
          case Json.Bool(b) =>
            Right(FieldValueBoolean(id = id, value = b, definition = definition))
          case _ => Left("unsupported value type")
      } yield fieldValue
    }

final case class FieldValueString(id: Long, value: String, definition: FieldDefinition) extends FieldValue
    derives JsonDecoder,
      JsonEncoder

final case class FieldValueNumber(id: Long, value: Long, definition: FieldDefinition) extends FieldValue
    derives JsonDecoder,
      JsonEncoder

final case class FieldValueBoolean(id: Long, value: Boolean, definition: FieldDefinition) extends FieldValue
    derives JsonDecoder,
      JsonEncoder

@JsonDeserialize(`using` = classOf[FieldValueUpdateDeserializer])
class FieldValueUpdate(val definition: Long, val value: Json)

object FieldValueUpdate:

  given JsonEncoder[FieldValueUpdate] = JsonEncoder[Json].contramap { fvu =>
    Json.Obj("definition" -> Json.Num(fvu.definition), "value" -> fvu.value)
  }

class FieldValueUpdateDeserializer extends ValueDeserializer[FieldValueUpdate]:

  // Jackson-style deserialization needed by fast-mcp-scala to read Tool argument payloads
  override def deserialize(p: JsonParser, ctx: DeserializationContext): FieldValueUpdate =
    val node: JsonNode = ctx.readTree(p)

    val definition = node.get("definition").asLong()
    val valueNode = node.get("value")
    val value = {
      if valueNode.isNull then throw new IllegalArgumentException("field.value cannot be null")
      else if valueNode.isString then Json.Str(valueNode.asString())
      else if valueNode.isBoolean then Json.Bool(valueNode.asBoolean())
      else if valueNode.isNumber then Json.Num(valueNode.decimalValue())
      else throw new IllegalArgumentException(s"Unsupported value type: ${valueNode.getNodeType}")
    }

    FieldValueUpdate(definition, value)

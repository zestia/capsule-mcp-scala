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

package com.zestia.capsulemcp.model.filter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonNode}
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import sttp.tapir.Schema.annotations.description
import zio.json.ast.Json
import zio.json.{JsonDecoder, JsonEncoder}

import scala.util.Try

/**
 * See <a href="https://developer.capsulecrm.com/v2/reference/filters"</a>
 */
@JsonDeserialize(`using` = classOf[ConditionDeserializer])
sealed trait Condition:
  val field: String
  val operator: String

final case class StringCondition(
    field: String,
    operator: String,
    value: Option[String]
) extends Condition derives JsonDecoder, JsonEncoder

final case class NumberCondition(
    field: String,
    operator: String,
    value: Long
) extends Condition derives JsonDecoder, JsonEncoder

final case class BooleanCondition(
    field: String,
    operator: String,
    value: Boolean
) extends Condition derives JsonDecoder, JsonEncoder

final case class MoneyCondition(
    field: String,
    operator: String,
    value: MoneyValue
) extends Condition derives JsonDecoder, JsonEncoder

final case class MoneyValue(
    currency: String,
    amount: Double
) derives JsonDecoder,
      JsonEncoder

object Condition:

  // ZIO serialisation used in HttpClient
  implicit val encoder: JsonEncoder[Condition] =
    JsonEncoder[Json].contramap { condition =>
      val valueFieldValue = condition match {
        case StringCondition(_, _, None) =>
          Json.Null
        case StringCondition(_, _, Some(value)) =>
          Json.Str(value)
        case NumberCondition(_, _, value) =>
          Json.Num(value)
        case BooleanCondition(_, _, value) =>
          Json.Bool(value)
        case MoneyCondition(_, _, value) =>
          Json.Obj(
            "currency" -> Json.Str(value.currency),
            "amount" -> Json.Num(value.amount)
          )
      }

      Json.Obj(
        "field" -> Json.Str(condition.field),
        "operator" -> Json.Str(condition.operator),
        "value" -> valueFieldValue
      )
    }

class ConditionDeserializer extends JsonDeserializer[Condition]:

  // Jackson-style deserialization needed by fast-mcp-scala to read Tool argument payloads
  override def deserialize(p: JsonParser, context: DeserializationContext): Condition =
    val node: JsonNode = p.getCodec.readTree(p)

    val field = node.get("field").asText()
    val operator = node.get("operator").asText()
    val valueNode = node.get("value")

    if (valueNode.isNull) {
      StringCondition(field, operator, None)
    } else if (valueNode.isTextual) {
      StringCondition(field, operator, Some(valueNode.asText()))
    } else if (valueNode.isNumber) {
      NumberCondition(field, operator, valueNode.asLong())
    } else if (valueNode.isBoolean) {
      BooleanCondition(field, operator, valueNode.asBoolean())
    } else if (valueNode.isObject) {
      val currency = valueNode.get("currency").asText()
      val amount = valueNode.get("amount").asDouble()
      MoneyCondition(field, operator, MoneyValue(currency, amount))
    } else {
      throw new IllegalArgumentException(s"Unsupported value type in condition: ${valueNode.getNodeType}")
    }

package com.zestia.capsulemcp.model

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

      Json.Obj(
        "id" -> Json.Num(fieldValue.id),
        "value" -> valueFieldValue,
        "definition" -> defJson
      )
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
          .map(_ => "id must be a string")
        definition <- definitionJson
          .as[FieldDefinition]
          .left
          .map(_ => "definition must be a string")

        fieldValue <- valueJson match
          case Json.Str(s) =>
            Right(
              FieldValueString(
                id = id,
                value = s,
                definition = definition
              )
            )
          case Json.Num(n) =>
            Try(n.longValue()).toOption
              .map(l =>
                FieldValueNumber(
                  id = id,
                  value = l,
                  definition = definition
                )
              )
              .toRight("value is not a valid number")
          case Json.Bool(b) =>
            Right(
              FieldValueBoolean(
                id = id,
                value = b,
                definition = definition
              )
            )
          case _ => Left("unsupported value type")
      } yield fieldValue
    }

final case class FieldValueString(
    id: Long,
    value: String,
    definition: FieldDefinition
) extends FieldValue with CsvSerialisable derives JsonDecoder, JsonEncoder:
  override def renderCsv: String = s"${definition.name}=${value}"

final case class FieldValueNumber(
    id: Long,
    value: Long,
    definition: FieldDefinition
) extends FieldValue with CsvSerialisable derives JsonDecoder, JsonEncoder:
  override def renderCsv: String = s"${definition.name}=${value}"

final case class FieldValueBoolean(
    id: Long,
    value: Boolean,
    definition: FieldDefinition
) extends FieldValue with CsvSerialisable derives JsonDecoder, JsonEncoder:
  override def renderCsv: String = s"${definition.name}=${value}"

package com.zestia.capsulemcp.model

import zio.json.*

case class Team(
    id: Long,
    name: Option[String]
) extends CsvSerialisable derives JsonDecoder, JsonEncoder:
  override def renderCsv: String = name.getOrElse("")

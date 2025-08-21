package com.zestia.capsulemcp.model

import zio.json.*

case class Tag(
    id: Long,
    name: String,
    description: Option[String]
) extends CsvSerialisable derives JsonDecoder,
      JsonEncoder:
  override def renderCsv: String = name

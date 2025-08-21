package com.zestia.capsulemcp.model

import zio.json.*

case class User(
    id: Long,
    username: String,
    name: String
) extends CsvSerialisable derives JsonDecoder,
      JsonEncoder:
  override def renderCsv: String = name

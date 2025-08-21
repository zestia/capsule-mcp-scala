package com.zestia.capsulemcp.model

trait CsvSerialisable:
  def renderCsv: String

trait PartiallyCsvSerialisable:
  def renderCsv: String
  

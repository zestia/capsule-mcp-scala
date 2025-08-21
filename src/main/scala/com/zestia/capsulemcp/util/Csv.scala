package com.zestia.capsulemcp.util

import com.zestia.capsulemcp.model.*

object Csv extends FileLogging {

  def render[T <: Product](
      rows: List[T],
      fieldsToInclude: List[String] = Nil
  ): String = {
    val delimiter: Char = ','
    if (rows.isEmpty) return ""

    // Derive headers from the first row if not given
    val allNames = rows.head.productElementNames.toList

    logger.info(s"Rendering ${rows.size} rows with ${allNames.size} columns")
    val headers =
      if (fieldsToInclude.nonEmpty) fieldsToInclude
      else allNames

    logger.info(s"Rendering ${headers.size} columns: ${headers.mkString(",")}")

    // Precompute index map
    val nameToIndex: Map[String, Int] = allNames.zipWithIndex.toMap

    // For each row, extract the requested columns by index
    try {
      val lines = rows.map { r =>
        val nameToValue: Map[String, Any] =
          r.productElementNames.zip(r.productIterator).toMap

        headers
          .map { h =>
            val v = nameToValue.getOrElse(h, "")
            escape(cellString(v))
          }
          .mkString(delimiter.toString)
      }

      (headers.map(escape).mkString(delimiter.toString) :: lines)
        .mkString("\n")

    } catch {
      case t: Throwable =>
        logger.error(s"Unexpected error writing to CSV: $t")

        "Unexpected error writing to CSV"
    }
  }

  private def escape(s: String): String = {
    val needsQuotes =
      s.exists(ch => ch == ',' || ch == '"' || ch == '\n' || ch == '\r')
    val escapedQuotes =
      if (!needsQuotes) s else "\"" + s.replace("\"", "\"\"") + "\""
    escapedQuotes.replaceAll(
      "\n",
      " "
    ) // escape newlines allowed in places like addresses
  }

  private def cellString(value: Any): String = {
    value match {
      case null                             => ""
      case None                             => ""
      case Some(v)                          => cellString(v)
      case xs: Iterable[_]                  => xs.map(cellString).mkString(";")
      case csvSerialisable: CsvSerialisable => csvSerialisable.renderCsv
      case csvSerialisable: PartiallyCsvSerialisable =>
        csvSerialisable.renderCsv
      case p: Product =>
        p.productIterator
          .map(cellString)
          .mkString(";") // default to flatten any other Products
      case s: String           => s
      case b: Boolean          => b.toString
      case n: java.lang.Number => n.toString
      case other               => other.toString
    }
  }
}

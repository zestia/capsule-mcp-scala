package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*
import io.circe.*

trait HasFilterSchema:
  /**
   * Valid filter fields for the entity. See https://developer.capsulecrm.com/v2/reference/filters#field-reference
   */
  protected val filterFields: List[FilterField] = List()

  def filterSchema: String = SchemaBuilders.objectSchema(
    properties = Map(
      "pagination" -> paginationSchema,
      "filter" -> buildFilterSchema(filterFields)
    ),
    required = List("filter")
  )

package com.zestia.capsulemcp.server.schemas

import com.zestia.capsulemcp.server.schemas.SchemaTypes.*
import io.circe.*

trait HasFilterSchema:
  /**
   * Valid filter fields for the entity. See https://developer.capsulecrm.com/v2/reference/filters#field-reference
   */
  protected val filterFields: List[FilterField] = List()

  val filterSchema: String = SchemaBuilders.objectSchema(
    Map(
      "pagination" -> paginationSchema,
      "filter" -> buildFilterSchema(filterFields),
      "required" -> Json.arr(
        Json.fromString("filter")
      )
    )
  )

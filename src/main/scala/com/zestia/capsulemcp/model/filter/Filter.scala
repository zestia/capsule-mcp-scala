package com.zestia.capsulemcp.model.filter

import com.zestia.capsulemcp.model.*
import sttp.tapir.Schema.annotations.*
import zio.json.*

case class Filter(conditions: List[SimpleCondition])
    derives JsonDecoder,
      JsonEncoder

case class FilterRequestWrapper(filter: Filter) derives JsonDecoder, JsonEncoder

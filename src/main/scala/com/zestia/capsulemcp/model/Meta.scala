package com.zestia.capsulemcp.model

import zio.json.*

case class Meta(totalCount: Long) derives JsonDecoder, JsonEncoder

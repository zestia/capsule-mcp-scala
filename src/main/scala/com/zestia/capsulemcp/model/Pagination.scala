/*
 * Copyright 2025 Zestia Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zestia.capsulemcp.model

import sttp.tapir.Schema.annotations.*
import zio.json.{JsonDecoder, JsonEncoder}

case class Pagination(
    @description("Page number") @default(Pagination.DEFAULT_PAGE) page: Int = Pagination.DEFAULT_PAGE,
    @description("Page size") @default(
      Pagination.DEFAULT_PER_PAGE
    ) perPage: Int = Pagination.DEFAULT_PER_PAGE
) derives JsonDecoder,
      JsonEncoder:

  def toMap: Map[String, Int] =
    this.productElementNames
      .zip(this.productIterator)
      .collect { case (name, value: Int) =>
        name -> value
      }
      .toMap

object Pagination:
  val DEFAULT_PAGE: Int = 1
  val DEFAULT_PER_PAGE: Int = 100

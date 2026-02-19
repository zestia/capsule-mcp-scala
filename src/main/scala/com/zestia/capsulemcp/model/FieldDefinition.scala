/*
 * Copyright 2026 Zestia Ltd
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

import zio.json.*

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/field_definition"</a>
 */
case class FieldDefinition(
    id: Long,
    name: String,
    `type`: Option[String],
    description: Option[String],
    important: Option[Boolean],
    tag: Option[Tag],
    options: Option[List[String]],
    captureRule: Option[String]
) derives JsonDecoder,
      JsonEncoder

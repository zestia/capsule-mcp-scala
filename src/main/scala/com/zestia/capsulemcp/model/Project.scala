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
 * See <a href="https://developer.capsulecrm.com/v2/models/board"</a>
 */
final case class Board(id: Long, name: String, description: Option[String]) derives JsonDecoder, JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/nested_stage"</a>
 */
final case class Stage(id: Long, name: String, description: Option[String], board: Option[Board])
    derives JsonDecoder,
      JsonEncoder

/**
 * See <a href="https://developer.capsulecrm.com/v2/models/project"</a>
 */
final case class Project(
    id: Long,
    party: Option[Party],
    name: String,
    description: Option[String],
    owner: Option[User],
    team: Option[Team],
    status: Option[String],
    opportunity: Option[Opportunity],
    stage: Option[Stage],
    createdAt: Option[String],
    updatedAt: Option[String],
    expectedCloseOn: Option[String],
    closedOn: Option[String],
    lastContactedAt: Option[String],
    tags: Option[List[Tag]],
    fields: Option[List[FieldValue]],
    missingImportantFields: Option[Boolean]
) derives JsonDecoder,
      JsonEncoder
